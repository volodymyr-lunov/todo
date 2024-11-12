package com.vlunov.todo.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlunov.todo.R
import com.vlunov.todo.viewModels.TodoViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TodoLstAdapter
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.todo_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.todoListLyt)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView: RecyclerView = findViewById(R.id.todoListItemsLst)
        val addBtn: FloatingActionButton = findViewById(R.id.todoListAddBtn)
        val delBtn: FloatingActionButton = findViewById(R.id.todoListDelBtn)

        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        viewModel.todoList.observe(this) { todos ->
            adapter.updateData(todos)
        }

        adapter = TodoLstAdapter { isChecked ->
            delBtn.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(this)

        viewModel.loadTodos()

        addBtn.setOnClickListener {
            startActivity(Intent(this, AddEditItemForm::class.java))
        }

        delBtn.setOnClickListener {
            delBtn.visibility = View.GONE
            viewModel.deleteItemsByIDs(adapter.getChechedItems().map { it.id }.toList())
        }

        //startService(Intent(this, MainService::class.java))
    }
}