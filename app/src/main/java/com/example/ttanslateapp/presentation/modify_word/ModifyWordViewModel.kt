package com.example.ttanslateapp.presentation.modify_word

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.use_case.GetWordItemUseCase
import com.example.ttanslateapp.domain.use_case.ModifyWordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ModifyWordViewModel @Inject constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
    private val getWordItemUseCase: GetWordItemUseCase
) : ViewModel() {

    private val _word = MutableLiveData<ModifyWord>()
    val word: LiveData<ModifyWord> = _word

    private val _isAdditionalFieldVisible = MutableLiveData(false)
    val isAdditionalFieldVisible: LiveData<Boolean> = _isAdditionalFieldVisible


    private val scope = CoroutineScope(Dispatchers.IO)

    fun saveWord(word: String, description: String) {
        scope.launch {
            modifyWordUseCase(word, description)
        }
    }

    fun getWordById(id: Long) {
        scope.launch {
            val word = getWordItemUseCase(id)
            Log.d("ModifyWordViewModel", word.toString())
        }
    }

    /**
     * Ми маємо created_at, updated_at, id
     * Як найкраще їх оброблювати, вразовуючи що буде локальна БД + сервер
     *
     * У мене на клік буде відриватися меню, де будуть певні дію.
     * 1. Чи варто робити це список через RV + враховувати що є menu
     * 2. Як найкраще передати, який саме chip я клікнув, чи add_translate чи add_hint
     * 3. Як перевикористати логіку для обох випадків, оскільки структура однакова.
     * Abstract Class якось не працює, тай треба буде пов'язати з LiveData
     *
     * Чи може створти баьківський клас для add_translate чи add_hint, але вони ж data class.
     * Чи може просто сворити універсальний клас chips, але що робити, коли в якомусь з них з'явиться додаткве
     * поле
     *
     * Якщо будемо виносити логіку в окрмений клас, то я обробити обидва класа, поліморфізм чи
     * краще окремі функції
     *
     *
     * //////////
     * add scroll for every EditText inside scrollView & description scroll work not correct (velocity)
     */

    // FIXME translate chips and hint has the same structures and click logic
    fun addTranslate(translateItem: String) {
        // TODO add created_at & updated_at & correct id
//        val newTranslateItem = TranslateWordItem(value = translateItem, id = -1)
//        val oldTranslations = _word.value?.translateWords
//
//        if (oldTranslations != null) {
//            _word.value = _word.value?.copy(
//                translateWords = oldTranslations + newTranslateItem
//            )
//        } else {
//            error("translations list is null")
//        }

    }

    fun editTranslate(translateItemId: Long, newValue: String) {
        var isItemExist = _word.value?.translateWords?.find {
            it.id == translateItemId
        }
        if (isItemExist != null) {
            // TODO add created_at & updated_at
            isItemExist.copy(value = newValue).also { isItemExist = it }
        } else {
            // TODO
        }
    }


    // Валідацію робити на EditableItem
    fun addHint(HintItem: String) {
        // TODO
    }

    fun toggleVisibleAdditionalField() {
        val oldValue = _isAdditionalFieldVisible.value!!
        _isAdditionalFieldVisible.value = !oldValue
    }
}