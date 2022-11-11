package com.ovolk.dictionary.presentation.modify_word.compose.text_fields

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.SimpleError
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.compose.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.modify_word.ComposeState
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldPriority(priorityValue: String,priorityError: ValidateResult , onAction: (ModifyWordAction) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedErrableTextField(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gutter)),
        value = priorityValue,
        onValueChange = { value -> onAction(ModifyWordAction.OnChangePriority(value)) },
        label = { Text(text = stringResource(id = R.string.modify_word_priority)) },
        errorMessage = priorityError.errorMessage,
        isError = !priorityError.successful,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
    )
}

@Composable
@Preview(showBackground = true)
fun TextFieldPriorityPreview() {
    TextFieldPriority(priorityValue = "", priorityError = ValidateResult(), onAction = {})
}