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
import java.util.*
import kotlin.collections.ArrayList

class ToDoAdapter(var toDoList: List<ToDoEntity>? = ArrayList<ToDoEntity>()): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private var onTodoItemClickedListener: OnTodoItemClickedListener?= null

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
        holder.onBindViews(position)
    }

    inner class ToDoViewHolder(val view: View, val toDoList: List<ToDoEntity>):RecyclerView.ViewHolder(view) {
        fun onBindViews(position: Int) {
            if (itemCount != 0) {
                view.findViewById<TextView>(R.id.taskTitle).text = toDoList.get(position).taskTitle
                view.findViewById<TextView>(R.id.taskDescription).text = toDoList.get(position).taskDescription


/*                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val result = formatter.parse(toDoList.get(position).taskDeadline.toString())*/


                view.findViewById<TextView>(R.id.taskDeadlineDate).text = toDoList.get(position).taskDeadline.toString()

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

                view.findViewById<ImageView>(R.id.imageViewDone).setOnClickListener{


                }
                view.findViewById<ImageView>(R.id.imageViewDelete).setOnClickListener{

                }


                //view.findViewById<TextView>(R.id.taskDeadlineDate).text = toDoList.get(position).taskDeadline
            }
        }
    }

        fun setTodoItemClickedListener(onTodoItemClickedListener: OnTodoItemClickedListener){
            this.onTodoItemClickedListener = onTodoItemClickedListener
        }

        interface OnTodoItemClickedListener{
            fun onTodoItemClicked(todo: ToDoEntity)
        }
    }
