package com.example.kotlintodolist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ToDoAdapter(var toDoList: List<ToDoEntity>? = ArrayList<ToDoEntity>()): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private var onTodoItemClickedListener: OnTodoItemClickedListener?= null
    private var taskDeadlineNear = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoAdapter.ToDoViewHolder {
        val layout = R.layout.task_row
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ToDoViewHolder(view, toDoList!!)
    }

    override fun getItemCount(): Int {
        return if(toDoList!!.isEmpty()) 0 else toDoList!!.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int){
        holder.view.setOnClickListener{onTodoItemClickedListener!!.onTodoItemClicked(toDoList!!.get(position))}
        holder.view.setOnLongClickListener{
            onTodoItemClickedListener!!.onTodoItemLongClicked(toDoList!!.get(position))
            true
        }
        holder.onBindViews(position)
    }

    inner class ToDoViewHolder(val view: View, val toDoList: List<ToDoEntity>):RecyclerView.ViewHolder(view) {
        fun onBindViews(position: Int) {
            if (itemCount != 0) {
                view.findViewById<TextView>(R.id.taskTitle).text = toDoList.get(position).taskTitle
                view.findViewById<TextView>(R.id.taskDescription).text = toDoList.get(position).taskDescription


                var dateToDisplay = toDoList.get(position).taskDeadline!!.time
                var date = Date(dateToDisplay)
                var format = SimpleDateFormat("dd.MM.yyyy")

                val calendar = Calendar.getInstance()
                if(dateToDisplay <= calendar.timeInMillis+90000000){
                    taskDeadlineNear = true
                }

                view.findViewById<TextView>(R.id.taskDeadlineDate).text = "Do zrobienia na: " + format.format(date)


                when (toDoList.get(position).taskPriorityValue) {
                    3 -> {
                        view.findViewById<TextView>(R.id.rowColor).setBackgroundColor(Color.parseColor("#ffb3b3"))
                    }
                    2 -> {
                        view.findViewById<TextView>(R.id.rowColor).setBackgroundColor(Color.parseColor("#ffffb3"))
                    }
                    else -> {
                        view.findViewById<TextView>(R.id.rowColor).setBackgroundColor(Color.parseColor("#b3ffb3"))
                    }
                }
                if(toDoList.get(position).taskIsDone==true){
                    view.findViewById<TextView>(R.id.rowColor).setBackgroundColor(Color.parseColor("#d8d8d8"))
                }

            }
        }
    }

        fun setTodoItemClickedListener(onTodoItemClickedListener: OnTodoItemClickedListener){
            this.onTodoItemClickedListener = onTodoItemClickedListener
        }

    fun isDeadlineNear(): Boolean?{
        return this.taskDeadlineNear
    }

    fun resetDeadlineNear(){
        this.taskDeadlineNear = false
    }

        interface OnTodoItemClickedListener{
            fun onTodoItemClicked(todo: ToDoEntity)
            fun onTodoItemLongClicked(todo: ToDoEntity)
        }
    }
