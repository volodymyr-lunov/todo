package com.vlunov.todo.models

data class TodoItem(
    var title: String,
    var desc: String,
    var dueDate: String,
    var isDone: Boolean = false,
    var id: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TodoItem) return false

        return if (id != null && other.id != null) {
            id == other.id
        } else {
            title == other.title &&
                    desc == other.desc &&
                    dueDate == other.dueDate &&
                    isDone == other.isDone
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: listOf(title, desc, dueDate, isDone).hashCode()
    }
}