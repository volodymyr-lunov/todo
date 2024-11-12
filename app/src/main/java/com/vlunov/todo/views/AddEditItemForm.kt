package com.vlunov.todo.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.vlunov.todo.R
import com.vlunov.todo.models.TodoItem
import com.vlunov.todo.utils.DateTimePicker;
import com.vlunov.todo.utils.TodoDatabaseHelper

class AddEditItemForm : AppCompatActivity() {
    private var dbHelper: TodoDatabaseHelper = TodoDatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.add_edit_todo_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_edit)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titleFld: EditText = findViewById(R.id.addEditItemTitleFld)
        val descFld: EditText = findViewById(R.id.addEditItemDescFld)
        val dueDateFld: EditText = findViewById(R.id.addEditItemDateDueFld)
        val createBtn: Button = findViewById(R.id.addEditItemCreateBtn)

        dueDateFld.setOnClickListener {
            DateTimePicker(dueDateFld).showDateFirst(this)
        }

        titleFld.addTextChangedListener {
            createBtn.visibility = if (titleFld.text.toString().isEmpty()) View.GONE else View.VISIBLE
        }

        createBtn.setOnClickListener {
            val item = TodoItem(
                titleFld.getText().toString(),
                descFld.getText().toString(),
                dueDateFld.getText().toString(),
                false)

            dbHelper.insertTodo(item)

            startActivity(Intent(this, MainActivity::class.java))

            titleFld.setText("")
            descFld.setText("")
            dueDateFld.setText("")
        }
    }
}