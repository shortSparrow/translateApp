package com.ovolk.dictionary.presentation.modify_word

import android.view.View
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType

sealed interface ModifyWordUiState {
    data class IsWordLoading(
        val isLoading: Boolean
    ) : ModifyWordUiState

    data class EditFieldError(
        val wordValueError: String? = null,
        val translatesError: String? = null,
        val priorityValidation: String? = null,

        ) : ModifyWordUiState

    data class PreScreen(
        val wordValue: String,
        val transcription: String,
        val priority: Int,
        val description: String,
        val translates: List<Translate>,
        val editableTranslate: Translate? = null,
        val hints: List<HintItem>,
        val editableHint: HintItem? = null,
        val langFrom: String?,
        val soundFileName: String?,
        val isAdditionalFieldVisible: Int,
        val wordValueError: String? = null,
        val translatesError: String? = null,
        val screenIsRestored: Boolean? = false,
        val isDeleteModalOpen: Boolean = false,
    ) : ModifyWordUiState

    data class ShowAdditionalFields(val isVisible: Int) : ModifyWordUiState
    data class ShowResultModify(val isSuccess: Boolean) : ModifyWordUiState

    data class StartModifyTranslate(val value: String) : ModifyWordUiState
    data class CompleteModifyTranslate(val translates: List<Translate>) : ModifyWordUiState

    data class StartModifyHint(val value: String) : ModifyWordUiState
    data class CompleteModifyHint(val hints: List<HintItem>) : ModifyWordUiState

    data class DeleteTranslates(val translates: List<Translate>) : ModifyWordUiState
    data class DeleteHints(val hints: List<HintItem>) : ModifyWordUiState

    data class UpdateSoundFile(val name: String?) : ModifyWordUiState
    data class ToggleOpenedDeleteModel(val isOpened: Boolean) : ModifyWordUiState
}


data class ModifyWordState(
    val wordValue: String = "",
    val transcription: String = "",
    val description: String = "",
    val translates: List<Translate> = emptyList(),
    val hints: List<HintItem> = emptyList(),
    val editableHint: HintItem? = null,
    val editableTranslate: Translate? = null,
    val langFrom: String? = null,
    val langTo: String? = null,
    val selectableLanguage: String? = null,
    val soundFileName: String? = null,
    val priority: Int = ModifyWord.DEFAULT_PRIORITY,
    val translatesError: String? = null,
    val wordValueError: String? = null,
    val priorityValidation: String? = null,
    val isAdditionalFieldVisible: Int = View.GONE,
    val savedWordResult: Boolean = false,
    val editableWordId: Long? = null,
    val createdAt: Long? = null,
    val isDeleteModalOpen: Boolean = false,
)

data class ComposeState(
    val wordListInfo: ModifyWordListItem? = null,
    val wordLists: List<ModifyWordListItem> = emptyList(),
    val modalError: SimpleError = SimpleError(
        isError = false,
        text = ""
    ),
    val isOpenSelectModal: Boolean = false,
    val isOpenAddNewListModal: Boolean = false,

    val languageFromList: List<SelectLanguage> = emptyList(),
    val languageToList: List<SelectLanguage> = emptyList(),

    val selectLanguageFromError: ValidateResult = ValidateResult(),
    val selectLanguageToError: ValidateResult = ValidateResult(),
)

sealed class ModifyWordAction {
    object ResetModalError : ModifyWordAction()
    data class HandleAddNewListModal(val isOpen: Boolean) : ModifyWordAction()
    data class HandleSelectModal(val isOpen: Boolean) : ModifyWordAction()
    data class OnSelectLanguage(val type: LanguagesType, val language: SelectLanguage) :
        ModifyWordAction()
}
