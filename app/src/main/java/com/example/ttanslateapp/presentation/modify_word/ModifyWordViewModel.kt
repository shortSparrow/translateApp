package com.example.ttanslateapp.presentation.modify_word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordAudio
import com.example.ttanslateapp.domain.model.modify_word_chip.Chip
import com.example.ttanslateapp.domain.model.modify_word_chip.HintItem
import com.example.ttanslateapp.domain.model.modify_word_chip.TranslateWordItem
import com.example.ttanslateapp.domain.use_case.GetWordItemUseCase
import com.example.ttanslateapp.domain.use_case.ModifyWordUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ModifyWordViewModel @Inject constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
    private val getWordItemUseCase: GetWordItemUseCase,
) : ViewModel() {

    private var _editableWordId: Long? = null

    private val _wordValueError = MutableLiveData<Boolean>()
    val wordValueError: LiveData<Boolean> = _wordValueError

    private val _translates = MutableLiveData<List<TranslateWordItem>>()
    val translates: LiveData<List<TranslateWordItem>> = _translates

    private val _translatesError = MutableLiveData<Boolean>()
    val translatesError: LiveData<Boolean> = _translatesError

    private val _hints = MutableLiveData<List<HintItem>>()
    val hints: LiveData<List<HintItem>> = _hints

    private val _editableHint = MutableLiveData<HintItem?>()
    val editableHint: LiveData<HintItem?> = _editableHint

    private val _editableTranslate = MutableLiveData<TranslateWordItem?>()
    val editableTranslate: LiveData<TranslateWordItem?> = _editableTranslate

    private val _isAdditionalFieldVisible = MutableLiveData(false)
    val isAdditionalFieldVisible: LiveData<Boolean> = _isAdditionalFieldVisible

    private val _savedWordResult = MutableLiveData<Boolean>()
    val savedWordResult: LiveData<Boolean> = _savedWordResult

    var soundFileName: String? = null

    var successLoadWordById: SuccessLoadWordById? = null

    private fun getTimestamp(): Long = System.currentTimeMillis()

    init {
        _translates.value = listOf()
        _hints.value = emptyList()
    }

    fun setWordValueError(value: Boolean) {
        _wordValueError.value = value
    }

    fun setTranslatesError(value: Boolean) {
        _translatesError.value = value
    }

    private fun validateWord(value: String): Boolean {
        var isValid = true

        if (value.trim().isEmpty()) {
            _wordValueError.value = true
            isValid = false
        }

        if (_translates.value?.size == 0 || _translates.value == null) {
            _translatesError.value = true
            isValid = false
        }

        return isValid
    }

    override fun onCleared() {
        _editableWordId = null
    }

    fun saveWord(
        value: String = "",
        description: String = "",
        transcription: String = "",
        langFrom: String = "",
        langTo: String = "",
    ) {
        val isValid = validateWord(value = value)

        if (!isValid) {
            return
        }

        val sound = soundFileName?.let { WordAudio(it) }
        Timber.d("SOUND is $sound")

        val word = ModifyWord(
            id = _editableWordId ?: 0L,
            priority = ModifyWord.DEFAULT_PRIORITY,
            value = value,
            translates = translates.value ?: emptyList(),
            description = description,
            sound = sound,
            langFrom = langFrom,
            langTo = langTo,
            hints = _hints.value ?: emptyList(),
            transcription = transcription
        )

        viewModelScope.launch {
            modifyWordUseCase(word).apply {
                _savedWordResult.value = this
            }
        }
    }

    fun getWordById(id: Long) {
        viewModelScope.launch {
            val word = getWordItemUseCase(id)
            Timber.d("getWordItemUseCase $word")
            _translates.postValue(word.translates)
            _hints.postValue(word.hints)
            _editableWordId = word.id
            successLoadWordById?.onLoaded(word)
            soundFileName = word.sound?.fileName
        }
    }

    fun addTranslate(translateValue: String) {
        if (translateValue.trim().isEmpty()) return

        val newTranslateItem =
            _editableTranslate.value?.copy(value = translateValue, updatedAt = getTimestamp())
                ?: TranslateWordItem(
                    value = translateValue,
                    id = UUID.randomUUID().toString(),
                    createdAt = getTimestamp(),
                    updatedAt = getTimestamp()
                )

        addChip<LiveData<List<TranslateWordItem>>>(_translates, newTranslateItem)

        // clear editableHint, because we finish edit
        setEditableTranslate(null)
    }

    fun addHint(hintValue: String) {
        if (hintValue.trim().isEmpty()) return

        val newHintItem =
            _editableHint.value?.copy(value = hintValue, updatedAt = getTimestamp()) ?: HintItem(
                value = hintValue,
                id = UUID.randomUUID().toString(),
                createdAt = getTimestamp(),
                updatedAt = getTimestamp()
            )

        addChip<LiveData<List<HintItem>>>(_hints, newHintItem)

        // clear editableHint, because we finish edit
        setEditableHint(null)
    }

    private fun <T> addChip(chips: T, newChipItem: Chip) {
        val chipList = (chips as MutableLiveData<List<Chip>>)
        val hintAlreadyExist =
            chipList.value?.find { it.id == newChipItem.id }

        if (hintAlreadyExist == null) {
            chipList.value = chipList.value?.plus(newChipItem)
        } else {
            chipList.value = chipList.value?.map {
                if (it.id == newChipItem.id) {
                    return@map newChipItem
                }
                return@map it
            }
        }
    }

    fun deleteTranslate(translateId: String) {
        if (translateId == editableTranslate.value?.id) {
            setEditableTranslate(null)
        }
        _translates.value = _translates.value?.filter { it.id != translateId }
    }

    fun deleteHint(hintId: String) {
        if (hintId == editableHint.value?.id) {
            setEditableHint(null)
        }
        _hints.value = _hints.value?.filter { it.id != hintId }
    }

    fun setEditableHint(editableHint: HintItem?) {
        _editableHint.value = editableHint
    }

    fun setEditableTranslate(editableTranslateWordItem: TranslateWordItem?) {
        _editableTranslate.value = editableTranslateWordItem
    }


    fun toggleVisibleAdditionalField() {
        val oldValue = _isAdditionalFieldVisible.value!!
        _isAdditionalFieldVisible.value = !oldValue
    }

    fun saveAudio(fileName: String?) {
        viewModelScope.launch {
            _editableWordId?.let {
                val sound = if (fileName == null) null else WordAudio(fileName = fileName)
                modifyWordUseCase.modifyOnlySound(it, sound = sound)
            }
        }
    }

    interface SuccessLoadWordById {
        fun onLoaded(word: ModifyWord)
    }
}