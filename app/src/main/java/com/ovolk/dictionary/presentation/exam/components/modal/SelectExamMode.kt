package com.ovolk.dictionary.presentation.exam.components.modal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.BaseDialog
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.presentation.exam.ExamMode
import com.ovolk.dictionary.util.compose.click_effects.clickWithoutFeedback


@Composable
fun SelectExamMode(mode: ExamMode, onAction: (ExamAction) -> Unit) {
    BaseDialog(onDismissRequest = { onAction(ExamAction.ToggleSelectModeModal(false)) }) {
        Surface(shape = RoundedCornerShape(10.dp)) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.gutter))) {
                Text(
                    text = stringResource(id = R.string.exam_alert_select_mode_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(id = R.dimen.gutter)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.grey)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        colors = RadioButtonDefaults.colors(selectedColor = colorResource(id = R.color.blue)),
                        selected = mode == ExamMode.DAILY_MODE,
                        onClick = { onAction(ExamAction.OnSelectMode(ExamMode.DAILY_MODE)) })
                    Text(
                        text = stringResource(id = R.string.exam_daily_mode),
                        Modifier.clickWithoutFeedback { onAction(ExamAction.OnSelectMode(ExamMode.DAILY_MODE)) })
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        colors = RadioButtonDefaults.colors(selectedColor = colorResource(id = R.color.blue)),
                        selected = mode == ExamMode.INFINITY_MODE,
                        onClick = { onAction(ExamAction.OnSelectMode(ExamMode.INFINITY_MODE)) })
                    Text(
                        text = stringResource(id = R.string.exam_infinity_mode),
                        Modifier.clickWithoutFeedback { onAction(ExamAction.OnSelectMode(ExamMode.INFINITY_MODE)) })
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectExamModePreview() {
    SelectExamMode(mode = ExamMode.DAILY_MODE, onAction = {})
}