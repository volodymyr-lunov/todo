package com.vlunov.todo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Visibility
import com.vlunov.todo.R
import com.vlunov.todo.viewModels.TodoViewModel

class ViewTodoActivityFragment: Fragment() {
    private var todoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoId = it.getString(ARG_TODO_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_todo, container, false)
        val viewModel: TodoViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
        val item = viewModel.getTodoById(todoId!!)

        val titleView: TextView = view.findViewById(R.id.viewTodoTitleText)
        val descView: TextView = view.findViewById(R.id.viewTodoDescText)
        val dueDateView: TextView = view.findViewById(R.id.viewTodoDueDateText)

        titleView.setText(item?.title)
        if (item!!.desc.isNotEmpty()) {
            descView.setText(item?.desc)
            descView.visibility = View.VISIBLE
        }
        if (item?.dueDate != null) {
            dueDateView.setText(item?.dueDate.toString())
            dueDateView.visibility = View.VISIBLE
        }

        return view
    }

    companion object {
        private const val ARG_TODO_ID = "todo_id"

        fun newInstance(todoId: String) = ViewTodoActivityFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TODO_ID, todoId)
            }
        }
    }
}