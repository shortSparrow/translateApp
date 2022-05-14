package com.example.ttanslateapp.presentation.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ttanslateapp.domain.model.exam.ExamWord
import com.example.ttanslateapp.domain.model.exam.ExamWordStatus
import com.example.ttanslateapp.domain.model.modify_word_chip.HintItem
import com.example.ttanslateapp.domain.model.modify_word_chip.TranslateWordItem
import com.example.ttanslateapp.presentation.exam.AnswerResult.*
import timber.log.Timber
import javax.inject.Inject

enum class AnswerResult {
    SUCCESS, FAILED, EMPTY
}

class ExamKnowledgeWordsViewModel @Inject constructor() : ViewModel() {
    private val _examWordList = MutableLiveData<List<ExamWord>>()
    val examWordList: LiveData<List<ExamWord>> = _examWordList

    private val _currentWord = MutableLiveData<ExamWord>()
    val currentWord: LiveData<ExamWord> = _currentWord

    private val _answerResult = MutableLiveData(EMPTY)
    val answerResult: LiveData<AnswerResult> = _answerResult

    private val _isExamEnd = MutableLiveData(false)
    val isExamEnd: LiveData<Boolean> = _isExamEnd

    private val _countShownHints = MutableLiveData(1)
    val countShownHints: LiveData<Int> = _countShownHints

    private val _allHintsShown = MutableLiveData(false)
    val allHintsShown: LiveData<Boolean> = _allHintsShown

    private val _isShownHintsVisible = MutableLiveData(false)
    val isShownHintsVisible: LiveData<Boolean> = _isShownHintsVisible

    private val _isShownVariants = MutableLiveData(false)
    val isShownVariants: LiveData<Boolean> = _isShownVariants

    private val _isInoutWordInvalid = MutableLiveData(false)
    val isInoutWordInvalid: LiveData<Boolean> = _isInoutWordInvalid

    var activeWordPosition = 0

    fun toggleVisibleHint() {
        _isShownHintsVisible.value = !_isShownHintsVisible.value!!
    }

    fun toggleVisibleVariants() {
        _isShownVariants.value = !_isShownVariants.value!!
    }

    fun showNextHint() {
        val newCount = _countShownHints.value!!.plus(1)

        if (newCount == _currentWord.value!!.hints.size) {
            _allHintsShown.value = true
        }

        if (newCount > _currentWord.value!!.hints.size) {
            return
        }

        _countShownHints.value = newCount
    }

    // TODO temporary
    fun generateWordsList() {
        _examWordList.value = generateList()
        _currentWord.value = generateList()[0]
    }

    private fun validateAnswer(answer: String): Boolean {
        return answer.trim().isNotEmpty()
    }

    fun checkAnswer(answer: String, cb: () -> Unit) {
        val answerQuery = answer.trim().lowercase()
        val isValid = validateAnswer(answerQuery)
        if (!isValid) {
            _isInoutWordInvalid.value = true
            return
        }

        // invoke callback if validate is success
        cb()

        val answerIsCorrect = _currentWord.value!!.translates.find { translate ->
            translate.value.lowercase() == answerQuery
        }

        if (answerIsCorrect != null) {
            _answerResult.value = SUCCESS
        } else {
            _answerResult.value = FAILED
        }

        updatePositionColors()
        if (_examWordList.value?.last()!!.id == _currentWord.value!!.id) {
            _isExamEnd.value = true
        }
    }


    private fun updatePositionColors() {
        val isAnswerCorrect = getIsAnswerCorrect()

        val newList = _examWordList.value?.mapIndexed { index, it ->
            var newPriority = if (isAnswerCorrect) it.priority - 1 else it.priority + 1
            if (newPriority < 0) newPriority = 0

            if (index == activeWordPosition) {
                return@mapIndexed it.copy(status = getCorrectStatus(), priority = newPriority)
            }

            return@mapIndexed it
        }

        newList?.let {
            _examWordList.value = it

        }
    }

    fun goToNextQuestion() {
        activeWordPosition++
        if (activeWordPosition >= generateList().size) {
            activeWordPosition = 0
        }
        goNext()
    }

    private fun getIsAnswerCorrect(): Boolean {
        return _answerResult.value!! == SUCCESS
    }

    private fun getCorrectStatus(): ExamWordStatus {
        Timber.d(_answerResult.value.toString())
        return when (_answerResult.value!!) {
            SUCCESS -> {
                ExamWordStatus.SUCCESS
            }
            FAILED -> {
                ExamWordStatus.FAIL
            }
            EMPTY -> {
                ExamWordStatus.IN_PROCESS
            }
        }

    }

    private fun goNext() {

        Timber.d("currentPosition $activeWordPosition")
        // close hints and variants when go to text word
        _isShownHintsVisible.value = false
        _allHintsShown.value = false
        _isShownVariants.value = false

        _answerResult.value = EMPTY
        updatePositionColors()
        _currentWord.value = _examWordList.value!![activeWordPosition]
    }

}


fun generateList(): List<ExamWord> {
    return listOf(
        ExamWord(
            priority = 5,
            id = 1L,
            value = "Apple",
            translates = listOf(
                TranslateWordItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "яблуко"
                ),
                TranslateWordItem(
                    id = "2",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "dwdw"
                ),
                TranslateWordItem(
                    id = "3",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "банан"
                ),
                TranslateWordItem(
                    id = "4",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "кіт"
                )
            ),
            hints = listOf(
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "an fruit"
                ),
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "green"
                ),
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "you can eat this"
                ),
            ),
            status = ExamWordStatus.IN_PROCESS,
            answerVariants = listOf(
                "Груша", "апельсин", "груша", "яблучко",
                "яблуко", "машина", "тост",
            ),
        ),

        ExamWord(
            priority = 5,
            id = 2L,
            value = "Car",
            translates = listOf(
                TranslateWordItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "Машина"
                )
            ),
            hints = listOf(
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "може їздити"
                ),
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "має 4 колеса"
                ),
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "робить біп-біп"
                ),
            ),
            status = ExamWordStatus.UNPROCESSED,
            answerVariants = listOf(
                "Парус", "апельсин", "машина", "яблучко",
                "яблуко", "тост",
            ),
        ),
        ExamWord(
            priority = 5,
            id = 3L,
            value = "Frog",
            translates = listOf(
                TranslateWordItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "жаба"
                )
            ),
            hints = listOf(
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "зелене"
                ),
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "квакає"
                ),
                HintItem(
                    id = "1",
                    createdAt = 1L,
                    updatedAt = 1L,
                    value = "живе біля водойми"
                ),
            ),
            status = ExamWordStatus.UNPROCESSED,
            answerVariants = listOf(
                "Ялинка", "апельсин", "жабка", "яблучко",
                "яблуко", "жаба",
            ),
        )
    )
}

