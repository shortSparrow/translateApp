package com.ovolk.dictionary.util.helpers

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun getAudioPath(context: Context, fileName: String): String {
    val dirName =
        context.getExternalFilesDir(null)?.absolutePath

    return "$dirName/$fileName"
}


fun generateFileName(): String {
    val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US)
    val date = Date()

    return "Recording_" + formatter.format(date) + ".3gp"
}

