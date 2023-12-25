package com.farzin.notey.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.farzin.notey.R
import com.farzin.notey.activities.MainActivity
import com.farzin.notey.databinding.BottomSheetLayoutBinding
import com.farzin.notey.databinding.FragmentSaveOrUpdateBinding
import com.farzin.notey.model.Note
import com.farzin.notey.utils.hideKeyboard
import com.farzin.notey.viewmodel.NoteActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SaveOrUpdateFragment : Fragment(R.layout.fragment_save_or_update) {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentSaveOrUpdateBinding
    private var note: Note? = null
    private var color = -1
    private lateinit var result: String
    private val noteActivityViewModel by viewModels<NoteActivityViewModel>()
    private val currentDate = SimpleDateFormat.getInstance().format(Date())
    private val job = CoroutineScope(Dispatchers.Main)
    private val args: SaveOrUpdateFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            scrimColor = Color.TRANSPARENT
            duration = 300
        }

        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveOrUpdateBinding.bind(view)



        navController = Navigation.findNavController(view)
        val activity = activity as MainActivity


        ViewCompat.setTransitionName(
            binding.noteContentFragParent,
            "recyclerView_${args.note?.id}"
        )

        val isLeftToRight= TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())==View.LAYOUT_DIRECTION_LTR

        if (!isLeftToRight){
            binding.backButton.rotation = 180f
        }

        binding.backButton.setOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
        }




        binding.saveNote.setOnClickListener {
            saveNote()
        }

        try {
            binding.etNoteContent.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    binding.bottomBar.visibility = View.VISIBLE
                    binding.etNoteContent.setStylesBar(binding.styleBar)
                } else {
                    binding.bottomBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "$e")
        }

        binding.fabColorPicker.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            )


            val bottomSheetView: View = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

            with(bottomSheetDialog) {
                setContentView(bottomSheetView)
                show()
            }

            val bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                colorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener { value ->
                        color = value
                        binding.apply {
                            noteContentFragParent.setBackgroundColor(color)
                            toolbarFragNoteContent.setBackgroundColor(color)
                            bottomBar.setBackgroundColor(color)
                            activity.window.statusBarColor = color
                        }
                        bottomSheetBinding.bottomSheetParent.setCardBackgroundColor(color)
                    }
                }
                bottomSheetParent.setCardBackgroundColor(color)
            }
            bottomSheetView.post {
                bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        // opens existing item
        setUpNote()

    }

    @SuppressLint("SetTextI18n")
    private fun setUpNote() {
        val note = args.note
        val title = binding.etTitle
        val content = binding.etNoteContent
        val lastEdited = binding.lastEdited

        if (note == null){
            binding.lastEdited.text =
                "${getString(R.string.edited_on)}: ${SimpleDateFormat.getDateInstance().format(Date())}"
        }
        if (note!=null){
            title.setText(note.title)
            content.renderMD(note.content)
            lastEdited.text = "${getString(R.string.edited_on)}: ${note.date}"
            color = note.color
            binding.apply {
                job.launch {
                    delay(10)
                    noteContentFragParent.setBackgroundColor(color)
                }

                toolbarFragNoteContent.setBackgroundColor(color)
                bottomBar.setBackgroundColor(color)

            }
            activity?.window?.statusBarColor = note.color
        }
    }

    private fun saveNote() {
        if (binding.etNoteContent.text.toString().isEmpty() || binding.etTitle.text.toString()
                .isEmpty()
        ) {
            Toast.makeText(activity, getString(R.string.note_shouldnt_be_empty), Toast.LENGTH_SHORT).show()
        } else {
            note = args.note

            when (note) {
                null -> {
                    noteActivityViewModel.addNote(
                        Note(
                            0,
                            title = binding.etTitle.text.toString(),
                            content = binding.etNoteContent.getMD(),
                            color = color,
                            date = currentDate
                        )
                    )

                    result = "ذخیره شد"
                    setFragmentResult(
                        "key",
                        bundleOf("bundleKey" to result)
                    )

                    navController.navigate(SaveOrUpdateFragmentDirections.actionSaveOrUpdateFragmentToNoteFragment())
                }

                else -> {
                    updateNote()
                    navController.popBackStack()
                }
            }
        }
    }

    private fun updateNote() {
        if (note != null) {
            noteActivityViewModel.updateNote(
                Note(
                    id = note!!.id,
                    content = binding.etNoteContent.getMD(),
                    title = binding.etTitle.text.toString(),
                    date = currentDate,
                    color = color
                )
            )
        }
    }

}