package com.farzin.notey.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzin.notey.model.Note
import com.farzin.notey.repo.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteActivityViewModel @Inject constructor(private val repo:NoteRepo) : ViewModel() {

    val allNotes = repo.getAllNotes()

    fun addNote(note:Note){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNote(note)
        }
    }

    fun updateNote(note:Note){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(note)
        }
    }

    fun searchNote(query: String) = repo.searchNotes(query)
}