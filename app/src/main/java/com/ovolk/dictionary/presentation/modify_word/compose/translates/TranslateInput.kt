package com.ovolk.dictionary.presentation.modify_word.compose.translates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.core.compose.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.list_full.components.getPreviewTranslates
import com.ovolk.dictionary.presentation.modify_word.ModifyWordTranslatesAction
import com.ovolk.dictionary.presentation.modify_word.Translates

@Composable
fun TranslateInput(
    translatesState: Translates,
    focusRequesterTranslates: FocusRequester,
    onAction: (ModifyWordTranslatesAction) -> Unit
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (translatesState.editableTranslate != null) {
                    Text(
                        modifier = Modifier
                            .clickable { onAction(ModifyWordTranslatesAction.CancelEditTranslate) },
                        textAlign = TextAlign.Right,
                        text = "Cancel edit"
                    )
                }
            }

            OutlinedErrableTextField(
                modifier = Modifier.focusRequester(focusRequesterTranslates),
                value = translatesState.translationWord,
                onValueChange = { value ->
                    onAction(
                        ModifyWordTranslatesAction.OnChangeTranslate(
                            value
                        )
                    )
                },
                label = { Text(text = stringResource(id = R.string.modify_word_transcription)) },
                isError = !translatesState.error.successful,
                errorMessage = translatesState.error.errorMessage,
            )
        }
        Button(
            onClick = { onAction(ModifyWordTranslatesAction.OnPressAddTranslate) },
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp)
                .width(85.dp)
        ) {
            Text(
                text = translatesState.editableTranslate?.let { "EDIT" } ?: "ADD",
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(vertical = 9.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TranslateInputPreview() {
    TranslateInput(
        translatesState = Translates(
            translates = getPreviewTranslates()
        ),
        onAction = {},
        focusRequesterTranslates = remember { FocusRequester() }
    )
}

@Preview(showBackground = true)
@Composable
fun TranslateInputPreview2() {
    TranslateInput(
        translatesState = Translates(
            translates = getPreviewTranslates(),
            error = ValidateResult(successful = false, errorMessage = "This fields is required")
        ),
        onAction = {},
        focusRequesterTranslates = remember { FocusRequester() }
    )
}

@Preview(showBackground = true)
@Composable
fun TranslateInputPreview3() {
    TranslateInput(
        translatesState = Translates(
            translates = getPreviewTranslates(),
            editableTranslate = Translate(
                id = 0L,
                localId = 0L,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                value = "Green",
                isHidden = false,
            )
        ),
        onAction = {},
        focusRequesterTranslates = remember { FocusRequester() }
    )
}