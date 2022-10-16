package com.ovolk.dictionary.domain.use_case.select_languages

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.util.UKRAINE_COUNTRY_CODE
import com.ovolk.dictionary.util.UKRAINE_LANGUAGE_CODE
import java.util.*
import javax.inject.Inject


class GetPreferredLanguages @Inject constructor(
    val application: Application,
) {

    operator fun invoke(languageList: List<Language>): List<Language> {
        val systemLanguage = Locale.getDefault().language

        val telephoneManager = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val country = telephoneManager.networkCountryIso

        val preferredLanguages =
            if (country.lowercase() == UKRAINE_COUNTRY_CODE) {
                languageList.filter { it.langCode == systemLanguage || it.langCode == UKRAINE_LANGUAGE_CODE }
            } else {
                languageList.filter { it.langCode == systemLanguage }
            }


        return preferredLanguages
    }


}

