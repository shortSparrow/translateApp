package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.presentation.exam.NavigateButtons

@Composable
fun NavigationPart(
    answerIsCorrect: Boolean,
    answerIsWrong: Boolean,
    givenAnswer: String,
    currentInputValue: String,
    activeWordPosition: Int,
    listSize: Int,
    onAction: (ExamAction) -> Unit
) {
    val prevButtonDisabled = activeWordPosition == 0
    val nextButtonDisabled = activeWordPosition == (listSize - 1)
    val isCheckAnswerDisabled = currentInputValue.trim()
        .isEmpty() || answerIsCorrect || answerIsWrong

    val text = if (givenAnswer.trim().isNotEmpty()) {
        stringResource(id = R.string.exam_your_answer, givenAnswer)
    } else ""

    val color = if (answerIsCorrect) {
        colorResource(id = R.color.green)
    } else if (answerIsWrong) {
        colorResource(R.color.red)
    } else Color.Unspecified

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.small_gutter)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = color, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ArrowButton(
                type = NavigateButtons.PREVIOUS,
                isDisabled = prevButtonDisabled,
                onAction = onAction,
            )
            Button(
                enabled = !isCheckAnswerDisabled,
                onClick = { onAction(ExamAction.OnCheckAnswer) }
            ) {
                Text(
                    text = stringResource(id = R.string.check_answer).uppercase(),
                    color = colorResource(id = R.color.white)
                )
            }
            ArrowButton(
                type = NavigateButtons.NEXT,
                isDisabled = nextButtonDisabled,
                onAction = onAction
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationPartPreview1() {
    NavigationPart(
        answerIsCorrect = true,
        answerIsWrong = false,
        currentInputValue = "",
        givenAnswer = "Грінка",
        activeWordPosition = 0,
        listSize = 1,
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun NavigationPartPreview2() {
    NavigationPart(
        answerIsCorrect = false,
        answerIsWrong = true,
        givenAnswer = "Грінка",
        currentInputValue = "",
        activeWordPosition = 2,
        listSize = 3,
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun NavigationPartPreview3() {
    NavigationPart(
        answerIsCorrect = false,
        answerIsWrong = false,
        currentInputValue = "some value",
        givenAnswer = "",
        activeWordPosition = 0,
        listSize = 3,
        onAction = {}
    )
}