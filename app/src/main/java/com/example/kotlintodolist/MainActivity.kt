package com.example.kotlintodolist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ToDoAdapter.OnTodoItemClickedListener {

    private var toDoDatabase: ToDoDatabase? = null
    private var toDoAdapter: ToDoAdapter? = null
    private var notificationShowed = false
    private lateinit var toDoRecyclerView: RecyclerView
    private lateinit var spinner: Spinner

    lateinit var builder: NotificationCompat.Builder
    private val channelId = "i.apps.notifications"
    private val notificationTopic = "ToDo List"
    private val notificationText = "Kończy się czas na wykonanie niektórych zadań!!!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDoDatabase = ToDoDatabase.getInstance(this)
        toDoAdapter = ToDoAdapter()
        toDoAdapter?.setTodoItemClickedListener(this)
        toDoRecyclerView = findViewById(R.id.toDoRecyclerView)
        spinner = findViewById(R.id.TaskSortSpinner)

        createNotificationChannel()

        builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notificationTopic)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        ArrayAdapter.createFromResource(
                this,
                R.array.sort,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.setSelection(0)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onResume()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskListSoon()
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

        if(spinner.selectedItemPosition==0){
            toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskListSoon()
        }
        else if(spinner.selectedItemPosition==1){
            toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskListLate()
        }
        else if(spinner.selectedItemPosition==2){
            toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskListPriorityHighest()
        }
        else if(spinner.selectedItemPosition==3){
            toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskListPriorityLowest()
        }
        else if(spinner.selectedItemPosition==4){
            toDoAdapter?.toDoList=toDoDatabase?.getDAO()?.getTaskListStatus()
        }

        toDoRecyclerView.adapter = toDoAdapter
        toDoRecyclerView.layoutManager = LinearLayoutManager(this)
        toDoRecyclerView.hasFixedSize()


        if(toDoAdapter?.isDeadlineNear() == true && notificationShowed == false) {
            with(NotificationManagerCompat.from(this)) {
                notify(1111, builder.build())
            }
            notificationShowed = true
            toDoAdapter?.resetDeadlineNear()
        }

    }

    override fun onTodoItemClicked(todo: ToDoEntity) {
        val intent = Intent(this, AddTask::class.java)
        intent.putExtra("id",todo.tableID);
        intent.putExtra("title",todo.taskTitle);
        intent.putExtra("description",todo.taskDescription);
        intent.putExtra("priority",todo.taskPriority);
        intent.putExtra("deadline",todo.taskDeadline?.time);
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ToDoNotification"
            val descriptionText = "TaskDeadlineNear"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}