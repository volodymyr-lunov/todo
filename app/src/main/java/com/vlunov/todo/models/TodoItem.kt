package com.vlunov.todo.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

val df = SimpleDateFormat("dd-MM-yyyy HH:mm");

open class TodoItem(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @Required var title: String = "",
    var desc: String = "",
    var dueDate: Date? = null,
    var createDate: Date? = null,
    var isDone: Boolean = false
) : RealmObject() {
    fun copy() : TodoItem {
        return TodoItem(
            title = this.title,
            desc = this.desc,
            dueDate = this.dueDate,
            createDate = this.createDate,
            isDone = this.isDone
        )
    }

    companion object {
        fun parseDate(sDate: String): Date? {
            return df.parse(sDate)
        }

        fun formatDate(date: Date): Date? {
            return parseDate(df.format(date))
        }
    }
}