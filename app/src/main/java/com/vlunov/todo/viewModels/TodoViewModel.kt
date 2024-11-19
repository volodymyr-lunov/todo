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
        repository.insertTodoItem(item)
        loadTodos()
    }

    fun getTodoById(id: String): TodoItem? {
        return repository.getTodoItemById(id)
    }

    fun deleteItemsByIDs(ids: List<String?>) {
        repository.deleteAllByIds(ids)
        loadTodos()
    }

    fun deleteItemByID(id: String) {
        repository.deleteTodoItemById(id)
        loadTodos()
    }

    fun updateTodoItem(id: String?, updatedFields: (TodoItem) -> Unit) {
        repository.updateTodoItem(id, updatedFields)
        loadTodos()
    }
}
