package com.ovolk.dictionary.presentation.exam

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.presentation.core.compose.dialog.InfoDialog
import com.ovolk.dictionary.presentation.core.compose.header.Header
import com.ovolk.dictionary.presentation.exam.components.ExamList
import com.ovolk.dictionary.presentation.exam.components.InputWord
import com.ovolk.dictionary.presentation.exam.components.NavigationPart
import com.ovolk.dictionary.presentation.exam.components.WordIsCheckedPart
import com.ovolk.dictionary.presentation.exam.components.modal.SelectExamMode
import com.ovolk.dictionary.presentation.exam.components.variants_and_hints.VariantsAndHints
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewExamListAllStatus

@Composable
fun ExamScreen(
    state: ExamKnowledgeState,
    onAction: (ExamAction) -> Unit
) {
    // TODO add on empty list and loading

    if (state.isModeDialogOpen) {
        SelectExamMode(mode = state.mode, onAction = onAction)
    }

    if (state.isExamEnd) {
        InfoDialog(
            onDismissRequest = { onAction(ExamAction.CloseTheEndExamModal) },
            message = stringResource(
                id = R.string.exam_alert_complete_daily_exam_description
            ),
            onClick = { onAction(ExamAction.CloseTheEndExamModal) },
            buttonText = stringResource(id = R.string.ok)
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 10.dp)
    ) {
        Header(
            title = stringResource(id = R.string.exam_counter_toolbar_title_daily_mode),
            firstRightIcon = {
                Image(
                    painter = painterResource(id = R.drawable.exam_mode),
                    contentDescription = stringResource(id = R.string.exam_select_exam_daily_mode_cd),
                    modifier = Modifier
                        .width(34.dp)
                        .height(34.dp)
                )
            },
            onFirstRightIconClick = { onAction(ExamAction.ToggleSelectModeModal(true)) }
        )

        val currentWord = state.examWordList.getOrNull(state.activeWordPosition)
        currentWord?.let {
            val currentWordFreeze =
                currentWord.status == ExamWordStatus.SUCCESS || currentWord.status == ExamWordStatus.FAIL
            val answerIsCorrect = currentWord.status == ExamWordStatus.SUCCESS
            val answerIsWrong = currentWord.status == ExamWordStatus.FAIL

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.medium_gutter))
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
                    word = currentWord.value,
                    answerValue = state.answerValue,
                    onAction = onAction
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
}

@Preview(showBackground = true)
@Composable
fun ExamScreenPreview() {
    ExamScreen(
        state = ExamKnowledgeState(
            examWordList = getPreviewExamListAllStatus(),
            isAllExamWordsLoaded = true,
        ),
        onAction = {}
    )
}