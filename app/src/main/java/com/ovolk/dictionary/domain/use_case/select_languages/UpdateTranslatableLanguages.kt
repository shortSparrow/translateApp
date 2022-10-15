package com.ovolk.dictionary.domain.use_case.select_languages

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ovolk.dictionary.presentation.select_languages.Language
import com.ovolk.dictionary.util.IS_CHOOSE_LANGUAGE
import com.ovolk.dictionary.util.LANGUAGE_FROM
import com.ovolk.dictionary.util.LANGUAGE_TO
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import javax.inject.Inject

class UpdateTranslatableLanguages @Inject constructor(val application: Application) {

    fun saveLanguagesTo(list: List<Language>) {
        save(list, LANGUAGE_TO)
    }

    fun saveLanguagesFrom(list: List<Language>) {
        save(list, LANGUAGE_FROM)
    }

    private fun save(list: List<Language>, key: String) {
        application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
            .edit()
            .apply {
                val gson = Gson()
                val json = gson.toJson(list.filter { it.isChecked })
                putString(key, json)
                if (key == LANGUAGE_TO) {
                    putBoolean(IS_CHOOSE_LANGUAGE, true)
                }
                apply()
            }
    }
}