package com.ovolk.dictionary.util.helpers

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ovolk.dictionary.util.SHOW_VARIANTS_EXAM_AVAILABLE_LANGUAGES
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import com.ovolk.dictionary.util.showVariantsAvailableLanguages

fun setShowVariantsExamAvailableLanguagesIdNeeded(application: Application) {
    val sharedPref: SharedPreferences =
        application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
    val availableLangListString =
        sharedPref.getString(SHOW_VARIANTS_EXAM_AVAILABLE_LANGUAGES, "")

    if (availableLangListString == null || availableLangListString.isEmpty()) {
        sharedPref.edit().apply {
            val stringList = Gson().toJson(showVariantsAvailableLanguages)
            putString(SHOW_VARIANTS_EXAM_AVAILABLE_LANGUAGES, stringList)
            apply()
        }
    }
}