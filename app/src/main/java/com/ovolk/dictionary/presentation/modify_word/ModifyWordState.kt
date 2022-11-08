package com.ovolk.dictionary.presentation.modify_word

import androidx.compose.runtime.Stable
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.util.DEFAULT_PRIORITY_VALUE


data class AddNewLangModal(
    val isOpen: Boolean = false,
    val type: LanguagesType? = null
)

@Stable
data class Translates(
    val translates:  List<Translate> = emptyList(),
    val translationWord: String = "",
    val error: ValidateResult = ValidateResult(),
    val editableTranslate: Translate? = null,
)

@Stable
data class Hints(
    val hints: List<HintItem> = emptyList(),
    val hintWord: String = "",
    val error: ValidateResult = ValidateResult(),
    val editableHint: HintItem? = null
)

@Stable
data class Languages(
    val languageFromList: List<SelectLanguage> = emptyList(),
    val languageToList: List<SelectLanguage> = emptyList(),
    val selectLanguageFromError: ValidateResult = ValidateResult(),
    val selectLanguageToError: ValidateResult = ValidateResult(),
    val addNewLangModal: AddNewLangModal = AddNewLangModal(),
)


data class ComposeState(
    val englishWord: String = "",
    val englishWordError: ValidateResult = ValidateResult(),
    val transcriptionWord: String = "",
    val descriptionWord: String = "",
    // TODO here was audio
    val priorityValue: String = DEFAULT_PRIORITY_VALUE.toString(),
    val priorityError: ValidateResult = ValidateResult(),

    val wordListInfo: ModifyWordListItem? = null,
    @Stable
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
    val modifyMode: ModifyWordModes = ModifyWordModes.MODE_ADD,
    val isOpenDeleteWordModal: Boolean = false,
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
    data class OnChangeDescription(val value: String) : ModifyWordAction()
    data class OnChangePriority(val value: String) : ModifyWordAction()
    data class AddNewList(val title: String) : ModifyWordAction()
    data class OnSelectList(val listId: Long) : ModifyWordAction()
    object ToggleVisibleAdditionalPart: ModifyWordAction()
    object OnPressSaveWord: ModifyWordAction()
    object ToggleDeleteModalOpen: ModifyWordAction()
    object DeleteWord: ModifyWordAction()
}

sealed class ModifyWordHintsAction {
    object CancelEditHint: ModifyWordHintsAction()
    object OnPressAddHint : ModifyWordHintsAction()
    data class OnChangeHint(val value: String) : ModifyWordHintsAction()
    data class OnPressEditHint(val hint: HintItem) : ModifyWordHintsAction()
    data class OnDeleteHint(val hintLocalId: Long): ModifyWordHintsAction()
}

sealed class ModifyWordTranslatesAction {
    object CancelEditTranslate : ModifyWordTranslatesAction()
    object OnPressAddTranslate : ModifyWordTranslatesAction()
    data class OnChangeTranslate(val value: String) : ModifyWordTranslatesAction()
    data class OnPressEditTranslate(val translate: Translate) : ModifyWordTranslatesAction()
    data class OnPressDeleteTranslate(val translateLocalId: Long) : ModifyWordTranslatesAction()
    data class OnLongPressTranslate(val translateLocalId: Long) : ModifyWordTranslatesAction()
}