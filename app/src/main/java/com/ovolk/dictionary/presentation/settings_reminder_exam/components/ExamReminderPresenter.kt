package com.ovolk.dictionary.presentation.settings_reminder_exam.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.ovolk.dictionary.domain.model.exam_reminder.FrequencyItem
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.settings_reminder_exam.OnExamReminderAction
import com.ovolk.dictionary.presentation.settings_reminder_exam.SettingsReminderExamState
import com.ovolk.dictionary.util.MAX_BUTTON_WIDTH
import com.ovolk.dictionary.util.PushFrequency

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExamReminderPresenter(
    state: SettingsReminderExamState,
    onAction: (OnExamReminderAction) -> Unit,
    goBack: () -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val paddingVertical = dimensionResource(id = R.dimen.small_gutter)
    val timePickerDialog = TimePickerDialog(
        context,
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
                androidx.compose.foundation.layout.FlowRow(
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
                    Text(
                        text = stringResource(id = R.string.settings_exam_reminder_time_push_notification),
                        modifier = Modifier.weight(1f)
                    )
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
                        color = colorResource(id = R.color.grey_2),
                        maxLines = 1
                    )
                }

//                ReminderPermission()
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

@Preview(showBackground = true)
@Composable
fun ExamReminderPresenterPreview() {
    ExamReminderPresenter(
        state = SettingsReminderExamState(
            reminderTime = "10:00",
            frequencyList = listOf(
                FrequencyItem(
                    title = stringResource(R.string.settings_exam_reminder_frequency_once_a_day),
                    PushFrequency.ONCE_AT_DAY
                ),
            ),
            selectedFrequency = FrequencyItem(
                title = stringResource(id = R.string.settings_exam_reminder_frequency_once_a_day),
                PushFrequency.ONCE_AT_DAY
            ),
        ),
        onAction = {},
        goBack = {}
    )
}
