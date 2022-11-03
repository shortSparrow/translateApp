package com.ovolk.dictionary.presentation.modify_word

import android.view.View
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.model.modify_word.ModifyWord
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType

sealed interface ModifyWordUiState {
    data class IsWordLoading(
        val isLoading: Boolean
    ) : ModifyWordUiState

    data class EditFieldError(
        val wordValueError: String? = null,
        val translatesError: String? = null,
        val priorityValidation: String? = null,
        val hintWordError: String? = null,
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
        val hintWordError: String? = null,
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
    val hintWordError: String? = null,
    val priorityValidation: String? = null,
    val isAdditionalFieldVisible: Int = View.GONE,

    val savedWordResult: Boolean = false,
    val editableWordId: Long? = null,
    val createdAt: Long? = null,
    val isDeleteModalOpen: Boolean = false,
    val modifyMode: ModifyWordModes = ModifyWordModes.MODE_ADD
)

data class AddNewLangModal(
    val isOpen: Boolean = false,
    val type: LanguagesType? = null
)


data class Translates(
    val translates: List<Translate> = emptyList(),
    val translationWord: String = "",
    val error: ValidateResult = ValidateResult(),
    val editableTranslate: Translate? = null,
)

data class Hints(
    val hints: List<HintItem> = emptyList(),
    val hintWord: String = "",
    val error: ValidateResult = ValidateResult(),
    val editableHint: HintItem? = null
)

data class Languages(
    val languageFromList: List<SelectLanguage> = emptyList(),
    val languageToList: List<SelectLanguage> = emptyList(),
    val selectLanguageFromError: ValidateResult = ValidateResult(),
    val selectLanguageToError: ValidateResult = ValidateResult(),
    val addNewLangModal: AddNewLangModal = AddNewLangModal(),
)
//data class WordLists(
//    val wordListInfo: ModifyWordListItem? = null,
//    val lists: List<ModifyWordListItem> = emptyList(),
//)

//data class ComposeState(
//    val languageFromList: List<SelectLanguage> = emptyList(),
//    val languageToList: List<SelectLanguage> = emptyList(),
//    val selectLanguageFromError: ValidateResult = ValidateResult(),
//    val selectLanguageToError: ValidateResult = ValidateResult(),
//    val addNewLangModal: AddNewLangModal = AddNewLangModal(),
//
//    val englishWord: EnglishWord = EnglishWord(),
//    val transcriptionWord: String = "",
//
//    val translates: List<Translate> = emptyList(),
//    val translationWord: String = "",
//    val translateError: SimpleError = SimpleError(),
//    val editableTranslate: Translate? = null,
//
//    val descriptionWord: String = "",
//    // TODO here audio
//    val priority: Priority = Priority(),
//
//    val hints: List<HintItem> = emptyList(),
//    val hintWord: String = "",
//
//    val wordListInfo: ModifyWordListItem? = null,
//    val wordLists: List<ModifyWordListItem> = emptyList(),
//
//    val editableHint: HintItem? = null,
//
//    val isAdditionalFieldVisible: Boolean = false,
//    val modalError: SimpleError = SimpleError(
//        isError = false,
//        text = ""
//    ),
//    val isOpenSelectModal: Boolean = false,
//    val isOpenAddNewListModal: Boolean = false,
//    val editableWordId: Long? = null,
//    val createdAt: Long? = null, // TODO подумати, може при завантаженні слова записати його кудись і потім звертатися
//    val isDeleteModalOpen: Boolean = false,
//    val modifyMode: ModifyWordModes = ModifyWordModes.MODE_ADD
//)

data class ComposeState(
    val englishWord: String = "",
    val englishWordError: ValidateResult = ValidateResult(),
    val transcriptionWord: String = "",
    val descriptionWord: String = "",
    // TODO here audio
    val priorityValue: String = "5", // TODO add as constant
    val priorityError: ValidateResult = ValidateResult(),

    val wordListInfo: ModifyWordListItem? = null,
    val wordLists: List<ModifyWordListItem> = emptyList(),
    val isAdditionalFieldVisible: Boolean = false,
    val modalError: SimpleError = SimpleError(
        isError = false,
        text = ""
    ),
    val isOpenSelectModal: Boolean = false,
    val isOpenAddNewListModal: Boolean = false,
    val editableWordId: Long? = null,
    val createdAt: Long? = null, // TODO подумати, може при завантаженні слова записати його кудись і потім звертатися
    val isDeleteModalOpen: Boolean = false,
    val modifyMode: ModifyWordModes = ModifyWordModes.MODE_ADD
)

sealed class ModifyWordAction {
    object ResetModalError : ModifyWordAction()
    data class HandleAddNewListModal(val isOpen: Boolean) : ModifyWordAction()
    data class HandleSelectModal(val isOpen: Boolean) : ModifyWordAction()
    data class OnSelectLanguage(val type: LanguagesType, val language: SelectLanguage) :
        ModifyWordAction()

    data class PressAddNewLanguage(val type: LanguagesType) : ModifyWordAction()
    object CloseAddNewLanguageModal : ModifyWordAction()

    data class OnChangeEnglishWord(val value: String) : ModifyWordAction()
    data class OnChangeEnglishTranscription(val value: String) : ModifyWordAction()

    data class OnChangeTranslate(val value: String) : ModifyWordAction()
    object OnPressAddTranslate : ModifyWordAction()
    data class OnPressEditTranslate(val translate: Translate) : ModifyWordAction()
    data class OnPressDeleteTranslate(val translateLocalId: Long) : ModifyWordAction()
    data class OnLongPressTranslate(val translateLocalId: Long) : ModifyWordAction()
    object CancelEditTranslate : ModifyWordAction()

    data class OnChangeDescription(val value: String) : ModifyWordAction()
    data class OnChangePriority(val value: String) : ModifyWordAction()

    data class AddNewList(val title: String) : ModifyWordAction()
    data class OnSelectList(val listId: Long) : ModifyWordAction()


    data class OnChangeHint(val value: String) : ModifyWordAction()
    object OnPressAddHint : ModifyWordAction()
    data class OnPressEditHint(val hint: HintItem) : ModifyWordAction()
    object CancelEditHint: ModifyWordAction()
    data class OnDeleteHint(val hintLocalId: Long): ModifyWordAction()

    object ToggleVisibleAdditionalPart: ModifyWordAction()

    object OnPressSaveWord: ModifyWordAction()
}
