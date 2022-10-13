package com.ovolk.dictionary.presentation.lists.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.presentation.core.compose.dialog.MyDialog
import com.ovolk.dictionary.presentation.lists.ListsAction
import com.ovolk.dictionary.presentation.lists.ModalListState
import com.ovolk.dictionary.presentation.lists.ModalType

@Composable
fun DialogAddNewList(
    modalListState: ModalListState,
    modalError: SimpleError,
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

    MyDialog(
        onDismissRequest = { onAction(ListsAction.CloseModal) },
        content = {
            TextField(
                value = newListName,
                onValueChange = {
                    newListName = it
                    onAction(ListsAction.ResetModalError)
                },
                label = { Text(stringResource(id = R.string.lists_screen_add_new_list_label)) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                isError = modalError.isError
            )
            Box(Modifier.fillMaxWidth()) {
                if (modalError.isError) {
                    Text(
                        text = modalError.text,
                        maxLines = 1,
                        color = colorResource(id = R.color.red),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }

                OutlinedButton(
                    onClick = { handleOnClick() },
                    Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Center)
                ) {
                    Text(text = stringResource(id = R.string.save).uppercase())
                }
            }

        },
        title = modalListState.title
    )

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
        ),
        modalError = SimpleError(isError = true, text = "Enter list name")
    )
}