package com.farzin.notey.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farzin.notey.R
import com.farzin.notey.databinding.NoteItemLayoutBinding
import com.farzin.notey.fragments.NoteFragmentDirections
import com.farzin.notey.model.Note
import com.farzin.notey.utils.hideKeyboard
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import org.commonmark.node.SoftLineBreak

class RVNotesAdapter : ListAdapter<Note, RVNotesAdapter.NoteViewHolder>(DiffUtilCallback()) {


    inner class NoteViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = NoteItemLayoutBinding.bind(item)
        val title = binding.noteItemTitle
        val content = binding.noteContentItem
        val date = binding.noteDate
        val parent = binding.noteItemLayoutParent
        val markWon = Markwon.builder(item.context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(item.context))
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                    super.configureVisitor(builder)
                    builder.on(
                        SoftLineBreak::class.java
                    ) { visitor, _ ->
                        visitor.forceNewLine()
                    }
                }
            }).build()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        getItem(position).let {note->
            holder.apply {
                parent.transitionName = "recyclerView_${note.id}"
                title.text = note.title
                markWon.setMarkdown(content,note.content)
                date.text = note.date
                parent.setCardBackgroundColor(note.color)

                itemView.setOnClickListener {
                    val action = NoteFragmentDirections.actionNoteFragmentToSaveOrUpdateFragment()
                        .setNote(note)

                    val extras = FragmentNavigatorExtras(parent to "recyclerView_${note.id}")
                    it.hideKeyboard()
                    Navigation.findNavController(it).navigate(action,extras)
                }

                content.setOnClickListener {
                    val action = NoteFragmentDirections.actionNoteFragmentToSaveOrUpdateFragment()
                        .setNote(note)

                    val extras = FragmentNavigatorExtras(parent to "recyclerView_${note.id}")
                    it.hideKeyboard()
                    Navigation.findNavController(it).navigate(action,extras)
                }
            }
        }
    }
}