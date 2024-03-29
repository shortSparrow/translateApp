package com.ovolk.dictionary.presentation.modify_word.compose.alerts

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
import com.ovolk.dictionary.domain.model.lists.ModifyWordListItem
import com.ovolk.dictionary.presentation.core.dialog.MyDialog
import com.ovolk.dictionary.presentation.modify_word.compose.lists.ListItem
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewListsList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogSelectList(
    list: List<ModifyWordListItem>,
    onDismissRequest: () -> Unit,
    onItemsPress: (id: Long) -> Unit,
    onAddNewItemPress: () -> Unit,
    selectedId: Long? = null
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
                                        withMark = true,
                                        isSelected = item.id == selectedId
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
                isSelected = false,
                dictionaryId = 1L,
            ),
            ModifyWordListItem(
                title = "Sport",
                count = 5,
                id = 2L,
                isSelected = true,
                dictionaryId = 1L,
            )
        ),
        selectedId = 2L,
        onDismissRequest = {},
        onItemsPress = {},
        onAddNewItemPress = {}
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewDialogSelectListLarge() {
    DialogSelectList(
        selectedId = 2L,
        list = getPreviewListsList(),
        onDismissRequest = {},
        onItemsPress = {},
        onAddNewItemPress = {}
    )
}