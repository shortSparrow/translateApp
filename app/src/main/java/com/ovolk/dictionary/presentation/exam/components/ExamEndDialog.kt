package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.BaseDialog
import com.ovolk.dictionary.presentation.exam.CompleteAlertBehavior
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.presentation.exam.ExamMode

@Composable
fun ExamEndDialog(mode: ExamMode, onAction: (ExamAction) -> Unit) {
    val message =
        if (mode == ExamMode.DAILY_MODE)
            stringResource(id = R.string.exam_alert_complete_daily_exam_description)
        else stringResource(id = R.string.exam_complete_infinity_exam)


    BaseDialog(
        onDismissRequest = { onAction(ExamAction.CloseTheEndExamModal(CompleteAlertBehavior.STAY_HERE)) },
    ) {

        Text(
            text = message,
            Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            OutlinedButton(onClick = {
                onAction(
                    ExamAction.CloseTheEndExamModal(
                        CompleteAlertBehavior.STAY_HERE
                    )
                )
            }) {
                Text(
                    text = stringResource(id = R.string.exam_complete_alert_stay_here),
                    color = colorResource(id = R.color.blue)
                )
            }
            Button(onClick = { onAction(ExamAction.CloseTheEndExamModal(CompleteAlertBehavior.GO_HOME)) }) {
                Text(
                    text = stringResource(id = R.string.exam_complete_alert_go_home),
                    color = colorResource(id = R.color.white)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExamEndDialogPreview() {
    ExamEndDialog(mode = ExamMode.DAILY_MODE, onAction = {})
}

@Preview(showBackground = true)
@Composable
fun ExamEndDialogPreview2() {
    ExamEndDialog(mode = ExamMode.INFINITY_MODE, onAction = {})
}
