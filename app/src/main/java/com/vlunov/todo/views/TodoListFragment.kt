package com.vlunov.todo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlunov.todo.R
import com.vlunov.todo.viewModels.TodoViewModel

class TodoListFragment : Fragment() {
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoLstAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)

        val listView: RecyclerView = view.findViewById(R.id.todoListItemsLst)
        val addBtn: FloatingActionButton = view.findViewById(R.id.todoListAddBtn)
        val delBtn: FloatingActionButton = view.findViewById(R.id.todoListDelBtn)

        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        viewModel.todoList.observe(viewLifecycleOwner) { todos ->
            adapter.updateData(todos)
        }

        adapter = TodoLstAdapter(this) { isChecked ->
            delBtn.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(context)

        viewModel.loadTodos()

        addBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddEditItemFormFragment())
                .addToBackStack(null)
                .commit()
        }

        delBtn.setOnClickListener {
            delBtn.visibility = View.GONE
            viewModel.deleteItemsByIDs(adapter.getChechedItems().map { it.id }.toList())
        }

        return view
    }
}
