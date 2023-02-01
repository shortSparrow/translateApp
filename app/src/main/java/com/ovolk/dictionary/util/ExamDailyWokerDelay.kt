package com.ovolk.dictionary.util

import java.util.*

class PushFrequency {
    companion object {
        const val NONE = 0
        const val ONCE_AT_DAY = 86_400_000
        const val ONCE_AT_TWO_DAYS = 86_400_000 * 2
        const val ONCE_AT_THREE_DAYS = 86_400_000 * 3
        const val ONCE_AT_SIX_DAYS = 86_400_000 * 5

        const val DEFAULT_HOURS = 10
        const val DEFAULT_MINUTES = 0
    }
}

fun getExamReminderDelayFromNow(
    hours: Int,
    minutes: Int,
    frequencyDelay: Int
): Long {
    val dueDate = Calendar.getInstance()
    val currentDate = Calendar.getInstance()

    dueDate.set(Calendar.HOUR_OF_DAY, hours)
    dueDate.set(Calendar.MINUTE, minutes)
    dueDate.set(Calendar.SECOND, 0)
    dueDate.set(Calendar.MILLISECOND, 0)

    if ((dueDate.timeInMillis - currentDate.timeInMillis) <= 0) {
        dueDate.add(Calendar.MILLISECOND, frequencyDelay)
    }

    return dueDate.timeInMillis
}


