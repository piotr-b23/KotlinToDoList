package com.example.kotlintodolist

import androidx.room.*

@Dao
interface ToDoDAO {
    @Query("SELECT * FROM task ORDER BY todo_status ASC, todo_deadline ASC, todo_priority_value DESC")
    fun getTaskListSoon(): List<ToDoEntity>

    @Query("SELECT * FROM task ORDER BY todo_status ASC, todo_deadline DESC, todo_priority_value DESC")
    fun getTaskListLate(): List<ToDoEntity>

    @Query("SELECT * FROM task ORDER BY todo_status ASC, todo_priority_value DESC")
    fun getTaskListPriorityHighest(): List<ToDoEntity>

    @Query("SELECT * FROM task ORDER BY todo_status ASC, todo_priority_value ASC")
    fun getTaskListPriorityLowest(): List<ToDoEntity>

    @Query("SELECT * FROM task ORDER BY todo_status DESC, todo_priority_value DESC")
    fun getTaskListStatus(): List<ToDoEntity>

    @Query("SELECT * FROM task WHERE tableID=:id")
    fun getTask(id: Int): ToDoEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewTask(task: ToDoEntity)

    @Update
    fun updateTask(task : ToDoEntity)

    @Delete
    fun deleteTask(task: ToDoEntity)
}