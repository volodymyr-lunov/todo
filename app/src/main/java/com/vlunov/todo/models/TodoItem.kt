package com.vlunov.todo.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.UUID

open class TodoItem(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @Required var title: String = "",
    var desc: String = "",
    var dueDate: String = "",
    var isDone: Boolean = false
) : RealmObject() {
    fun copy() : TodoItem {
        return TodoItem(
            title = this.title,
            desc = this.desc,
            dueDate = this.dueDate,
            isDone = this.isDone
        )
    }
}