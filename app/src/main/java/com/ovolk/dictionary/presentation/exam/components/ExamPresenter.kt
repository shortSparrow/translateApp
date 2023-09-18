package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.presentation.core.dictionaries.NoActiveDictionary
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.core.scrollableWrapper.ScrollableWrapperScreen
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.presentation.exam.ExamKnowledgeState
import com.ovolk.dictionary.presentation.exam.ExamMode
import com.ovolk.dictionary.presentation.exam.components.empty_exam.EmptyExam
import com.ovolk.dictionary.presentation.exam.components.modal.SelectExamMode
import com.ovolk.dictionary.presentation.exam.components.variants_and_hints.VariantsAndHints
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewExamListAllStatus

@Composable
fun ExamPresenter(
    state: ExamKnowledgeState,
    onAction: (ExamAction) -> Unit
) {

    if (state.isModeDialogOpen) {
        SelectExamMode(mode = state.mode, onAction = onAction)
    }

    if (state.isExamEnd) {
        ExamEndDialog(mode = state.mode, onAction = onAction)
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = colorResource(id = R.color.blue))
        }
    }

    val title = when (state.mode) {
        ExamMode.DAILY_MODE -> stringResource(id = R.string.exam_counter_toolbar_title_daily_mode)
        ExamMode.INFINITY_MODE -> stringResource(id = R.string.exam_counter_toolbar_title_infinity_mode)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            title = title,
            firstRightIcon = {
                Image(
                    painter = painterResource(id = R.drawable.exam_mode),
                    contentDescription = stringResource(id = R.string.exam_select_exam_daily_mode_cd),
                    modifier = Modifier
                        .width(34.dp)
                        .height(34.dp)
                )
            },
            onFirstRightIconClick = { onAction(ExamAction.ToggleSelectModeModal(true)) },
            withBackButton = false
        )

        val listName =
            if (state.listName.isEmpty()) "" else stringResource(
                id = R.string.exam_list_name,
                state.listName
            )

        Text(
            text = listName,
            modifier = Modifier.fillMaxWidth(1f),
            textAlign = TextAlign.Center
        )

        val currentWord = state.examWordList.getOrNull(state.activeWordPosition)
        currentWord?.let {
            val currentWordFreeze =
                currentWord.status == ExamWordStatus.SUCCESS || currentWord.status == ExamWordStatus.FAIL
            val answerIsCorrect = currentWord.status == ExamWordStatus.SUCCESS
            val answerIsWrong = currentWord.status == ExamWordStatus.FAIL

            Column(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.gutter))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.exam_counter,
                            state.activeWordPosition + 1,
                            state.examListTotalCount
                        ),
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter))
                    )
                    ExamList(
                        examWordList = state.examWordList,
                        isAllExamWordsLoaded = state.isAllExamWordsLoaded,
                        activeWordPosition = state.activeWordPosition,
                        onAction = onAction,
                    )
                }

                Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {
                    InputWord(
                        isInverseWord = currentWord.isInverseWord,
                        inverseWordValues = currentWord.inverseValueList,
                        word = currentWord.value,
                        answerValue = state.answerValue,
                        onAction = onAction,
                        currentWordFreeze = currentWordFreeze,
                        isDoubleLanguageExamEnable = state.isDoubleLanguageExamEnable,
                        isAutoSuggestEnable = state.isAutoSuggestEnable
                    )

                    NavigationPart(
                        answerIsCorrect = answerIsCorrect,
                        answerIsWrong = answerIsWrong,
                        givenAnswer = currentWord.givenAnswer,
                        currentInputValue = state.answerValue,
                        activeWordPosition = state.activeWordPosition,
                        listSize = state.examWordList.size,
                        onAction = onAction
                    )
                    WordIsCheckedPart(
                        currentInputValue = state.answerValue,
                        status = currentWord.status,
                        currentWordFreeze = currentWordFreeze,
                        isTranslateExpanded = state.isTranslateExpanded,
                        isHiddenTranslateDescriptionExpanded = state.isHiddenTranslateDescriptionExpanded,
                        translates = currentWord.translates,
                        onAction = onAction
                    )

                    if (!currentWordFreeze) {
                        VariantsAndHints(
                            isVariantsExpanded = state.isVariantsExpanded,
                            isHintsExpanded = state.isHintsExpanded,
                            hints = currentWord.hints,
                            answerVariants = currentWord.answerVariants,
                            onAction = onAction
                        )
                    }
                }
            }
        }

        if (!state.isLoading && state.dictionaryId == null) {
            Box(modifier = Modifier.weight(1f)) {
                ScrollableWrapperScreen {
                    NoActiveDictionary(onPressAddNewDictionary = { onAction(ExamAction.OnPressAddDictionary) })
                }
            }
        }

        if (!state.isLoading && currentWord == null && state.dictionaryId != null) {
            Box(modifier = Modifier.weight(1f)) {
                ScrollableWrapperScreen {
                    EmptyExam(onAction = onAction)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExamScreenPreview() {
    ExamPresenter(
        state = ExamKnowledgeState(
            examWordList = getPreviewExamListAllStatus(),
            isAllExamWordsLoaded = true,
            dictionaryId = 1L,
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ExamScreenPreview2() {
    ExamPresenter(
        state = ExamKnowledgeState(
            examWordList = emptyList(),
            isAllExamWordsLoaded = true,
            isLoading = false,
            dictionaryId = 1L,
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ExamScreenPreview3() {
    ExamPresenter(
        state = ExamKnowledgeState(
            examWordList = emptyList(),
            isAllExamWordsLoaded = true,
            isLoading = false,
            dictionaryId = null,
        ),
        onAction = {}
    )
}
