package com.ovolk.dictionary.domain.use_case.settings_languages

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import java.lang.reflect.Type
import javax.inject.Inject

class GetSelectedLanguages @Inject constructor(
    val application: Application
) {
    fun getLanguagesTo(): List<Language> {
        return getSelectedLanguages(LanguagesType.LANG_TO)
    }

    fun getLanguagesFrom(): List<Language> {
        return getSelectedLanguages(LanguagesType.LANG_FROM)
    }

    private fun getSelectedLanguages(key: LanguagesType): List<Language> {
        val gson = Gson()
        val languageListType: Type = object : TypeToken<List<Language>?>() {}.type

        val savedSelectedListPref = application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        ).getString(key.toString(), "")

        val savedSelectedFromLanguage: List<Language> = gson.fromJson(savedSelectedListPref, languageListType)

        return savedSelectedFromLanguage.ifEmpty {
            emptyList()
        }
    }
}