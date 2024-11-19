package com.vlunov.todo.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vlunov.todo.models.TodoItem
import com.vlunov.todo.utils.TodoRepository

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TodoRepository()
    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> get() = _todoList

    fun loadTodos() {
        _todoList.value = repository.getAllTodoItems()
    }

    fun addTodoItem(item: TodoItem) {
        addTodoItem(item, true)
    }

    fun addTodoItem(item: TodoItem, reload: Boolean) {
        repository.insertTodoItem(item)
        if (reload) loadTodos()
    }

    fun getTodoById(id: String): TodoItem? {
        return repository.getTodoItemById(id)
    }

    fun deleteItemsByIDs(ids: List<String?>) {
        repository.deleteAllByIds(ids)
        loadTodos()
    }

    fun deleteItemByID(id: String, reload: Boolean) {
        repository.deleteTodoItemById(id)
        if (reload) loadTodos()
    }

    fun deleteItemByID(id: String) {
        deleteItemByID(id, true)
    }

    fun updateTodoItem(id: String?, updatedFields: (TodoItem) -> Unit) {
        repository.updateTodoItem(id, updatedFields)
        loadTodos()
    }
}
