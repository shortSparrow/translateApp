package com.ovolk.dictionary.presentation.exam.helpers

import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// Needed for auto generate fake words (only for testing/debugging)
class GenerateFakeWords constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
) {
    private fun getTimestamp(): Long = System.currentTimeMillis()
    val scope = CoroutineScope(Dispatchers.IO)

    fun generateFakeWords() {
        scope.launch {
            var wordCount = 1
            while (wordCount <= 10) {
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
                    dictionary = Dictionary(
                        id = 1L,
                        langFromCode = "EN",
                        langToCode = "UA",
                        title = "EN-UA",
                        isSelected = false,
                        isActive = true,
                    )
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
