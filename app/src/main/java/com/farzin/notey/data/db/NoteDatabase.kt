package com.farzin.notey.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farzin.notey.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {


    abstract fun getDao(): NoteDao

}