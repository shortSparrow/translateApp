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
    isDictionaryActive: Boolean,
    onConfirmDelete: () -> Unit,
    onDecline: () -> Unit
) {
    val deleteActiveDictionaryInfo = if (isDictionaryActive) {
        "\n \n ${stringResource(id = R.string.dictionary_word_list_remove_active_dictionary)}"
    } else ""

    val description =
        stringResource(id = R.string.dictionary_word_list_confirm_delete_description) + deleteActiveDictionaryInfo

    ConfirmDialog(
        title = stringResource(
            id = R.string.dictionary_word_list_confirm_delete_title,
            dictionaryTitle
        ),
        description = description,
        descriptionColor = colorResource(id = R.color.red),
        onAcceptClick = { onConfirmDelete() },
        onDeclineClick = { onDecline() }
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteDictionaryDialogPreview() {
    DeleteDictionaryDialog(
        dictionaryTitle = "EN-UK",
        isDictionaryActive = true,
        onConfirmDelete = { },
        onDecline = { },
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteDictionaryDialogPreview2() {
    DeleteDictionaryDialog(
        dictionaryTitle = "EN-UK",
        isDictionaryActive = false,
        onConfirmDelete = { },
        onDecline = { },
    )
}