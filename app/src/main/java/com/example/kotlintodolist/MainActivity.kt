package com.example.kotlintodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ToDoAdapter.OnTodoItemClickedListener {

    private var toDoDatabase: ToDoDatabase? = null
    private var toDoAdapter: ToDoAdapter? = null
    private lateinit var toDoRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDoDatabase = ToDoDatabase.getInstance(this)
        toDoAdapter = ToDoAdapter()
        toDoAdapter?.setTodoItemClickedListener(this)
        toDoRecyclerView = findViewById(R.id.toDoRecyclerView)

        toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskList()
        toDoRecyclerView.adapter = toDoAdapter
        toDoRecyclerView.layoutManager = LinearLayoutManager(this)
        toDoRecyclerView.hasFixedSize()


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskList()
        toDoRecyclerView.adapter = toDoAdapter
        toDoRecyclerView.layoutManager = LinearLayoutManager(this)
        toDoRecyclerView.hasFixedSize()
    }

    override fun onTodoItemClicked(todo: ToDoEntity) {
        TODO("Not yet implemented")
    }

}