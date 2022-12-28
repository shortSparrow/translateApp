package com.ovolk.dictionary.presentation.exam.helpers

import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import kotlinx.coroutines.*
import javax.inject.Inject


// Needed for auto generate fake words (only for testing/debugging)
class GenerateFakeWords constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
) {
    private fun getTimestamp(): Long = System.currentTimeMillis()
    val scope = CoroutineScope(Dispatchers.IO)

    fun generateFakeWords() {
        scope.launch {
            var wordCount = 1
            while (wordCount <= 30) {
                val word = ModifyWord(
                    value = wordCount.toString(),
                    translates = listOf(
                        Translate(
                            localId = 1,
                            updatedAt = getTimestamp(),
                            createdAt = getTimestamp(),
                            value = "translate_$wordCount",
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
                    transcription = ""
                )
                val d = this.async {
                    modifyWordUseCase(word = word)
                }
                d.await()
                delay(1)
                wordCount++
            }
        }
    }
}