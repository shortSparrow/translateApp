package com.ovolk.dictionary.presentation.exam.components.empty_exam

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.exam.ExamAction

@Composable
fun EmptyExam(onAction: (ExamAction) -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.nothing_found),
        contentDescription = stringResource(
            id = R.string.nothing_found
        ),
        modifier = Modifier
            .size(250.dp)
            .padding(dimensionResource(id = R.dimen.gutter))
    )

    Text(
        text = stringResource(id = R.string.word_list_empty_exam_list),
        modifier = Modifier.padding(dimensionResource(id = R.dimen.gutter)),
        fontSize = 18.sp,
        color = colorResource(id = R.color.grey),
        textAlign = TextAlign.Center
    )
    Button(onClick = { onAction(ExamAction.OnNavigateToCreateFirstWord) }) {
        Text(
            text = stringResource(id = R.string.word_list_add_first_word).uppercase(),
            color = colorResource(id = R.color.white)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyExamPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmptyExam(onAction = {})
    }
}