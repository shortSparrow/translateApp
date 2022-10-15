package com.ovolk.dictionary.domain.use_case.select_languages

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.select_languages.Language
import com.ovolk.dictionary.util.LANGUAGE_FROM
import com.ovolk.dictionary.util.LANGUAGE_TO
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import java.lang.reflect.Type
import javax.inject.Inject

class GetLanguageList @Inject constructor(
    val application: Application,
) {

    fun getLanguageListTo(): List<Language> {
        return getLanguageList(LANGUAGE_TO)
    }

    fun getLanguageListFrom(): List<Language> {
        return getLanguageList(LANGUAGE_FROM)
    }

     private fun getLanguageList(key: String): List<Language> {
        val languageRaw = application.resources.openRawResource(R.raw.languges).bufferedReader()
            .use { it.readText() }
        val gson = Gson()

        val languageListType: Type = object : TypeToken<List<Language>?>() {}.type
        var languageList: List<Language> = gson.fromJson(languageRaw, languageListType)

        val savedSelectedListPref = application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        ).getString(key, "")

        savedSelectedListPref?.let {
            if (it.isNotEmpty()) {
                val savedSelectedFromLanguage: List<Language> = gson.fromJson(it, languageListType)
                languageList = languageList.map { lang ->
                    return@map savedSelectedFromLanguage.find { it.langCode == lang.langCode }
                        ?: lang
                }
            }
        }

        return languageList
    }
}
