package com.ovolk.dictionary.domain.use_case.select_languages

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ovolk.dictionary.data.mapper.LanguageMapper
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.util.IS_CHOOSE_LANGUAGE
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import javax.inject.Inject

class UpdateTranslatableLanguages @Inject constructor(
    val application: Application,
    val mapper: LanguageMapper
) {

    fun saveLanguagesTo(list: List<Language>) {
        save(list, LanguagesType.LANG_TO)
    }

    fun saveLanguagesFrom(list: List<Language>) {
        save(list, LanguagesType.LANG_FROM)
    }

    fun saveLanguagesByKey(list: List<Language>, key: LanguagesType) {
        save(list, key)
    }

    private fun save(list: List<Language>, key: LanguagesType) {
        application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
            .edit()
            .apply {
                val gson = Gson()
                val json = gson.toJson(
                    list.filter { it.isChecked }.map { mapper.languageToSharedLang(it) }
                )
                putString(key.toString(), json)
                if (key == LanguagesType.LANG_TO) {
                    putBoolean(IS_CHOOSE_LANGUAGE, true)
                }
                apply()
            }
    }
}