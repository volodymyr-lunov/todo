package com.vlunov.todo.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import java.util.Calendar

class DateTimePicker(private val field: EditText?) {
    private val c: Calendar = Calendar.getInstance()

    fun showDateFirst(context: Context) {
        showDatePicker(context) { showTimePicker(context) }
    }

    fun showTimeFirst(context: Context) {
        showTimePicker(context) { showDatePicker(context) }
    }

    private fun showDatePicker(context: Context, cb: (() -> Unit)? = null) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                field?.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                cb?.invoke()
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun showTimePicker(context: Context, cb: (() -> Unit)? = null) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val dateStr = field?.text
                field?.setText("$dateStr $hourOfDay:$minute")
                cb?.invoke()
            },
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            false
        )

        timePickerDialog.show()
    }
}
