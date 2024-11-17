package com.vlunov.todo.views

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.vlunov.todo.R
import com.vlunov.todo.models.TodoItem

class TodoLstAdapter (
    private val frame: Fragment,
    private val onCheckedChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<TodoLstAdapter.TodoViewHolder> () {
    private val todos = mutableListOf<TodoItem>()
    private val checkedItems = mutableSetOf<TodoItem>()

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val viewHolder = TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_list_tem,
                parent,
                false
            )
        )

        return viewHolder
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = todos[position]
        val textView = holder.itemView.findViewById<TextView>(R.id.todoListItemTextTxt)
        val checkbox = holder.itemView.findViewById<CheckBox>(R.id.todoListItemCheckCkx)

        textView.text = item.title
        checkbox.isChecked = item.isDone

        toggleStrikeThrough(textView, item.isDone)

        holder.itemView.setOnClickListener {
            val fragment = ViewTodoActivityFragment.newInstance(item.id!!)

            frame.requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit()
        }

        checkbox.setOnCheckedChangeListener {_, isChecked ->
            toggleStrikeThrough(textView, isChecked)
            checkbox.isChecked = isChecked
            item.isDone = isChecked

            if (isChecked) {
                checkedItems.add(item)
            } else {
                checkedItems.remove(item)
            }

            onCheckedChanged(checkedItems.isNotEmpty())
        }
    };

    override fun getItemCount(): Int {
        return todos.size
    }

    fun getChechedItems() : MutableSet<TodoItem> {
        return checkedItems
    }

    fun updateData(newItems: List<TodoItem>) {
        todos.clear()
        todos.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(view: TextView, isCheched: Boolean) {
        if (isCheched) {
            view.paintFlags = view.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            view.paintFlags = view.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}