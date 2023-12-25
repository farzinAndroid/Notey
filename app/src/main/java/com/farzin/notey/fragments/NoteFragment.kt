package com.farzin.notey.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.INVISIBLE
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.farzin.notey.R
import com.farzin.notey.activities.MainActivity
import com.farzin.notey.adapters.RVNotesAdapter
import com.farzin.notey.databinding.FragmentNoteBinding
import com.farzin.notey.utils.SwipeToDelete
import com.farzin.notey.utils.hideKeyboard
import com.farzin.notey.viewmodel.DataStoreViewModel
import com.farzin.notey.viewmodel.NoteActivityViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NoteFragment : Fragment(R.layout.fragment_note) {

    private lateinit var binding: FragmentNoteBinding
    private val noteActivityViewModel by viewModels<NoteActivityViewModel>()
    private lateinit var rvNotesAdapter: RVNotesAdapter


    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false).apply {
            duration = 350
        }

        enterTransition = MaterialElevationScale(true).apply {
            duration = 350
        }
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteBinding.bind(view)
        val activity = activity as MainActivity
        val navController = Navigation.findNavController(view)
        requireView().hideKeyboard()
        CoroutineScope(Dispatchers.Main).launch {
            delay(10)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.parseColor("#A5A5A5")
        }



        binding.language.setOnClickListener {
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            val newFragment = SimpleAlertDialogFragment()
            newFragment.show(ft,"dialog")
        }

        binding.addNoteFab.setOnClickListener {
            binding.appBar.visibility = INVISIBLE
            navController.navigate(NoteFragmentDirections.actionNoteFragmentToSaveOrUpdateFragment())
        }

        binding.innerFab.setOnClickListener {
            binding.appBar.visibility = INVISIBLE
            navController.navigate(NoteFragmentDirections.actionNoteFragmentToSaveOrUpdateFragment())
        }

        recyclerViewDisplay()
        swipeToDelete(binding.rvNote)


        // implement search
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.noData.isVisible = false
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString().isNotEmpty()) {
                    val text = s.toString()
                    val query = "%$text%"
                    if (query.isNotEmpty()) {
                        noteActivityViewModel.searchNote(query).observe(viewLifecycleOwner) {
                            rvNotesAdapter.submitList(it)
                        }
                    } else {
                        observerDataChanges()
                    }
                } else {
                    observerDataChanges()
                }
            }


            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.search.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus()
                requireView().hideKeyboard()
            }

            return@setOnEditorActionListener true
        }

        binding.rvNote.setOnScrollChangeListener { _, scrollX, scrollY, _, oldscrollY ->

            when {
                scrollY > oldscrollY -> {
                    binding.fabText.isVisible = false
                }

                scrollX == scrollY -> {
                    binding.fabText.isVisible = true
                }

                else -> {
                    binding.fabText.isVisible = true
                }
            }
        }


    }

    private fun swipeToDelete(rvNote: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val note = rvNotesAdapter.currentList[position]
                var actionBTNTab = false
                noteActivityViewModel.deleteNote(note)
                binding.search.apply {
                    hideKeyboard()
                    clearFocus()
                }

                if (binding.search.toString().isEmpty()) {
                    observerDataChanges()
                }

                val snackBar = Snackbar.make(
                    requireView(),
                    getString(R.string.note_deleted),
                    Snackbar.LENGTH_SHORT
                )
                    .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)

                        }

                        override fun onShown(transientBottomBar: Snackbar?) {
                            transientBottomBar?.setAction(getString(R.string.undo)) {
                                noteActivityViewModel.addNote(note)
                                actionBTNTab = true
                                binding.noData.isVisible = false
                            }
                            super.onShown(transientBottomBar)
                        }
                    })
                    .apply {
                        animationMode = Snackbar.ANIMATION_MODE_SLIDE
                        setAnchorView(R.id.add_note_fab)
                    }
                snackBar.setActionTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellowOrange
                    )
                )
                    .show()
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(rvNote)
    }

    private fun recyclerViewDisplay() {
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                setUpRecyclerView(2)
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                setUpRecyclerView(3)
            }
        }
    }

    private fun setUpRecyclerView(spanCount: Int) {
        binding.rvNote.apply {
            layoutManager =
                StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            rvNotesAdapter = RVNotesAdapter()
            rvNotesAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = rvNotesAdapter
            postponeEnterTransition(300L, TimeUnit.MILLISECONDS)
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        observerDataChanges()
    }

    private fun observerDataChanges() {
        noteActivityViewModel.allNotes.observe(viewLifecycleOwner) { list ->
            binding.noData.isVisible = list.isEmpty()
            rvNotesAdapter.submitList(list)
        }
    }

}