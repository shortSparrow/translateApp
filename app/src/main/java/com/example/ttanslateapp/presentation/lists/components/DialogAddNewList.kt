package com.example.ttanslateapp.presentation.lists.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttanslateapp.presentation.core.compose.dialog.MyDialog
import com.example.ttanslateapp.presentation.lists.ListsViewModel
import com.example.ttanslateapp.presentation.lists.ModalListState
import com.example.ttanslateapp.presentation.lists.ModalType

@Composable
fun DialogAddNewList(
    saveNewList: (title: String) -> Unit,
    modalListState: ModalListState
) {
    val viewModel = hiltViewModel<ListsViewModel>()

    var newListName by remember {
        mutableStateOf(modalListState.initialValue)
    }

    MyDialog(onDismissRequest = { viewModel.closeModalList() }, content = {
        TextField(
            value = newListName,
            onValueChange = {
                newListName = it
            },
            label = { Text("Label") }
        )
        Button(
            onClick = { saveNewList(newListName) },
            Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Save")
        }
    }, title = modalListState.title)

}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreview2() {
    DialogAddNewList(
        saveNewList = {},
        modalListState = ModalListState(
            isOpen = true,
            type = ModalType.NEW,
            title = "Add New List"
        )
    )
}