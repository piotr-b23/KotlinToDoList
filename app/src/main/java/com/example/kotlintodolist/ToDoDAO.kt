package com.example.kotlintodolist

import androidx.room.*

@Dao
interface ToDoDAO {
    @Query("SELECT * FROM task ORDER BY tableID ASC")
    fun getTaskList(): List<ToDoEntity>

    @Query("SELECT * FROM task WHERE tableID=:id")
    fun getTask(id: Int): ToDoEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewTask(task: ToDoEntity)

    @Update
    fun updateTask(task : ToDoEntity)

    @Delete
    fun deleteTask(task: ToDoEntity)
}