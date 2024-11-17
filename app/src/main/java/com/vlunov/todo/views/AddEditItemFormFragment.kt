package com.vlunov.todo.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.vlunov.todo.R
import com.vlunov.todo.models.TodoItem
import com.vlunov.todo.utils.DateTimePicker;
import com.vlunov.todo.utils.TodoDatabaseHelper

class AddEditItemFormFragment : Fragment() {
    private lateinit var dbHelper: TodoDatabaseHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHelper = TodoDatabaseHelper(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_todo_item, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.add_edit)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titleFld: EditText = view.findViewById(R.id.addEditItemTitleFld)
        val descFld: EditText = view.findViewById(R.id.addEditItemDescFld)
        val dueDateFld: EditText = view.findViewById(R.id.addEditItemDateDueFld)
        val createBtn: Button = view.findViewById(R.id.addEditItemCreateBtn)

        dueDateFld.setOnClickListener {
            DateTimePicker(dueDateFld).showDateFirst(requireContext())
        }

        titleFld.addTextChangedListener {
            createBtn.visibility = if (titleFld.text.toString().isEmpty()) View.GONE else View.VISIBLE
        }

        createBtn.setOnClickListener {
            dbHelper.insertTodo(TodoItem(
                title = titleFld.text.toString(),
                desc = descFld.text.toString(),
                dueDate = dueDateFld.text.toString(),
                isDone = false
            ))

            activity?.supportFragmentManager?.popBackStack()

            titleFld.setText("")
            descFld.setText("")
            dueDateFld.setText("")
        }

        return view
    }
}