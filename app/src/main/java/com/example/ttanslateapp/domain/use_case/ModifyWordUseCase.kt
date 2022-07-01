package com.example.ttanslateapp.domain.use_case

import com.example.ttanslateapp.data.mapper.WordMapper
import com.example.ttanslateapp.domain.TranslatedWordRepository
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordAudio
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ModifyWordUseCase @Inject constructor(
    private val repository: TranslatedWordRepository,
    private val mapper: WordMapper
) {
    suspend operator fun invoke(word: ModifyWord): Long = coroutineScope {
        val wordId = repository.modifyWordInfo(wordInfoDb = mapper.modifyWordToWordInfoDb(word))
        val translates = repository.modifyWordTranslates(
            translates = word.translates.map {
                mapper.translateLocalToDb(
                    translate = it,
                    wordId = wordId
                )
            }
        )
        val hints = repository.modifyWordHints(
            hints = word.hints.map {
                mapper.hintLocalToDb(
                    hint = it,
                    wordId = wordId
                )
            }
        )

        return@coroutineScope wordId
    }
    // FIXME delete wordId here, because we don't have one yet
//        repository.modifyWord(
//            wordInfoDb = mapper.modifyWordToWordFullDb(word),
//            translates = word.translates.map {
//                mapper.translateLocalToDb(
//                    translate = it,
//                    wordId = word.id
//                )
//            },
//            hints = word.hints.map { mapper.hintLocalToDb(hint = it, wordId = word.id) }
//        )


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