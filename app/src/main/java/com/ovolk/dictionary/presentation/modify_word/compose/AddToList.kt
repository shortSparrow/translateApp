package com.ovolk.dictionary.presentation.modify_word.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.presentation.core.compose.dialog.MyDialog
import com.ovolk.dictionary.presentation.modify_word.ComposeState
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction

@Composable
fun AddToList(
    state: ComposeState,
    addNewList: (title: String) -> Unit,
    onSelectList: (id: Long) -> Unit,
    onAction: (ModifyWordAction) -> Unit,
) {
    var newListName by remember {
        mutableStateOf("")
    }

    if (state.isOpenAddNewListModal) {
        MyDialog(
            onDismissRequest = {
                onAction(ModifyWordAction.HandleAddNewListModal(false))
                newListName = ""
            },
            content = {
                TextField(
                    value = newListName,
                    onValueChange = {
                        newListName = it
                        onAction(ModifyWordAction.ResetModalError)
                    },
                    label = { Text(stringResource(id = R.string.modify_word_dialog_add_new_list_placeholder)) },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    isError = state.modalError.isError
                )
                Box(Modifier.fillMaxWidth()) {
                    if (state.modalError.isError) {
                        Text(
                            text = state.modalError.text,
                            maxLines = 1,
                            color = colorResource(id = R.color.red),
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            addNewList(newListName)
                            newListName = ""
                        },
                        Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.Center)
                    ) {
                        Text(text = stringResource(id = R.string.save).uppercase())
                    }
                }
            },
            title = stringResource(id = R.string.modify_word_dialog_add_new_list_title)
        )
    }

    if (state.isOpenSelectModal) {
        DialogSelectList(
            list = state.wordLists,
            onDismissRequest = { onAction(ModifyWordAction.HandleSelectModal(false)) },
            onItemsPress = { id: Long -> onSelectList(id) },
            onAddNewItemPress = { onAction(ModifyWordAction.HandleAddNewListModal(true)) }
        )
    }

    if (state.wordListInfo == null) {
        Column {
            OutlinedButton(onClick = { onAction(ModifyWordAction.HandleSelectModal(true)) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.modify_word_add_to_list).uppercase())
                    Icon(
                        modifier = Modifier.padding(start = 10.dp),
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = stringResource(id = R.string.add)
                    )
                }
            }
        }
    } else {
        Column {
            Text(
                text = stringResource(id = R.string.modify_word_select_list),
                color = colorResource(id = R.color.blue_3),
                fontSize = 14.sp
            )

            ListItem(
                wordListInfo = state.wordListInfo,
                onItemsPress = { onAction(ModifyWordAction.HandleSelectModal(true)) },
                withMark = false
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewAddToList() {
    AddToList(
        state = ComposeState(
            wordListInfo = ModifyWordListItem(
                title = "My List",
                count = 10,
                id = 1L
            ),
            wordLists = emptyList(),
        ),
        addNewList = {},
        onSelectList = {},
        onAction = {}
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewAddToList2() {
    AddToList(
        state = ComposeState(
            wordListInfo = null,
            wordLists = emptyList()
        ),
        addNewList = {},
        onSelectList = {},
        onAction = {}
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewAddToList3() {
    AddToList(
        state = ComposeState(
            wordListInfo = ModifyWordListItem(
                title = "My List",
                count = 10,
                id = 1L
            ),
            wordLists = emptyList(),
            isOpenAddNewListModal = true,
            isOpenSelectModal = false,
        ),
        addNewList = {},
        onSelectList = {},
        onAction = {}
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewAddToList4() {
    AddToList(
        state = ComposeState(
            wordListInfo = ModifyWordListItem(
                title = "My List",
                count = 10,
                id = 1L
            ),
            wordLists = emptyList(),
            isOpenAddNewListModal = false,
            isOpenSelectModal = true,
        ),
        addNewList = {},
        onSelectList = {},
        onAction = {}
    )
}