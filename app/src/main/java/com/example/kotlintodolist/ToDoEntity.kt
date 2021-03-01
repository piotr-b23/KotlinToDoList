package com.example.kotlintodolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
class ToDoEntity (
    @ColumnInfo(name = "todo_description")
    var taskDescription:String = "",
    @ColumnInfo(name = "todo_priority")
    var taskPriority:String = "",
    @PrimaryKey(autoGenerate = true) var tableID: Int = 0){
        var detail: String = ""
    }
