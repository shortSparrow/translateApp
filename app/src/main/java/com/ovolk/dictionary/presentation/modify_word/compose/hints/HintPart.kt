package com.ovolk.dictionary.presentation.modify_word.compose.hints

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.presentation.modify_word.Hints
import com.ovolk.dictionary.presentation.modify_word.ModifyWordHintsAction

@Composable
fun HintPart(hintsState: Hints, onAction: (ModifyWordHintsAction) -> Unit) {
    val focusRequesterTranslates = remember { FocusRequester() }

    Column {
        HintInput(
            hintsState = hintsState,
            focusRequesterTranslates = focusRequesterTranslates,
            onAction = onAction
        )
        HintList(
            hints = hintsState.hints,
            focusRequesterTranslates = focusRequesterTranslates,
            onAction = onAction
        )
    }
}

@Preview( showBackground = true)
@Composable
fun HintPartPreview() {
    HintPart(
        hintsState = Hints(
            hints = emptyList()
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HintPartPreview2() {
    HintPart(
        hintsState = Hints(
            hints = getPreviewHints()
        ),
        onAction = {}
    )
}