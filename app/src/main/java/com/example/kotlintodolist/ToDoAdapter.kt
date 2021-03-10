package com.example.kotlintodolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
