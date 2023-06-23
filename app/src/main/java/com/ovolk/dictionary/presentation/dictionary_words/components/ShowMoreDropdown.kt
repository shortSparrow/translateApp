package com.ovolk.dictionary.presentation.dictionary_words.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dropdown_menu.ShowMoreIconsDropdownMenu
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsAction

@Composable
fun ShowMoreDropdown(
    showMoreExpanded: Boolean,
    isDictionaryActive: Boolean,
    setShowMoreExpanded: (value: Boolean) -> Unit,
    onAction: (DictionaryWordsAction) -> Unit,
) {
    fun onDropdownItemClick(cb: () -> Unit) {
        setShowMoreExpanded(false)
        cb()
    }


    val iconModifier = Modifier
        .padding(end = 10.dp)
        .width(18.dp)
        .height(18.dp)

    ShowMoreIconsDropdownMenu(
        expanded = showMoreExpanded,
        onDismissRequest = { setShowMoreExpanded(false) },
        properties = PopupProperties(focusable = true),
    ) {
        DropdownMenuItem(onClick = { onDropdownItemClick { DictionaryWordsAction.OnPressTakeExam } }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.exam),
                    stringResource(id = R.string.exam),
                    tint = colorResource(R.color.grey),
                    modifier = iconModifier
                )
                Text(
                    stringResource(id = R.string.exam),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        Divider()
        DropdownMenuItem(onClick = {
            onDropdownItemClick { onAction(DictionaryWordsAction.OnPressEditDictionary) }
        }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.edit),
                    stringResource(id = R.string.edit),
                    tint = colorResource(R.color.grey),
                    modifier = iconModifier
                )
                Text(
                    stringResource(id = R.string.edit),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (!isDictionaryActive) {
            Divider()
            DropdownMenuItem(onClick = {
                onDropdownItemClick { onAction(DictionaryWordsAction.MarkAsActive) }
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        stringResource(id = R.string.delete),
                        tint = colorResource(R.color.blue),
                        modifier = iconModifier
                    )
                    Text(
                        "Make active",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        Divider()
        DropdownMenuItem(
            onClick = {
                onDropdownItemClick {
                    onAction(DictionaryWordsAction.HandleDeleteDictionaryModal(true))
                }
            },
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.delete_active),
                    stringResource(id = R.string.delete),
                    tint = colorResource(R.color.red),
                    modifier = iconModifier
                )
                Text(
                    stringResource(id = R.string.delete),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowMoreDropdownPreview() {
    ShowMoreDropdown(
        showMoreExpanded = true,
        setShowMoreExpanded = { },
        isDictionaryActive = true,
        onAction = {},
    )
}