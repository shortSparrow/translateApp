package com.ovolk.dictionary.presentation.dictionary_words.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsAction

@Composable
fun DictionaryHeader(
    isActive: Boolean,
    dictionaryTitle: String?,
    onAction: (DictionaryWordsAction) -> Unit,
    onBackButtonClick: () -> Unit,
) {
    var showMoreExpanded by remember {
        mutableStateOf(false)
    }
    Header(
        title = {
            if (isActive) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        stringResource(id = R.string.delete),
                        tint = colorResource(R.color.blue),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(20.dp)
                    )
                    Text(text = dictionaryTitle as String)
                }
            } else {
                Text(text = dictionaryTitle ?: "")

            }
        },
        onBackButtonClick = { onBackButtonClick() },
        onFirstRightIconClick = {
            showMoreExpanded = true
        },
        firstRightIcon = {
            Box() {
                Icon(
                    painter = painterResource(R.drawable.more_vertical),
                    "more",
                    tint = colorResource(R.color.grey),
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                )

                ShowMoreDropdown(
                    showMoreExpanded = showMoreExpanded,
                    setShowMoreExpanded = { showMoreExpanded = it },
                    isDictionaryActive = isActive,
                    onAction = onAction,
                )

            }
        },
    )

}

@Preview(showBackground = true)
@Composable
fun DictionaryHeaderPreviewIsActive() {
    DictionaryHeader(
        isActive = true,
        dictionaryTitle = "EN-UK",
        onBackButtonClick = { },
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
fun DictionaryHeaderPreviewNotActive() {
    DictionaryHeader(
        isActive = false,
        dictionaryTitle = "EN-UK",
        onBackButtonClick = { },
        onAction = {},
    )
}