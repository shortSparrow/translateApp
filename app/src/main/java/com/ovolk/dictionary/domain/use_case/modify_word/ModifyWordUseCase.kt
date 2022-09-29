package com.ovolk.dictionary.domain.use_case.modify_word

import com.ovolk.dictionary.data.mapper.WordMapper
import com.ovolk.dictionary.domain.TranslatedWordRepository
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ModifyWordUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
    private val mapper: WordMapper,
) {
    suspend operator fun invoke(word: ModifyWord): Long = coroutineScope {
        return@coroutineScope repository.modifyWord(word = word, mapper = mapper)
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

    suspend fun modifyTranslateWithUpdatePriority(
        wordId: Long,
        translates: List<Translate>, priority: Int
    ) = repository.addHiddenTranslateWithUpdatePriority(
        translates = translates.map {
            mapper.translateLocalToDb(
                wordId = wordId,
                translate = it
            )
        },
        wordId = wordId,
        priority = priority
    )

    suspend fun modifyOnlySound(
        id: Long,
        sound: WordAudio?, // english sound
    ) = coroutineScope {
        async {
            val oldWord = repository.getWordById(id)
            val word = oldWord.copy(sound = sound)
            repository.modifyWordInfo(mapper.modifyWordToWordInfoDb(word))
        }
    }.await()

}