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
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                field?.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                cb?.invoke()
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun showTimePicker(context: Context, cb: (() -> Unit)? = null) {
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val dateStr = field?.text
                field?.setText("$dateStr $hourOfDay:$minute")
                cb?.invoke()
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }
}
