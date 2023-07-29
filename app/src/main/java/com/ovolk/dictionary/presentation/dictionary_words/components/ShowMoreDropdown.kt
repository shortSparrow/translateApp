package com.ovolk.dictionary.presentation.dictionary_words.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dropdown_menu.ShowMoreIconsDropdownMenu
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsAction

data class ShowMoreItem(
    val onClick: () -> Unit,
    val iconPainter: Painter,
    val iconTint: Color,
    val iconContentDescription: String,
    val text: String,
)

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

    val showMoreItems = listOf<ShowMoreItem>(
        ShowMoreItem(
            onClick = { onAction(DictionaryWordsAction.OnPressTakeExam) },
            iconPainter = painterResource(R.drawable.exam),
            iconTint = colorResource(R.color.grey),
            iconContentDescription = stringResource(id = R.string.exam),
            text = stringResource(id = R.string.exam),
        ),
        ShowMoreItem(
            onClick = { onAction(DictionaryWordsAction.OnPressEditDictionary) },
            iconPainter = painterResource(R.drawable.edit),
            iconTint = colorResource(R.color.grey),
            iconContentDescription = stringResource(id = R.string.edit),
            text = stringResource(id = R.string.edit),
        ),
        ShowMoreItem(
            onClick = { onAction(DictionaryWordsAction.MarkAsActive) },
            iconPainter = painterResource(R.drawable.star),
            iconTint = colorResource(R.color.blue),
            iconContentDescription = stringResource(id = R.string.make_active),
            text = stringResource(id = R.string.make_active),
        ),
        ShowMoreItem(
            onClick = { onAction(DictionaryWordsAction.HandleDeleteDictionaryModal(true)) },
            iconPainter = painterResource(R.drawable.delete_active),
            iconTint = colorResource(R.color.red),
            iconContentDescription = stringResource(id = R.string.delete),
            text = stringResource(id = R.string.delete),
        ),
    )



    ShowMoreIconsDropdownMenu(
        expanded = showMoreExpanded,
        onDismissRequest = { setShowMoreExpanded(false) },
        properties = PopupProperties(focusable = true),
    ) {

        showMoreItems.forEach { item ->
            if(item.text == stringResource(id = R.string.make_active) && isDictionaryActive) {
                return@forEach
            }
            Box(
                modifier = Modifier
                    .clickable { onDropdownItemClick { item.onClick() } }
                    .padding(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = item.iconPainter,
                        contentDescription = item.iconContentDescription,
                        tint = item.iconTint,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .width(18.dp)
                            .height(18.dp)
                    )
                    Text(
                        text = item.text,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Divider()
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