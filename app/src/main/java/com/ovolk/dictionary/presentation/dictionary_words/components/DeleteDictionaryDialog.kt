package com.ovolk.dictionary.presentation.dictionary_words.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsAction

@Composable
fun DeleteDictionaryDialog(
    dictionaryTitle: String,
    onConfirmDelete: () -> Unit,
    onDecline: () -> Unit
) {
    ConfirmDialog(
        message = {
            Text(
                buildAnnotatedString {
                    append("Are you sure you want to delete $dictionaryTitle dictionary?")

                    withStyle(
                        style = SpanStyle(
                            color = colorResource(id = R.color.red_2),
                            fontSize = 13.sp,
                        )
                    ) {
                        append("\n \n All words in this dictionary will be deleted")
                    }

                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
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