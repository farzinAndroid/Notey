package com.farzin.notey.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farzin.notey.utils.Constants.TBL_NAME
import java.io.Serializable

@Entity(tableName = TBL_NAME)
data class Note(

    @PrimaryKey(autoGenerate = true)
    var id : Int = 1,
    val title:String,
    val content:String,
    val date:String,
    var color:Int = -1
) : Serializable
