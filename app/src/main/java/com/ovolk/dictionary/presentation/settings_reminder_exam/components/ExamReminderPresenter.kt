package com.ovolk.dictionary.presentation.settings_reminder_exam.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.compose.header.Header
import com.ovolk.dictionary.presentation.settings_reminder_exam.OnExamReminderAction
import com.ovolk.dictionary.presentation.settings_reminder_exam.SettingsReminderExamState
import com.ovolk.dictionary.util.MAX_BUTTON_WIDTH

@Composable
fun ExamReminderPresenter(
    state: SettingsReminderExamState,
    onAction: (OnExamReminderAction) -> Unit,
    goBack: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val paddingVertical = dimensionResource(id = R.dimen.small_gutter)
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour: Int, minute: Int ->
            onAction(OnExamReminderAction.OnChangeTime(hours = hour, minutes = minute))
        }, state.timeHours, state.timeMinutes, true
    )

    Column {
        Header(
            title = stringResource(id = R.string.settings_exam_reminder_screen_title),
            withBackButton = true,
            onBackButtonClick = goBack
        )
        Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = paddingVertical)
                ) {
                    Text(text = stringResource(id = R.string.settings_exam_reminder_label))
                    DropDown(
                        state = state,
                        onChange = { frequencyItem ->
                            onAction(
                                OnExamReminderAction.OnChangeFrequency(frequencyItem)
                            )
                        },
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.gutter))
                    )
                }

                Row(
                    modifier = Modifier.padding(vertical = paddingVertical),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.settings_exam_reminder_time_push_notification))
                    Text(
                        text = state.reminderTime,
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { timePickerDialog.show() }
                            .padding(
                                start = dimensionResource(
                                    id = R.dimen.gutter
                                )
                            ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.grey_2)
                    )
                }


                if (state.leftTimeToNextExam.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(vertical = paddingVertical),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.settings_exam_reminder_left_before_next_reminder))
                        Text(
                            text = state.leftTimeToNextExam,
                            modifier = Modifier.padding(
                                start = dimensionResource(
                                    id = R.dimen.gutter
                                )
                            ),
                            color = colorResource(id = R.color.grey_2),
                        )
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = dimensionResource(id = R.dimen.small_gutter))
            ) {
                Button(
                    onClick = { onAction(OnExamReminderAction.SaveChanges) },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .widthIn(100.dp, MAX_BUTTON_WIDTH),
                    enabled = state.isStateChanges
                ) {
                    Text(
                        text = stringResource(id = R.string.save).uppercase(),
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExamReminderPresenterPreview() {
    ExamReminderPresenter(
        state = SettingsReminderExamState(
            leftTimeToNextExam = "22:00",
            reminderTime = "10:00"
        ),
        onAction = {},
        goBack={}
    )
}