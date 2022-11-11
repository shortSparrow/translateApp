package com.ovolk.dictionary.presentation.modify_word.compose.text_fields

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.compose.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.modify_word.ComposeState
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction

@Composable
fun TextFieldEnglishWord(
    englishWord: String,
    englishWordError: ValidateResult,
    onAction: (ModifyWordAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedErrableTextField(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gutter)),
        value = englishWord,
        onValueChange = { value -> onAction(ModifyWordAction.OnChangeEnglishWord(value)) },
        label = { Text(text = stringResource(id = R.string.modify_word_english_word_hint)) },
        errorMessage = englishWordError.errorMessage,
        isError = !englishWordError.successful,
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

@Composable
@Preview(showBackground = true)
fun TextFieldEnglishWordPreview() {
    TextFieldEnglishWord(englishWord = "", englishWordError = ValidateResult(), onAction = {})
}