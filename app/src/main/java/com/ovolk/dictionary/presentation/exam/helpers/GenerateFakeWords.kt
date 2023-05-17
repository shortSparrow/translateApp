package com.ovolk.dictionary.presentation.exam.helpers

import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.use_case.lists.ListsRepository
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject


// Needed for auto generate fake words (only for testing/debugging)
class GenerateFakeWords @Inject constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
    private val repository: ListsRepository,
) {
    private fun getTimestamp(): Long = System.currentTimeMillis()

    private val wordLists = listOf(
        mapOf(
            "title" to "sport",
            "id" to 1L,
            "wordsCount" to 25,
        ),
        mapOf(
            "title" to "finance",
            "id" to 2L,
            "wordsCount" to 31
        ),
        mapOf(
            "title" to "cooking",
            "id" to 3L,
            "wordsCount" to 22,
        ),
        mapOf(
            "title" to "life",
            "id" to 4L,
            "wordsCount" to 34,
        ),
        mapOf(
            "title" to "animals",
            "id" to 5L,
            "wordsCount" to 22, // + 5 from generateFakeWordsForPlayMarket
        ),
    )

    private suspend fun generateFakeWords(wordListId: Long? = null, max: Int = 10) {
        var wordCount = 1
        while (wordCount <= max) {
            val word = ModifyWord(
                value = wordCount.toString(),
                translates = listOf(
                    Translate(
                        localId = 1,
                        updatedAt = getTimestamp(),
                        createdAt = getTimestamp(),
                        value = wordCount.toString(),
                        isHidden = false,
                    )
                ),
                description = "",
                sound = null,
                langFrom = "EN",
                langTo = "UK",
                hints = listOf(
                    HintItem(
                        localId = 1,
                        updatedAt = getTimestamp(),
                        createdAt = getTimestamp(),
                        value = "translate_$wordCount",
                    )
                ),
                createdAt = getTimestamp(),
                updatedAt = getTimestamp(),
                transcription = "",
                priority = 0,
                wordListId = wordListId
            )
            withContext(Dispatchers.Default) {
                modifyWordUseCase(word = word)
            }
            delay(DELAY)
            wordCount++
        }
    }

    suspend fun generateFakeWordListWithWords() {
        wordLists.forEachIndexed { i, element ->
            val id = element["id"] as Long
            val title = element["title"] as String
            val wordsCount = element["wordsCount"] as Int

            withContext(Dispatchers.Default) {
                repository.addNewList(
                    ListItem(
                        id = id,
                        title = title,
                        count = 0,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            delay(DELAY)

            withContext(Dispatchers.Default) {
                generateFakeWords(id, wordsCount)
            }
        }
    }

    suspend fun generateFakeWordsForPlayMarket() {
        val animalsListId =
            wordLists.find { (it["title"] as String) == "animals" }?.get("id") as Long
        val words = listOf(
            mapOf(
                "value" to "estimate",
                "translates" to listOf("оцінка"),
                "description" to "ddd",
                "sound" to WordAudio("xxx"),
                "hints" to listOf<String>("що треба зробити з здачаєю перед початком її вирішення?"),
            ),
            mapOf(
                "value" to "driver",
                "translates" to listOf("водій"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>("Чи не хочете проїхатися на таксі"),
            ),
            mapOf(
                "value" to "proud",
                "translates" to listOf("гордий", "величний"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>(),
            ),
            mapOf(
                "value" to "arise",
                "translates" to listOf("зʼявлятися", "виникати"),
                "description" to "",
                "sound" to WordAudio("xxx"),
                "hints" to listOf<String>(),
            ),
            mapOf(
                "value" to "blue",
                "translates" to listOf("синій"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>(),
            ),
            mapOf(
                "value" to "turtle",
                "translates" to listOf("черепашка"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>("Довго живе", "Має панцир"),
                "wordListId" to animalsListId,
            ),
            mapOf(
                "value" to "parrot",
                "translates" to listOf("папуга"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>(),
                "wordListId" to animalsListId,
            ),
            mapOf(
                "value" to "horse",
                "translates" to listOf("кінь"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>(),
                "wordListId" to animalsListId,
            ),
            mapOf(
                "value" to "frog",
                "translates" to listOf("жабка"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>(),
                "wordListId" to animalsListId,
            ),
            mapOf(
                "value" to "squirrel",
                "translates" to listOf("білочка"),
                "description" to "",
                "sound" to null,
                "hints" to listOf<String>(),
                "wordListId" to animalsListId,
            ),
        ).reversed()

        words.forEach {
            val wordListId = it["wordListId"] as Long?

            val word = ModifyWord(
                value = it["value"].toString(),
                translates = (it["translates"] as List<String>).mapIndexed { i, translate ->
                    Translate(
                        localId = i.toLong(),
                        updatedAt = getTimestamp(),
                        createdAt = getTimestamp(),
                        value = translate,
                        isHidden = false,
                    )
                }.toList(),
                description = it["description"] as String,
                sound = it["sound"] as WordAudio?,
                langFrom = "EN",
                langTo = "UK",
                hints = (it["hints"] as List<String>).mapIndexed { i, translate ->
                    HintItem(
                        localId = i.toLong(),
                        updatedAt = getTimestamp(),
                        createdAt = getTimestamp(),
                        value = translate,
                    )
                }.toList(),
                createdAt = getTimestamp(),
                updatedAt = getTimestamp(),
                transcription = (it["transcription"] ?: "") as String,
                priority = 0,
                wordListId = wordListId

            )
            withContext(Dispatchers.Default) {
                modifyWordUseCase(word = word)
            }
            delay(DELAY)
        }
    }

    companion object {
        const val DELAY = 10L
    }
}
