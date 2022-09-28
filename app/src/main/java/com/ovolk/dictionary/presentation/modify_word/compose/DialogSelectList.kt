package com.ovolk.dictionary.presentation.modify_word.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.presentation.core.compose.dialog.MyDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogSelectList(
    list: List<ModifyWordListItem>,
    onDismissRequest: () -> Unit,
    onItemsPress: (id: Long) -> Unit,
    onAddNewItemPress: () -> Unit,
) {
    MyDialog(
        title = stringResource(id = R.string.modify_word_select_lists_dialog_title),
        onDismissRequest = { onDismissRequest() },
        content = {
            if (list.isEmpty()) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.modify_word_select_lists_dialog_description),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        fontSize = 17.sp
                    )
                    OutlinedButton(
                        onClick = onAddNewItemPress,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = stringResource(id = R.string.modify_word_select_lists_dialog_add_list).uppercase())
                    }
                }
            } else {
                Column(Modifier.heightIn(0.dp, 400.dp)) {
                    CompositionLocalProvider(
                        LocalOverscrollConfiguration provides null
                    ) {
                        LazyColumn {
                            items(items = list) { item ->
                                Box(Modifier.padding(bottom = 20.dp)) {
                                    ListItem(
                                        wordListInfo = item,
                                        onItemsPress = { onItemsPress(item.id) },
                                        withMark = true
                                    )
                                }
                            }
                            item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedButton(onClick = onAddNewItemPress) {
                                        Text(text = stringResource(id = R.string.modify_word_select_lists_dialog_add_list))
                                    }
                                }

                            }
                        }
                    }
                }
            }

        }
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewDialogSelectList() {
    DialogSelectList(
        list = listOf(),
        onDismissRequest = {},
        onItemsPress = {},
        onAddNewItemPress = {}
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewDialogSelectList2() {
    DialogSelectList(
        list = listOf(
            ModifyWordListItem(
                title = "My List",
                count = 10,
                id = 1L,
                isSelected = false
            ),
            ModifyWordListItem(
                title = "Sport",
                count = 5,
                id = 2L,
                isSelected = true
            )
        ),
        onDismissRequest = {},
        onItemsPress = {},
        onAddNewItemPress = {}
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewDialogSelectListLarge() {
    DialogSelectList(
        list = listOf(
            ModifyWordListItem(
                title = "My List",
                count = 10,
                id = 1L,
                isSelected = false
            ),
            ModifyWordListItem(
                title = "Sport",
                count = 5,
                id = 2L,
                isSelected = true
            ),
            ModifyWordListItem(
                title = "A",
                count = 5,
                id = 3L,
                isSelected = false
            ),
            ModifyWordListItem(
                title = "B",
                count = 1,
                id = 4L,
                isSelected = false
            ),
            ModifyWordListItem(
                title = "C",
                count = 3,
                id = 5L,
                isSelected = false
            ),
            ModifyWordListItem(
                title = "D",
                count = 3,
                id = 6L,
                isSelected = false
            ),
        ),
        onDismissRequest = {},
        onItemsPress = {},
        onAddNewItemPress = {}
    )
}