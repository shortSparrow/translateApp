package com.ovolk.dictionary.presentation.modify_word.view_model

import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.use_case.modify_word.AddChipUseCase
import com.ovolk.dictionary.presentation.modify_word.Hints
import com.ovolk.dictionary.presentation.modify_word.ModifyWordHintsAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


open class WordHintsPart @Inject constructor(
    private val addChipUseCase: AddChipUseCase,
) {
    var hintState = MutableStateFlow(Hints())

    fun onAction(action: ModifyWordHintsAction) {
        when (action) {
            is ModifyWordHintsAction.OnChangeHint -> {
                hintState.update {
                    it.copy(hintWord = action.value, error = ValidateResult())
                }
            }

            is ModifyWordHintsAction.OnDeleteHint -> {
                hintState.update { it.copy(hints = it.hints.filter { hintsItem -> hintsItem.localId != action.hintLocalId }) }
            }

            is ModifyWordHintsAction.OnPressEditHint -> {
                hintState.update {
                    it.copy(
                        editableHint = action.hint,
                        hintWord = action.hint.value,
                        error = ValidateResult()
                    )
                }
            }

            ModifyWordHintsAction.OnPressAddHint -> {
                hintState.value = addChipUseCase.addHint(
                    hintValue = hintState.value.hintWord.trim(),
                    hintState = hintState.value
                )
            }

            ModifyWordHintsAction.CancelEditHint -> {
                hintState.update { it.copy(editableHint = null, hintWord = "") }
            }
        }
    }
}
