package com.ovolk.dictionary.presentation.modify_word.view_model

import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.use_case.modify_word.AddChipUseCase
import com.ovolk.dictionary.presentation.modify_word.ModifyWordTranslatesAction
import com.ovolk.dictionary.presentation.modify_word.Translates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


open class WordTranslatesPart @Inject constructor(
    private val addChipUseCase: AddChipUseCase,
) {
    val translateState = MutableStateFlow(Translates())

    fun onAction(action: ModifyWordTranslatesAction) {
        when (action) {
            is ModifyWordTranslatesAction.OnChangeTranslate -> {
                translateState.update {
                    it.copy(
                        translationWord = action.value,
                        error = ValidateResult()
                    )
                }

            }

            is ModifyWordTranslatesAction.OnLongPressTranslate -> {
                translateState.update {
                    it.copy(
                        translates = it.translates.map {
                            if (it.localId == action.translateLocalId) return@map it.copy(isHidden = !it.isHidden)
                            return@map it
                        },
                    )
                }
            }

            is ModifyWordTranslatesAction.OnPressDeleteTranslate -> {
                translateState.update {
                    it.copy(translates = it.translates.filter { it.localId != action.translateLocalId })
                }
            }

            is ModifyWordTranslatesAction.OnPressEditTranslate -> {
                translateState.update {
                    it.copy(
                        editableTranslate = action.translate,
                        translationWord = action.translate.value,
                        error = ValidateResult()
                    )
                }
            }

            ModifyWordTranslatesAction.OnPressAddTranslate -> {
                translateState.value = addChipUseCase.addTranslate(
                    translateValue = translateState.value.translationWord.trim(),
                    translateState = translateState.value
                )
            }

            ModifyWordTranslatesAction.CancelEditTranslate -> {
                translateState.value =
                    translateState.value.copy(editableTranslate = null, translationWord = "")
            }
        }
    }
}
