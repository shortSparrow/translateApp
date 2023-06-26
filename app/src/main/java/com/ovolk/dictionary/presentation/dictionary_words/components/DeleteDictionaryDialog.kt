package com.ovolk.dictionary.presentation.dictionary_words.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.ConfirmDialog

@Composable
fun DeleteDictionaryDialog(
    dictionaryTitle: String,
    onConfirmDelete: () -> Unit,
    onDecline: () -> Unit
) {
    ConfirmDialog(
        title = stringResource(id = R.string.dictionary_word_list_confirm_delete_title, dictionaryTitle),
        description = stringResource(id = R.string.dictionary_word_list_confirm_delete_description),
        descriptionColor= colorResource(id = R.color.red),
        onAcceptClick = { onConfirmDelete() },
        onDeclineClick = { onDecline() }
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteDictionaryDialogPreview() {
    DeleteDictionaryDialog(
        dictionaryTitle = "EN-UK",
        onConfirmDelete = { },
        onDecline = { },
    )
}