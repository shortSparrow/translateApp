package com.ovolk.dictionary.presentation.modify_word.compose.translates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.core.text_field.MaxErrorLines
import com.ovolk.dictionary.presentation.core.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.modify_word.ModifyWordTranslatesAction
import com.ovolk.dictionary.presentation.modify_word.Translates
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewTranslates

@Composable
fun TranslateInput(
    translatesState: Translates,
    focusRequesterTranslates: FocusRequester,
    onAction: (ModifyWordTranslatesAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val maxErrorLines = if (translatesState.translates.isEmpty()) {
        MaxErrorLines.TWO
    } else {
        MaxErrorLines.ONE
    }

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
                        text = stringResource(id = R.string.modify_word_cancel_edit)
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
                label = {
                    Text(
                        text = stringResource(id = R.string.modify_word_input_translate),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                isError = !translatesState.error.successful,
                errorMessage = translatesState.error.errorMessage,
                maxErrorLines = maxErrorLines,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
        }
        Button(
            onClick = { onAction(ModifyWordTranslatesAction.OnPressAddTranslate) },
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp)
                .widthIn(85.dp)

        ) {
            Text(
                text = translatesState.editableTranslate?.let { stringResource(id = R.string.edit).uppercase() }
                    ?: stringResource(id = R.string.add).uppercase(),
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