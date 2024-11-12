package com.vlunov.todo.viewModels

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vlunov.todo.ReminderBroadcastReceiver
import com.vlunov.todo.models.TodoItem
import com.vlunov.todo.utils.TodoDatabaseHelper

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = TodoDatabaseHelper(application)
    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> get() = _todoList

    fun loadTodos() {
        _todoList.value = dbHelper.getAllTodos()
    }

    fun deleteItemsByIDs(ids: List<Int?>) {
        dbHelper.deleteAllByIDs(ids)
        loadTodos()
    }

    fun updateTodoItem(id: Long, updatedTodo: TodoItem) {
        dbHelper.updateTodo(id, updatedTodo)
        loadTodos()
    }

    fun updateMultipleTodos(items: List<Pair<Long, TodoItem>>) {
        dbHelper.updateAllByIDs(items)
        loadTodos()
    }

    fun scheduleReminder(context: Context, todoItem: TodoItem) {
        val reminderTimeMillis = todoItem.reminderTimeMillis ?: return

        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("title", todoItem.title)
            putExtra("desc", todoItem.desc)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            todoItem.id ?: reminderTimeMillis.toInt(), // Unique request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderTimeMillis,
            pendingIntent
        )
    }

}
