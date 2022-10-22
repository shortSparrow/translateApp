package com.ovolk.dictionary.domain.use_case.select_languages

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.domain.model.select_languages.SharedLanguage
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import java.lang.reflect.Type
import javax.inject.Inject

class GetLanguageList @Inject constructor(
    val application: Application,
) {

    fun getLanguageListTo(): List<Language> {
        return getLanguageList(LanguagesType.LANG_TO)
    }

    fun getLanguageListFrom(): List<Language> {
        return getLanguageList(LanguagesType.LANG_FROM)
    }

    fun getLanguageListByKey(key: LanguagesType): List<Language> {
        return getLanguageList(key)
    }

    private fun getLanguageList(key: LanguagesType): List<Language> {
        val languageRaw = application.resources.openRawResource(R.raw.languges).bufferedReader()
            .use { it.readText() }
        val gson = Gson()

        val languageListType: Type = object : TypeToken<List<Language>?>() {}.type
        var languageList: List<Language> = gson.fromJson(languageRaw, languageListType)

        val languageCodeListType: Type = object : TypeToken<List<SharedLanguage>?>() {}.type

        val sharedLanguageList = application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        ).getString(key.toString(), "")

        sharedLanguageList?.let {
            if (it.isNotEmpty()) {
                val savedSelectedFromLanguage: List<SharedLanguage> =
                    gson.fromJson(it, languageCodeListType)

                languageList = languageList.map { lang ->
                    return@map if (savedSelectedFromLanguage.find { it.langCode == lang.langCode } != null) {
                        lang.copy(isChecked = true)
                    } else {
                        lang
                    }

                }
            }
        }

        return languageList
    }
}
