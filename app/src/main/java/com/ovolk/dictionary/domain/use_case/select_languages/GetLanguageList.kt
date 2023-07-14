package com.ovolk.dictionary.domain.use_case.select_languages

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import java.lang.reflect.Type
import javax.inject.Inject

class GetLanguageList @Inject constructor(
    val application: Application,
) {

     fun getLanguageList( selectedLangCode: String? = null): List<Language> {
        val languageRaw = application.resources.openRawResource(R.raw.languges).bufferedReader()
            .use { it.readText() }
        val gson = Gson()

        val languageListType: Type = object : TypeToken<List<Language>?>() {}.type
        var languageList: List<Language> = gson.fromJson(languageRaw, languageListType)

        selectedLangCode?.let {
            languageList = languageList.map { lang ->
                return@map if (selectedLangCode == lang.langCode) {
                    lang.copy(isChecked = true)
                } else {
                    lang
                }

            }
        }

        return languageList
    }
}
