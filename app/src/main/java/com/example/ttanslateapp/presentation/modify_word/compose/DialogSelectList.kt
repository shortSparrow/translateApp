package com.example.ttanslateapp.presentation.modify_word.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ttanslateapp.domain.model.modify_word.ModifyWordListItem
import com.example.ttanslateapp.presentation.core.compose.dialog.MyDialog
import com.example.ttanslateapp.presentation.lists.ModalListState
import com.example.ttanslateapp.presentation.lists.ModalType
import com.example.ttanslateapp.presentation.lists.components.DialogAddNewList

@Composable
fun DialogSelectList(
    list: List<ModifyWordListItem>,
    onDismissRequest: () -> Unit,
    onItemsPress: (id: Long) -> Unit,
    onAddNewItemPress: () -> Unit,
) {


    MyDialog(
        title = "Select list",
        onDismissRequest = { onDismissRequest() },
        content = {
            if (list.isEmpty()) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = "There isn't lists. Press Add New List for adding one",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        fontSize = 17.sp
                    )
                    Button(
                        onClick = {
                            onAddNewItemPress()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Add New List")
                    }
                }
            } else {
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

//    DialogSelectList(
//        list = listOf(
//            ModifyWordListItem(
//                title = "My List",
//                count = 10,
//                id = 1L,
//                isSelected = false
//            ),
//            ModifyWordListItem(
//                title = "Sport",
//                count = 5,
//                id = 2L,
//                isSelected = true
//            )
//        ),
//        onDismissRequest = {},
//        onItemsPress = {}
//    onAddNewItemPress = {}
//    )
}