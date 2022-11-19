package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.exam.ExamAction

@Composable
fun InputWord(word: String, answerValue: String, onAction: (ExamAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.medium_gutter),
                bottom = dimensionResource(id = R.dimen.gutter)
            ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = word,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.grey),
            fontSize = 20.sp,
        )
    }

    OutlinedErrableTextField(
        value = answerValue,
        onValueChange = { value -> onAction(ExamAction.OnInputTranslate(value)) },
        label = { Text(text = stringResource(id = R.string.word_translate)) },
    )
}

@Preview(showBackground = true)
@Composable
fun InputWordPreview() {
    InputWord(
        word = "Green",
        answerValue = "Зелений",
        onAction = {}
    )
}