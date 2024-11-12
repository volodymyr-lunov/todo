package com.vlunov.todo.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vlunov.todo.models.TodoItem

class TodoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESC TEXT,
                $COLUMN_DUE_DATE TEXT,
                $COLUMN_IS_DONE INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTodo(todo: TodoItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, todo.title)
            put(COLUMN_DESC, todo.desc)
            put(COLUMN_DUE_DATE, todo.dueDate)
            put(COLUMN_IS_DONE, if (todo.isDone) 1 else 0)
        }
        return db.insert(TABLE_NAME, null, values).also {
            db.close()
        }
    }

    fun getAllTodos(): MutableList<TodoItem> {
        val todoList = mutableListOf<TodoItem>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    var id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                    val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC))
                    val dueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE))
                    val isDone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_DONE)) == 1
                    todoList.add(TodoItem(title, desc, dueDate, isDone, id))
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return todoList
    }

    fun updateTodo(id: Long, todo: TodoItem): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, todo.title)
            put(COLUMN_DESC, todo.desc)
            put(COLUMN_DUE_DATE, todo.dueDate)
            put(COLUMN_IS_DONE, if (todo.isDone) 1 else 0)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString())).also {
            db.close()
        }
    }

    fun updateAllByIDs(todoItems: List<Pair<Long, TodoItem>>) {
        if (todoItems.isEmpty()) return

        val db = writableDatabase
        val idList = todoItems.joinToString(", ") { it.first.toString() }

        val updateTitleCase = StringBuilder("$COLUMN_TITLE = CASE $COLUMN_ID ")
        val updateDescCase = StringBuilder("$COLUMN_DESC = CASE $COLUMN_ID ")
        val updateDueDateCase = StringBuilder("$COLUMN_DUE_DATE = CASE $COLUMN_ID ")
        val updateIsDoneCase = StringBuilder("$COLUMN_IS_DONE = CASE $COLUMN_ID ")

        for ((id, todo) in todoItems) {
            updateTitleCase.append("WHEN $id THEN '${todo.title}' ")
            updateDescCase.append("WHEN $id THEN '${todo.desc}' ")
            updateDueDateCase.append("WHEN $id THEN '${todo.dueDate}' ")
            updateIsDoneCase.append("WHEN $id THEN ${if (todo.isDone) 1 else 0} ")
        }

        updateTitleCase.append("END")
        updateDescCase.append("END")
        updateDueDateCase.append("END")
        updateIsDoneCase.append("END")

        val updateQuery = """
            UPDATE $TABLE_NAME 
            SET 
                $updateTitleCase, 
                $updateDescCase, 
                $updateDueDateCase, 
                $updateIsDoneCase 
            WHERE $COLUMN_ID IN ($idList)
        """.trimIndent()

        db.execSQL(updateQuery)
        db.close()
    }

    fun deleteAllByIDs(ids: List<Int?>) {
        if (ids.isEmpty()) return

        val db = writableDatabase
        val idList = ids.joinToString(", ")

        val deleteQuery = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID IN ($idList)"

        db.execSQL(deleteQuery)
        db.close()
    }

    fun deleteTodo(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString())).also {
            db.close()
        }
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TodoDatabase.db"
        private const val TABLE_NAME = "TodoTable"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESC = "desc"
        private const val COLUMN_DUE_DATE = "due_date"
        private const val COLUMN_IS_DONE = "is_done"
    }
}
