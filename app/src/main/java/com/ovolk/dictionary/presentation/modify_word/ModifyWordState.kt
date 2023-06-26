package com.ovolk.dictionary.presentation.modify_word

import androidx.compose.runtime.Stable
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.lists.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.modify_word.helpers.RecordAudioHandler
import com.ovolk.dictionary.util.DEFAULT_PRIORITY_VALUE
import kotlinx.coroutines.flow.MutableStateFlow

enum class ModifyWordModes { MODE_ADD, MODE_EDIT }
enum class WordAlreadyExistActions { REPLACE, CLOSE, GO_TO_WORD }


data class AddNewLangModal(
    val isOpen: Boolean = false,
    val type: LanguagesType? = null
)

data class InitialState(
    val composeState: ComposeState = ComposeState(),
    val translateState: Translates = Translates(),
    val hintState: Hints = Hints(),
    val recordAudio: RecordAudioHandler
)


@Stable
data class Translates(
    val translates: List<Translate> = emptyList(),
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


data class ComposeState(
    val word: String = "",
    val englishWordError: ValidateResult = ValidateResult(),
    val transcriptionWord: String = "",
    val descriptionWord: String = "",
    val soundFileName: String? = null,
    val priorityValue: String = DEFAULT_PRIORITY_VALUE.toString(),
    val priorityError: ValidateResult = ValidateResult(),
    @Stable
    val wordLists: List<ModifyWordListItem> = emptyList(),
    val selectedWordList: ModifyWordListItem? = null,
    val isAdditionalFieldVisible: Boolean = false,
    val modalError: SimpleError = SimpleError(
        isError = false,
        text = ""
    ),
    val isOpenSelectModal: Boolean = false,
    val isOpenAddNewListModal: Boolean = false,
    val isOpenUnsavedChanges: Boolean = false,
    val editableWordId: Long? = null,
    val createdAt: Long? = null,
    val modifyMode: ModifyWordModes = ModifyWordModes.MODE_ADD,
    val isOpenDeleteWordModal: Boolean = false,
    val isFieldDescribeModalOpen: Boolean = false,
    val fieldDescribeModalQuestion: String = "",
    val isOpenModalWordAlreadyExist: Boolean = false,
    val dictionaryList: List<Dictionary> = emptyList(),
    val dictionary: MutableStateFlow<Dictionary?> = MutableStateFlow(null),
    val dictionaryError: ValidateResult = ValidateResult(),
)

data class RecordAudioState(
    val isModalOpen: Boolean = false,
    val isRecording: Boolean = false,
    val isTempRecordExist: Boolean = false,
    val isRecordExist: Boolean = false,
    val isRecordPlaying: Boolean = false,
    val existingRecordDuration: Int = 0,
    val isChangesExist: Boolean = false
)

data class LocalState(
    val alreadyExistWordId: Long? = null
)

sealed interface RecordAudioAction {
    object StartRecording : RecordAudioAction
    object StopRecording : RecordAudioAction
    object ListenRecord : RecordAudioAction
    object SaveRecord : RecordAudioAction
    object DeleteRecord : RecordAudioAction
    object HideBottomSheet : RecordAudioAction
    object OpenBottomSheet : RecordAudioAction
}


sealed interface ModifyWordAction {
    object ResetModalError : ModifyWordAction
    data class HandleAddNewListModal(val isOpen: Boolean) : ModifyWordAction
    data class HandleSelectModal(val isOpen: Boolean) : ModifyWordAction
    data class OnSelectDictionary(val dictionaryId: Long) :
        ModifyWordAction

    object PressAddNewDictionary : ModifyWordAction
    data class OnChangeWord(val value: String) : ModifyWordAction
    data class OnChangeEnglishTranscription(val value: String) : ModifyWordAction
    data class OnChangeDescription(val value: String) : ModifyWordAction
    data class OnChangePriority(val value: String) : ModifyWordAction
    data class AddNewList(val title: String) : ModifyWordAction
    data class OnSelectList(val listId: Long) : ModifyWordAction
    object ToggleVisibleAdditionalPart : ModifyWordAction
    object OnPressSaveWord : ModifyWordAction
    object ToggleDeleteModalOpen : ModifyWordAction
    object DeleteWord : ModifyWordAction
    data class GoBack(val withValidateUnsavedChanges: Boolean = true) : ModifyWordAction
    object ToggleUnsavedChanges : ModifyWordAction
    data class ToggleFieldDescribeModalOpen(val question: String) : ModifyWordAction
    data class HandleWordAlreadyExistModal(val action: WordAlreadyExistActions) : ModifyWordAction

}

sealed interface ModifyWordHintsAction {
    object CancelEditHint : ModifyWordHintsAction
    object OnPressAddHint : ModifyWordHintsAction
    data class OnChangeHint(val value: String) : ModifyWordHintsAction
    data class OnPressEditHint(val hint: HintItem) : ModifyWordHintsAction
    data class OnDeleteHint(val hintLocalId: Long) : ModifyWordHintsAction
}

sealed interface ModifyWordTranslatesAction {
    object CancelEditTranslate : ModifyWordTranslatesAction
    object OnPressAddTranslate : ModifyWordTranslatesAction
    data class OnChangeTranslate(val value: String) : ModifyWordTranslatesAction
    data class OnPressEditTranslate(val translate: Translate) : ModifyWordTranslatesAction
    data class OnPressDeleteTranslate(val translateLocalId: Long) : ModifyWordTranslatesAction
    data class OnLongPressTranslate(val translateLocalId: Long) : ModifyWordTranslatesAction
}