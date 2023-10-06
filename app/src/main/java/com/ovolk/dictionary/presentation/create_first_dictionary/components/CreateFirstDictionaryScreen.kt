package com.ovolk.dictionary.presentation.create_first_dictionary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.core.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.create_first_dictionary.FirstDictionaryAction
import com.ovolk.dictionary.presentation.create_first_dictionary.FirstDictionaryState
import com.ovolk.dictionary.presentation.modify_dictionary.components.LanguagesPicker

@Composable
fun CreateFirstDictionaryScreen(
    state: FirstDictionaryState,
    onAction: (FirstDictionaryAction) -> Unit
) {
    Column {
        Header(
            title = stringResource(id = R.string.create_first_dictionary_title),
            withBackButton = false
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                OutlinedErrableTextField(
                    value = state.dictionaryName,
                    onValueChange = { onAction(FirstDictionaryAction.OnInputTitle(it)) },
                    label = { Text(text = stringResource(id = R.string.create_first_dictionary_title_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !state.dictionaryValidation.successful,
                    errorMessage = state.dictionaryValidation.errorMessage
                )


                Box(modifier = Modifier.padding(top = 40.dp).align(Alignment.CenterHorizontally)) {
                    LanguagesPicker(
                        languageToName = state.languageToCode,
                        languageFromName = state.languageFromCode,
                        langFromValidation = state.langFromValidation,
                        langToValidation = state.langToValidation,
                        openLanguageBottomSheet = { languageType ->
                            onAction(FirstDictionaryAction.OpenLanguageBottomSheet(languageType))
                        }
                    )
                }
            }

            Button(
                onClick = { onAction(FirstDictionaryAction.SaveDictionary) },
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.save).uppercase(),
                    color = colorResource(id = R.color.white)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateFirstDictionaryScreenPreview() {
    CreateFirstDictionaryScreen(
        state = FirstDictionaryState(),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CreateFirstDictionaryScreenPreviewWithError() {
    CreateFirstDictionaryScreen(
        state = FirstDictionaryState(
            dictionaryValidation = ValidateResult(
                successful = false,
                errorMessage = "this field is required"
            ),
            langFromValidation = ValidateResult(
                successful = false,
                errorMessage = "this field is required"
            ),
            langToValidation = ValidateResult(
                successful = false,
                errorMessage = "this field is required"
            ),
        ),
        onAction = {}
    )
}