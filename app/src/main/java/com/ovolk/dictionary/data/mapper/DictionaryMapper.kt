package com.ovolk.dictionary.data.mapper

import com.ovolk.dictionary.data.model.DictionaryDb
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.dictionary.SelectableDictionary
import javax.inject.Inject

class DictionaryMapper @Inject constructor() {
    fun dictionaryDbToDictionary(dictionaryDb: DictionaryDb): Dictionary =
        Dictionary(
            id = dictionaryDb.id,
            title = dictionaryDb.title,
            langFromCode = dictionaryDb.langFromCode,
            langToCode = dictionaryDb.langToCode,
            isActive = dictionaryDb.isActive,
        )

    fun dictionaryToSelectableDictionary(dictionary: Dictionary): SelectableDictionary =
        SelectableDictionary(
            id = dictionary.id,
            title = dictionary.title,
            langFromCode = dictionary.langFromCode,
            langToCode = dictionary.langToCode,
            isActive = dictionary.isActive,
            isSelected = false,
        )

    fun dictionaryToDictionaryDb(dictionary: Dictionary): DictionaryDb = DictionaryDb(
        id = dictionary.id,
        title = dictionary.title,
        langFromCode = dictionary.langFromCode,
        langToCode = dictionary.langToCode,
        isActive = dictionary.isActive,
    )
}