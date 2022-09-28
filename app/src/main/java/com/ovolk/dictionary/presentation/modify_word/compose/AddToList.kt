package com.ovolk.dictionary.presentation.modify_word.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
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
import com.ovolk.dictionary.presentation.core.compose.dialog.MyDialog
import com.ovolk.dictionary.presentation.modify_word.ComposeState

@Composable
fun AddToList(
    state: ComposeState,
    addNewList: (title: String) -> Unit,
    onSelectList: (id: Long) -> Unit
) {
    var isOpenSelectModal by remember {
        mutableStateOf(false)
    }

    var isOpenAddNewListModal by remember {
        mutableStateOf(false)
    }

    var newListName by remember {
        mutableStateOf("")
    }

    fun openModal() {
        isOpenSelectModal = true
    }

    fun closeModal() {
        isOpenSelectModal = false
    }


    if (isOpenAddNewListModal) {
        MyDialog(onDismissRequest = {
            isOpenAddNewListModal = false
            newListName = ""
        }, content = {
            TextField(
                value = newListName,
                onValueChange = {
                    newListName = it
                },
                label = { Text(stringResource(id = R.string.modify_word_dialog_add_new_list_placeholder)) }
            )
            OutlinedButton(
                onClick = {
                    addNewList(newListName)
                    isOpenAddNewListModal = false
                    newListName = ""
                },
                Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.save).uppercase())
            }
        }, title = stringResource(id = R.string.modify_word_dialog_add_new_list_title))
    }

    if (isOpenSelectModal) {
        DialogSelectList(
            list = state.wordLists,
            onDismissRequest = { closeModal() },
            onItemsPress = { id: Long -> onSelectList(id) },
            onAddNewItemPress = { isOpenAddNewListModal = true }
        )
    }

    if (state.wordListInfo == null) {
//        Text(
//            text = stringResource(id = R.string.modify_word_add_to_list),
//            color = colorResource(id = R.color.blue_3),
//            fontSize = 14.sp,
//            modifier = Modifier.clickable { openModal() })

        Column() {
            OutlinedButton(onClick = { openModal() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.modify_word_add_to_list).uppercase(),
//                    color = Color.White,
//                    fontSize = 20.sp,
                    )
                    Icon(
                        modifier = Modifier.padding(start = 10.dp),
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "add"
                    )
                }
            }
        }
    } else {
        Column() {
            Text(
                text = stringResource(id = R.string.modify_word_select_list),
                color = colorResource(id = R.color.blue_3),
                fontSize = 14.sp
            )

            ListItem(
                wordListInfo = state.wordListInfo,
                onItemsPress = { openModal() },
                withMark = false
            )
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
//@Composable
//fun ComposablePreviewAddToList() {
//    AddToList(
//        state = ComposeState(
//            wordListInfo = ModifyWordListItem(
//                title = "My List",
//                count = 10,
//                id = 1L
//            ),
//            wordLists = emptyList()
//        ),
//        addNewList = {},
//        onSelectList = {}
//    )
//}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewAddToList2() {
    AddToList(
        state = ComposeState(
            wordListInfo = null,
            wordLists = emptyList()
        ),
        addNewList = {},
        onSelectList = {}
    )
}