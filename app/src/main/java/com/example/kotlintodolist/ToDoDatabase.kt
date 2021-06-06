package com.example.kotlintodolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [ToDoEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun getDAO(): ToDoDAO

    companion object {
        val databaseName = "taskdatabase"
        var toDoDatabase: ToDoDatabase? = null

        fun getInstance(context: Context): ToDoDatabase?{
            if (toDoDatabase == null){
                toDoDatabase = Room.databaseBuilder(context,
                ToDoDatabase::class.java,
                ToDoDatabase.databaseName)
                        .allowMainThreadQueries()
                        .build()
            }
            return toDoDatabase
        }
    }
}