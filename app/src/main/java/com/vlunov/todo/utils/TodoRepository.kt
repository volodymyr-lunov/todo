package com.vlunov.todo.utils

import com.vlunov.todo.models.TodoItem
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where

class TodoRepository {
    private val realm: Realm = Realm.getDefaultInstance()

    fun insertTodoItem(todoItem: TodoItem) {
        realm.executeTransaction { transactionRealm ->
            transactionRealm.insert(todoItem)
        }
    }

    fun getAllTodoItems(): List<TodoItem> {
        return realm.where<TodoItem>().findAll().sort("createDate", Sort.DESCENDING).toList()
    }

    fun getTodoItemById(id: String): TodoItem? {
        return realm.where<TodoItem>().equalTo("id", id).findFirst()
    }

    fun updateTodoItem(id: String?, updatedFields: (TodoItem) -> Unit) {
        realm.executeTransaction {
            val item = it.where<TodoItem>().equalTo("id", id).findFirst()
            item?.let(updatedFields)
        }
    }

    fun deleteAllByIds(ids: List<String?>) {
        realm.executeTransaction {
            it.where<TodoItem>()
                .`in`("id", ids.toTypedArray())
                .findAll()
                .deleteAllFromRealm()
        }
    }


    fun deleteTodoItemById(id: String) {
        realm.executeTransaction {
            it.where<TodoItem>().equalTo("id", id).findFirst()?.deleteFromRealm()
        }
    }

    fun deleteAllTodoItems() {
        realm.executeTransaction {
            it.where<TodoItem>().findAll().deleteAllFromRealm()
        }
    }

    fun close() {
        realm.close()
    }
}
