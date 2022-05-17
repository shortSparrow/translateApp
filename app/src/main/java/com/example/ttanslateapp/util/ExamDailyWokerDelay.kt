package com.example.ttanslateapp.util

import java.util.*

fun getExamWorkerDelay(): Long {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()
    // Set Execution around 18:30:00 PM
    dueDate.set(Calendar.HOUR_OF_DAY, 18)
    dueDate.set(Calendar.MINUTE, 30)
    dueDate.set(Calendar.SECOND, 0)
    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.HOUR_OF_DAY, 24)
    }
    return dueDate.timeInMillis - currentDate.timeInMillis
}