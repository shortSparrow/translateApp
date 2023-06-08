package com.ovolk.dictionary.domain.use_case.modify_word

import com.ovolk.dictionary.data.database.TranslatedWordRepositoryImpl
import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.domain.TranslatedWordRepository
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

enum class AddedWordResult { SUCCESS, WORD_ALREADY_EXIST }
data class AddWordIfNotExistResult(val status: AddedWordResult, val wordId: Long)

class ModifyWordUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
    private val mapper: WordMapper,
) {
    suspend operator fun invoke(word: ModifyWord): Long = coroutineScope {
        return@coroutineScope repository.modifyWord(word = word, mapper = mapper)
    }


    suspend fun addWordIfNotExist(word: ModifyWord): AddWordIfNotExistResult {
        val searchedWordId = repository.getWordByValue(value = word.value, langFrom = word.langFrom, langTo = word.langTo)
        if (searchedWordId == TranslatedWordRepositoryImpl.WORD_IS_NOT_FOUND.toLong()) {
            val wordId = invoke(word)
            return AddWordIfNotExistResult(status = AddedWordResult.SUCCESS, wordId = wordId)
        }
        return AddWordIfNotExistResult(status = AddedWordResult.WORD_ALREADY_EXIST, wordId = searchedWordId)
    }

    suspend fun modifyTranslates(
        wordId: Long,
        translates: List<Translate>
    ) = repository.modifyWordTranslates(translates.map {
        mapper.translateLocalToDb(
            wordId = wordId,
            translate = it
        )
    })

    suspend fun modifyOnlySound(
        id: Long,
        sound: WordAudio?,
    ) = coroutineScope {
        async {
            val oldWord = repository.getWordById(id)
            val word = oldWord.copy(sound = sound)
            // must rewrite full word, because on replacing only the word deletes translations and hints
            repository.modifyWord(word, mapper)
        }
    }.await()


}