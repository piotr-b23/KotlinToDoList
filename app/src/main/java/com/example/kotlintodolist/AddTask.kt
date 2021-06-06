package com.example.kotlintodolist

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintodolist.ToDoDatabase.Companion.toDoDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddTask : AppCompatActivity() {

    private lateinit var description: EditText
    private lateinit var taskTitle: EditText
    private lateinit var spinner: Spinner
    private lateinit var dateTextView: TextView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var ifDateChanged = false;

        val pickDateButton: Button = findViewById(R.id.pickDateButton)
        val addTaskButton: Button = findViewById(R.id.addTaskButton)
        dateTextView = findViewById(R.id.dateDeadlineTextView)
        description= findViewById(R.id.taskDescription)
        taskTitle = findViewById(R.id.titleEditText)

        spinner = findViewById(R.id.TaskPrioritySpinner)

        val id = intent.getIntExtra("id",0)

        setSupportActionBar(findViewById(R.id.toolbar))

        if (id != 0){
            findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Edytuj zadanie"
        }
        else {
            findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Dodaj zadanie"
        }
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
                year = selectedYear
                month = selectedMonth
                day = selectedDay
            }, year, month, day)
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
            ifDateChanged = true
        }

        if (id != 0){
            description.setText(intent.getStringExtra("description"))
            taskTitle.setText(intent.getStringExtra("title"))
            spinner.setSelection(convertPriorityToDisplay(intent.getStringExtra("priority")))
            var dateToDisplay = Date(intent.getLongExtra("deadline",0)).time
            var date = Date(dateToDisplay)
            var format = SimpleDateFormat("dd.MM.yyyy")
            dateTextView.setText(format.format(date))


        }

        addTaskButton.setOnClickListener {

            if (checkIfDataOK()) {
                val task = ToDoEntity(taskTitle.text.toString(),
                        description.text.toString(),
                        Date(System.currentTimeMillis()),
                        Date(year - 1900, month, day),
                        spinner.selectedItem.toString(),
                        convertPriority(spinner.selectedItem.toString()),
                        false)

                if (id != 0) {
                    task.tableID = id;
                    if (ifDateChanged==false)
                    {
                        task.taskDeadline = Date(intent.getLongExtra("deadline",0))
                    }
                    toDoDatabase!!.getDAO().updateTask(task)
                    finish()
                } else {
                    toDoDatabase!!.getDAO().addNewTask(task)
                    finish()
                }
            }
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

    fun convertPriorityToDisplay(priority: String?): Int{
        when (priority) {
            "wysoki" -> return 0
            "średni" -> return 1
            "niski" -> return 2
        }
        return 0
    }

    fun checkIfDataOK(): Boolean{

        if(taskTitle.text.isEmpty() || taskTitle.text.isBlank()){
            Toast.makeText(applicationContext, "Wprowadź tytuł zadania", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (description.text.isEmpty() || description.text.isBlank()){
            Toast.makeText(applicationContext, "Wprowadź opis zadania", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (dateTextView.text.isEmpty() || dateTextView.text.isBlank()){

            Toast.makeText(applicationContext, "Wybierz datę zakończenia", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}
