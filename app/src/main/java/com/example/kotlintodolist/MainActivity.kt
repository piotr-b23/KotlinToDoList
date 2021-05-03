package com.example.kotlintodolist

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
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
        val intent = Intent(this, AddTask::class.java)
        intent.putExtra("id",todo.tableID);
        intent.putExtra("title",todo.taskTitle);
        intent.putExtra("description",todo.taskDescription);
        intent.putExtra("priority",todo.taskPriority);
        startActivity(intent)
    }

    override fun onTodoItemLongClicked(todo: ToDoEntity) {
        val alertDialog = AlertDialog.Builder(this)
            .setItems(R.array.options, DialogInterface.OnClickListener { dialog, which ->
                if (which==0){
                    todo.taskIsDone = true;
                    ToDoDatabase.toDoDatabase!!.getDAO().updateTask(todo)
                    onResume()

                }else{
                    toDoDatabase?.getDAO()?.deleteTask(todo)
                    onResume()
                }
                dialog.dismiss()
            })
            .create()
        alertDialog.show()
    }

}