package com.example.kotlintodolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task")
class ToDoEntity(
        @ColumnInfo(name = "todo_title")
        var taskTitle:String = "",
        @ColumnInfo(name = "todo_description")
        var taskDescription:String = "",
        @ColumnInfo(name = "todo_date")
        var taskDate: Date? = null,
        @ColumnInfo(name = "todo_deadline")
        var taskDeadline:Date? = null,
        @ColumnInfo(name = "todo_priority")
        var taskPriority:String = "",
        @ColumnInfo(name = "todo_priority_value")
        var taskPriorityValue:Int = 0,
        @ColumnInfo(name = "todo_status")
        var taskIsDone:Boolean = false,
        @PrimaryKey(autoGenerate = true) var tableID: Int = 0)