package com.vlunov.todo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
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

        listView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        viewModel.todoList.observe(viewLifecycleOwner) { todos ->
            adapter.updateData(todos)
        }

        adapter = TodoLstAdapter(this)

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(context)

        viewModel.loadTodos()

        setUpItemTouchHelper(listView)

        addBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddItemFormFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun setUpItemTouchHelper(recyclerView: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val itemToDelete = adapter.getItemAtPosition(position)
                val newItem = itemToDelete.copy()

                adapter.removeItemAt(position)
                viewModel.deleteItemByID(itemToDelete.id)

                Snackbar.make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG).setAction("Undo") {
                    adapter.addItemAt(position, newItem)
                    viewModel.addTodoItem(newItem, false)

                    recyclerView.scrollToPosition(position)
                }.show()
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
