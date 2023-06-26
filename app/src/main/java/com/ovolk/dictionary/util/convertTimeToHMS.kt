package com.ovolk.dictionary.util

import android.annotation.SuppressLint
import java.util.concurrent.TimeUnit

// TODO maybe delete
@SuppressLint("DefaultLocale")
fun convertTimeToHMS(millis: Long): String {
    return String.format(
        "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(millis)
        ),
        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(millis)
        )
    )
}