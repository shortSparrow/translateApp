package com.example.ttanslateapp.presentation.lists.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.core.compose.dialog.MyDialog
import com.example.ttanslateapp.presentation.lists.ListsAction
import com.example.ttanslateapp.presentation.lists.ModalListState
import com.example.ttanslateapp.presentation.lists.ModalType

@Composable
fun DialogAddNewList(
    modalListState: ModalListState,
    onAction: (ListsAction) -> Unit,
) {

    var newListName by remember {
        mutableStateOf(modalListState.initialValue)
    }

    fun handleOnClick() {
        when (modalListState.type) {
            ModalType.NEW -> onAction(ListsAction.AddNewList(newListName))
            ModalType.RENAME -> onAction(ListsAction.RenameList(newListName))
            else -> {}
        }
    }

    MyDialog(onDismissRequest = { onAction(ListsAction.CloseModal) }, content = {
        TextField(
            value = newListName,
            onValueChange = {
                newListName = it
            },
            label = { Text(stringResource(id = R.string.lists_screen_add_new_list_label)) }
        )
        OutlinedButton(
            onClick = { handleOnClick() },
            Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.save).uppercase())
        }
    }, title = modalListState.title)

}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreview2() {
    DialogAddNewList(
        onAction = {},
        modalListState = ModalListState(
            isOpen = true,
            type = ModalType.NEW,
            title = "Add New List"
        )
    )
}