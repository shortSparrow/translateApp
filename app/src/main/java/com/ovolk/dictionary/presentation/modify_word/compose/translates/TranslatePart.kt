package com.ovolk.dictionary.presentation.modify_word.compose.translates

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.presentation.list_full.components.getPreviewTranslates
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.presentation.modify_word.ModifyWordTranslatesAction
import com.ovolk.dictionary.presentation.modify_word.Translates

@Composable
fun TranslatePart(translatesState: Translates, onAction: (ModifyWordTranslatesAction) -> Unit) {
    val focusRequesterTranslates = remember { FocusRequester() }

    Column {
        TranslateInput(
            translatesState = translatesState,
            focusRequesterTranslates = focusRequesterTranslates,
            onAction = onAction
        )

        TranslateList(
            translates = translatesState.translates,
            focusRequesterTranslates = focusRequesterTranslates,
            onAction = onAction
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TranslatePartPreview() {
    TranslatePart(
        translatesState = Translates(
            translates = getPreviewTranslates()
        ),
        onAction = {}
    )
}