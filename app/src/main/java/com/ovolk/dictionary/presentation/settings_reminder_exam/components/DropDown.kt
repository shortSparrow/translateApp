package com.ovolk.dictionary.presentation.settings_reminder_exam.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam_reminder.FrequencyItem
import com.ovolk.dictionary.presentation.settings_reminder_exam.SettingsReminderExamState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDown(
    state: SettingsReminderExamState,
    modifier: Modifier = Modifier,
    onChange: (FrequencyItem) -> Unit
) {
    val width = 210.dp
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = state.selectedFrequency.title,
            onValueChange = { },
            label = { Text(stringResource(id = R.string.frequency)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .width(width)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .width(width)
        ) {
            state.frequencyList.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onChange(selectionOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption.title)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DropDownPreview() {
    DropDown(
        state = SettingsReminderExamState(
            reminderTime = "10:00"
        ),
        onChange = {}
    )
}