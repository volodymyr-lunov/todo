package com.vlunov.todo.views

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vlunov.todo.R
import com.vlunov.todo.utils.TodoDatabaseHelper

class ViewTodoActivity: AppCompatActivity() {
    private var dbHelper: TodoDatabaseHelper = TodoDatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.view_todo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewTodoLyt)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = getIntent();
        val id = intent.getIntExtra("id", 0)
        val item = dbHelper.getOneById(id)

        val titleView: TextView = findViewById(R.id.viewTodoTitleText)
        val descView: TextView = findViewById(R.id.viewTodoDescText)
        val dueDateView: TextView = findViewById(R.id.viewTodoDueDateText)

        titleView.setText(item?.title)
        descView.setText(item?.desc)
        dueDateView.setText(item?.dueDate)
    }
}