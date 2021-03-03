package com.example.kotlintodolist

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintodolist.ToDoDatabase.Companion.toDoDatabase
import java.util.*

class AddTask : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val pickDateButton: Button = findViewById(R.id.pickDateButton)
        val addTaskButton: Button = findViewById(R.id.addTaskButton)
        val dateTextView: TextView = findViewById(R.id.textView2)
        val description: EditText = findViewById(R.id.taskDescription)
        val taskTitle: EditText = findViewById(R.id.editTextTitle)

        val spinner: Spinner = findViewById(R.id.TaskPrioritySpinner)

        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        toDoDatabase = ToDoDatabase.getInstance(this)


        ArrayAdapter.createFromResource(
                this,
                R.array.priority,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        pickDateButton.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, selectedYear, selectedMonth, selectedDay->
                val adjustedMonth = selectedMonth+1
                dateTextView.setText("$selectedDay.$adjustedMonth.$selectedYear")
            }, year, month, day)
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
        }

        addTaskButton.setOnClickListener {
            val task = ToDoEntity(taskTitle.text.toString(),
                    description.text.toString(),
                    "data1",
                    "data2",
                    spinner.selectedItem.toString(),
                    convertPriority(spinner.selectedItem.toString()),
                    false)
            toDoDatabase!!.getDAO().addNewTask(task)
            finish()
        }
    }

    fun convertPriority(priority: String): Int{
        when (priority) {
            "wysoki" -> return 3
            "średni" -> return 2
            "niski" -> return 1
        }
        return 0
    }
}
