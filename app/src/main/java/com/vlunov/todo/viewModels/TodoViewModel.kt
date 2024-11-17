package com.vlunov.todo.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vlunov.todo.models.TodoItem
import com.vlunov.todo.utils.TodoDatabaseHelper

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = TodoDatabaseHelper(application)
    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> get() = _todoList

    fun loadTodos() {
        _todoList.value = dbHelper.getAllTodos()
    }

    fun getTodoById(id: Int): TodoItem? {
        return dbHelper.getOneById(id)
    }

    fun deleteItemsByIDs(ids: List<Int?>) {
        dbHelper.deleteAllByIDs(ids)
        loadTodos()
    }

    fun updateTodoItem(id: Int?, updatedTodo: TodoItem?) {
        dbHelper.updateTodo(id, updatedTodo)
        loadTodos()
    }

    fun updateMultipleTodos(items: List<Pair<Long, TodoItem>>) {
        dbHelper.updateAllByIDs(items)
        loadTodos()
    }
}
