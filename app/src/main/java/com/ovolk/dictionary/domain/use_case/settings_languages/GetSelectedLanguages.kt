package com.ovolk.dictionary.domain.use_case.settings_languages

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.R
import com.ovolk.dictionary.data.mapper.LanguageMapper
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.domain.model.select_languages.SharedLanguage
import com.ovolk.dictionary.util.USER_STATE_PREFERENCES
import java.lang.reflect.Type
import javax.inject.Inject

class GetSelectedLanguages @Inject constructor(
    val application: Application,
    val mapper: LanguageMapper
) {
    fun getLanguagesTo(): List<Language> {
        return getSelectedLanguages(LanguagesType.LANG_TO)
    }

    fun getLanguagesFrom(): List<Language> {
        return getSelectedLanguages(LanguagesType.LANG_FROM)
    }

    private fun getSelectedLanguages(key: LanguagesType): List<Language> {
        val languageRaw = application.resources.openRawResource(R.raw.languges).bufferedReader()
            .use { it.readText() }
        val gson = Gson()

        val languageCodeListType: Type = object : TypeToken<List<SharedLanguage>>() {}.type
        val languageListType: Type = object : TypeToken<List<Language>>() {}.type

        val langCodeListText = application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        ).getString(key.toString(), "")

        return if (langCodeListText == null || langCodeListText.isEmpty()) {
            emptyList()
        } else {
            val langCodeList: List<SharedLanguage> =
                gson.fromJson(langCodeListText, languageCodeListType)
            val languageList: List<Language> = gson.fromJson(languageRaw, languageListType)

            return languageList.filter { item -> langCodeList.find { it.langCode == item.langCode } != null }
        }
    }


    fun getLanguagesForSelect(
        savedLangFromCode: String?,
        savedLangToCode: String?
    ): Map<String, List<SelectLanguage>> {
        val gson = Gson()
        val languageRaw = application.resources.openRawResource(R.raw.languges).bufferedReader()
            .use { it.readText() }

        val languageCodeListType: Type = object : TypeToken<List<SharedLanguage>?>() {}.type
        val languageListType: Type = object : TypeToken<List<Language>>() {}.type

        val languageList: List<Language> = gson.fromJson(languageRaw, languageListType)

        val savedSelectedListPref = application.getSharedPreferences(
            USER_STATE_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        val langTo = savedSelectedListPref.getString(LanguagesType.LANG_TO.toString(), "")
        val langFrom = savedSelectedListPref.getString(LanguagesType.LANG_FROM.toString(), "")

        val languageToList: List<SharedLanguage> = if (langTo == null || langTo.isEmpty()) {
            emptyList()
        } else {
            gson.fromJson(langTo, languageCodeListType)
        }

        val languageFromList: List<SharedLanguage> = if (langFrom == null || langFrom.isEmpty()) {
            emptyList()
        } else {
            gson.fromJson(langFrom, languageCodeListType)
        }

        val from = prepareLangList(languageList, languageFromList, savedLangFromCode)
        val to = prepareLangList(languageList, languageToList, savedLangToCode)
        return mapOf(
            "languageFrom" to from,
            "languageTo" to to
        )
    }


    // convert SharedLanguage into SelectLanguage
    private fun prepareLangList(
        langList: List<Language>,
        langCodeList: List<SharedLanguage>,
        savedLangCode: String? = null,
    ): List<SelectLanguage> {
        val filtered =
            langList.filter { item -> langCodeList.find { it.langCode == item.langCode } != null }

        val res = filtered.mapIndexed { index, language ->
            mapper.languageToSelectLanguage(
                language.copy(
                    isChecked = if (savedLangCode == null) {
                        index == 0
                    } else {
                        language.langCode == savedLangCode
                    }
                )
            )
        }
        return res
    }

}