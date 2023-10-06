package com.ovolk.dictionary.domain.use_case.exam

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import java.lang.reflect.Type
import javax.inject.Inject

val answersLinks = mapOf("uk" to R.raw.word_list_ua, "en" to R.raw.word_list_en)

data class ExamAnswer(val version: Int, val data: List<String>)


class GetExamAnswersUseCase @Inject constructor(val application: Application) {
    private val gson = Gson()

    fun getAnswerList(lang: String, limit: Int): List<ExamAnswerVariant> {
        val linkToWordsResource = answersLinks[lang] ?: return emptyList()
        val languageRaw =
            application.resources.openRawResource(linkToWordsResource).bufferedReader()
                .use { it.readText() }

        val languageListType: Type = object : TypeToken<ExamAnswer>() {}.type
        val languageList: ExamAnswer = gson.fromJson(languageRaw, languageListType)

        // here the identifier is temporary and lives until the end of the examination session
        val list = languageList.data.shuffled()
            .mapIndexed { index, it -> ExamAnswerVariant(id = index.toLong(), value = it) }

        return list.take(limit)
    }
}