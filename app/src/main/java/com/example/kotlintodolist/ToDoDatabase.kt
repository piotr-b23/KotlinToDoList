package com.example.kotlintodolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [ToDoEntity::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun getDAO(): ToDoDAO

    companion object {
        val databaseName = "taskdatabase"
        var toDoDatabase: ToDoDatabase? = null

        fun getInstance(context: Context): ToDoDatabase?{
           context.deleteDatabase(databaseName);
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