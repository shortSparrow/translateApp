package com.example.ttanslateapp.util

import timber.log.Timber
import java.util.*

fun getExamWorkerDelay(): Long {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()
    // Set Execution around 18:30:00 PM
    dueDate.set(Calendar.HOUR_OF_DAY, 10)
    dueDate.set(Calendar.MINUTE, 0)
    dueDate.set(Calendar.SECOND, 0)
    if (dueDate.before(currentDate)) {
        Timber.tag("getExamWorkerDelay").d("LESS")
        dueDate.add(Calendar.HOUR_OF_DAY, 24)
    } else {
        Timber.tag("getExamWorkerDelay").d("MORE")
    }

//    dueDate.add(Calendar.MINUTE, 1)
    return dueDate.timeInMillis - currentDate.timeInMillis
}