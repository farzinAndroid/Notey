package com.farzin.notey.repo

import com.farzin.notey.data.db.NoteDao
import com.farzin.notey.model.Note
import javax.inject.Inject

class NoteRepo @Inject constructor(private val dao: NoteDao) {


    fun getAllNotes() = dao.getAllNotes()

    fun searchNotes(query: String) = dao.searchNote(query)

    suspend fun updateNote(note:Note) = dao.updateNote(note)

    suspend fun addNote(note: Note) = dao.addNote(note)

    suspend fun deleteNote(note: Note) = dao.deleteNote(note)

}