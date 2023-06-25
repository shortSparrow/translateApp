package com.ovolk.dictionary.presentation.modify_word.compose.hints

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
import com.ovolk.dictionary.presentation.core.text_field.MaxErrorLines
import com.ovolk.dictionary.presentation.core.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.modify_word.Hints
import com.ovolk.dictionary.presentation.modify_word.ModifyWordHintsAction
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewHints

@Composable
fun HintInput(
    hintsState: Hints,
    focusRequesterTranslates: FocusRequester,
    onAction: (ModifyWordHintsAction) -> Unit
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (hintsState.editableHint != null) {
                    Text(
                        modifier = Modifier
                            .clickable { onAction(ModifyWordHintsAction.CancelEditHint) },
                        textAlign = TextAlign.Right,
                        text = stringResource(id = R.string.cd_add_cancel_edit)
                    )
                }
            }

            OutlinedErrableTextField(
                modifier = Modifier.focusRequester(focusRequesterTranslates),
                value = hintsState.hintWord,
                onValueChange = { value -> onAction(ModifyWordHintsAction.OnChangeHint(value)) },
                label = { Text(text = stringResource(id = R.string.modify_word_input_hint)) },
                isError = !hintsState.error.successful,
                errorMessage = hintsState.error.errorMessage,
                maxErrorLines = MaxErrorLines.ONE
            )
        }
        Button(
            onClick = { onAction(ModifyWordHintsAction.OnPressAddHint) },
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp)
                .width(85.dp)
        ) {
            Text(
                text = hintsState.editableHint
                    ?.let { stringResource(id = R.string.edit).uppercase() }
                    ?: stringResource(id = R.string.add).uppercase(),
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(vertical = 9.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HintInputPreview() {
    HintInput(
        hintsState = Hints(
            hints = getPreviewHints(),
            hintWord = "a fruit",
            error = ValidateResult(),
            editableHint = getPreviewHints()[0]
        ),
        onAction = {},
        focusRequesterTranslates = remember { FocusRequester() }
    )
}

@Preview(showBackground = true)
@Composable
fun HintInputPreview2() {
    HintInput(
        hintsState = Hints(
            hints = getPreviewHints(),
            hintWord = "a fruit",
            error = ValidateResult(successful = false, errorMessage = "This fields can't be empty")
        ),
        onAction = {},
        focusRequesterTranslates = remember { FocusRequester() }
    )
}

