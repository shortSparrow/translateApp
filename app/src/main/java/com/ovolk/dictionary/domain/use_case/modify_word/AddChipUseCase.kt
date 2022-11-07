package com.ovolk.dictionary.domain.use_case.modify_word

import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.modify_word.Hints
import com.ovolk.dictionary.presentation.modify_word.Translates
import com.ovolk.dictionary.presentation.modify_word.helpers.validateOnAddChip
import javax.inject.Inject


//sealed interface AddChipReturn {
//    data class Success(val data: List<Translate>) : AddChipReturn
//    class Error(val data: ValidateResult) : AddChipReturn
//}

class AddChipUseCase @Inject constructor() {
    private fun getTimestamp(): Long = System.currentTimeMillis()

    fun addTranslate(
        translateValue: String,
        translateState: Translates
    ): Translates {
        val wordValidation = validateOnAddChip(translateValue)
        val hasError = !wordValidation.successful

        if (hasError) {
            return translateState.copy(
                error = wordValidation,
            )
        }

        val translates = translateState.translates
        val editableTranslate = translateState.editableTranslate

        val newTranslateItem =
            editableTranslate?.copy(value = translateValue, updatedAt = getTimestamp())
                ?: Translate(
                    localId = getTimestamp(),
                    value = translateValue,
                    createdAt = getTimestamp(),
                    updatedAt = getTimestamp(),
                    isHidden = false
                )

        val hintAlreadyExist =
            translates.find { it.localId == newTranslateItem.localId }

        val newList = if (hintAlreadyExist == null) {
            translates.plus(newTranslateItem)
        } else {
            translates.map {
                if (it.localId == newTranslateItem.localId) {
                    return@map newTranslateItem
                }
                return@map it
            }
        }
        return translateState.copy(
            translates = newList,
            editableTranslate = null,
            translationWord = "",
            error = ValidateResult()
        )
    }


    fun addHint(
        hintValue: String,
        hintState: Hints
    ): Hints {
        val wordValidation = validateOnAddChip(hintValue)
        val hasError = !wordValidation.successful

        if (hasError) {
            return hintState.copy(
                error = wordValidation,
            )
        }


        val hintList = hintState.hints
        val editableHint = hintState.editableHint

        val newHintItem =
            editableHint?.copy(value = hintValue, updatedAt = getTimestamp())
                ?: HintItem(
                    localId = getTimestamp(),
                    value = hintValue,
                    createdAt = getTimestamp(),
                    updatedAt = getTimestamp(),
                )

        val hintAlreadyExist = hintList.find { it.localId == newHintItem.localId }

        val newList = if (hintAlreadyExist == null) {
            hintList.plus(newHintItem)
        } else {
            hintList.map {
                if (it.localId == newHintItem.localId) {
                    return@map newHintItem
                }
                return@map it
            }
        }

        return hintState.copy(
            hints = newList,
            editableHint = null,
            hintWord = "",
            error = ValidateResult()
        )
    }
}