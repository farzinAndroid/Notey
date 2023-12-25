package com.farzin.notey.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farzin.notey.model.Note
import com.farzin.notey.utils.Constants.TBL_NAME

@Dao
interface NoteDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note:Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("select * from note_table order by id desc")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("select * from note_table where title like :query or content like :query or date like :query order by id desc")
    fun searchNote(query: String) : LiveData<List<Note>>

    @Delete
    suspend fun deleteNote(note:Note)

}