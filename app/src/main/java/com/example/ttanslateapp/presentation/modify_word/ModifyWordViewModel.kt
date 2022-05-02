package com.example.ttanslateapp.presentation.modify_word

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttanslateapp.data.model.Sound
import com.example.ttanslateapp.domain.model.AnswerItem
import com.example.ttanslateapp.domain.model.Chip
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import com.example.ttanslateapp.domain.use_case.GetWordItemUseCase
import com.example.ttanslateapp.domain.use_case.GetWordListUseCase
import com.example.ttanslateapp.domain.use_case.ModifyWordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    var successLoadWordById: SuccessLoadWordById? = null

    private fun getTimestamp(): Long = System.currentTimeMillis()

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
        langFrom: String = "",
        langTo: String = ""
    ) {
        val isValid = validateWord(value = value)

        if (!isValid) {
            return
        }

        val word = ModifyWord(
            id = _editableWordId ?: 0L,
            value = value,
            translates = translates.value!!,
            description = description,
            sound = null,
            langFrom = langFrom,
            langTo = langTo,
            hints = _hints.value,
        )

        viewModelScope.launch {
            modifyWordUseCase(word)
        }
    }

    fun getWordById(id: Long) {
        viewModelScope.launch {
            val word = getWordItemUseCase(id)

            _translates.postValue(word.translates)
            _hints.postValue(word.hints)

            _editableWordId = word.id

            successLoadWordById?.onLoaded(word)
            Log.d("ModifyWordViewModel", word.toString())
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


        if (_translates.value == null) {
            _translates.value = listOf(newTranslateItem)
        } else {
            val hintAlreadyExist = _translates.value?.find { it.id == newTranslateItem.id }

            if (hintAlreadyExist == null) {
                _translates.value = _translates.value?.plus(newTranslateItem)
            } else {
                _translates.value = _translates.value?.map {
                    if (it.id == newTranslateItem.id) {
                        return@map newTranslateItem
                    }
                    return@map it
                }
            }

            // clear editableHint, because we finish edit
            setEditableTranslate(null)
        }
    }

    fun deleteTranslate(translateId: String) {
        if (translateId == editableTranslate.value?.id) {
            setEditableTranslate(null)
        }
        _translates.value = _translates.value?.filter { it.id != translateId }
    }

    // Валідацію робити на EditableItem
    fun addHint(hintValue: String) {
        val newHintItem =
            _editableHint.value?.copy(value = hintValue, updatedAt = getTimestamp()) ?: HintItem(
                value = hintValue,
                id = UUID.randomUUID().toString(),
                createdAt = getTimestamp(),
                updatedAt = getTimestamp()
            )

//        modifyChip(newHintItem, _hints, _editableHint)
        if (_hints.value == null) {
            _hints.value = listOf(newHintItem)
        } else {
            val hintAlreadyExist = _hints.value?.find { it.id == newHintItem.id }

            if (hintAlreadyExist == null) {
                _hints.value = _hints.value?.plus(newHintItem)
            } else {
                _hints.value = _hints.value?.map {
                    if (it.id == newHintItem.id) {
                        return@map newHintItem
                    }
                    return@map it
                }
            }

            // clear editableHint, because we finish edit
            setEditableHint(null)
        }
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



    interface SuccessLoadWordById {
        fun onLoaded(word: ModifyWord)
    }
}