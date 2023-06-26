package com.ovolk.dictionary.presentation.modify_dictionary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.dialog.info_dialog.InfoDialog
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.core.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryAction
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryState


@Composable
fun ModifyDictionaryPresenter(
    state: ModifyDictionaryState,
    onAction: (ModifyDictionaryAction) -> Unit,
    goBack: () -> Unit,
) {

    if (state.dictionaryAlreadyExistModelOpen) {
        fun closeDictionary() = onAction(
            ModifyDictionaryAction.ToggleOpenDictionaryAlreadyExistModal(
                false
            )
        )

        InfoDialog(
            onDismissRequest = ::closeDictionary,
            message = stringResource(
                id = R.string.modify_dictionary_dictionary_already_exist_title,
                state.dictionaryName
            ),
            onClick = ::closeDictionary,
            buttonText = stringResource(id = R.string.ok)
        )
    }

    SelectLanguageBottomSheet(
        isBottomSheetOpen = state.languageBottomSheet.isOpen,
        languageList = state.languageBottomSheet.languageList,
        preferredLanguages = emptyList(),
        onSelectLanguage = { langCode ->
            onAction(ModifyDictionaryAction.OnSelectLanguage(languageCode = langCode))
        },
        onSearchLanguage = { value -> onAction(ModifyDictionaryAction.OnSearchLanguage(value)) },
        closeLanguageBottomSheet = { onAction(ModifyDictionaryAction.CloseLanguageBottomSheet) },
        body = {
            Column {
                Header(
                    title = state.screenTitle,
                    withBackButton = true,
                    onBackButtonClick = goBack
                )
                if (state.loadingState == LoadingState.PENDING) {
                    return@SelectLanguageBottomSheet CircularProgressIndicator()
                }

                if (state.loadingError != null) {
                    return@SelectLanguageBottomSheet Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = state.loadingError,
                            color = colorResource(id = R.color.red),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        OutlinedErrableTextField(
                            value = state.dictionaryName,
                            onValueChange = { onAction(ModifyDictionaryAction.OnInputTitle(it)) },
                            label = { Text(text = stringResource(id = R.string.modify_dictionary_dictionary_name_placeholder)) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = !state.dictionaryNameValidation.successful,
                            errorMessage = state.dictionaryNameValidation.errorMessage,
                        )


                        Box(modifier = Modifier.padding(top = 40.dp)) {
                            LanguagesPicker(
                                languageToName = state.languageToCode,
                                languageFromName = state.languageFromCode,
                                langFromValidation = state.langFromValidation,
                                langToValidation = state.langToValidation,
                                openLanguageBottomSheet = { languageType ->
                                    onAction(
                                        ModifyDictionaryAction.OpenLanguageBottomSheet(languageType)
                                    )
                                }
                            )
                        }
                    }

                    Button(
                        onClick = { onAction(ModifyDictionaryAction.SaveDictionary) },
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(text = stringResource(id = R.string.save), color = colorResource(id = R.color.white))
                    }
                }
            }
        },
    )

}

@Preview(showBackground = true)
@Composable
fun ModifyDictionaryPresenterPreview() {
    ModifyDictionaryPresenter(
        state = ModifyDictionaryState(loadingState = LoadingState.SUCCESS),
        onAction = {},
        goBack = {},
    )
}

@Preview(showBackground = true)
@Composable
fun ModifyDictionaryPresenterPreview2() {
    ModifyDictionaryPresenter(
        state = ModifyDictionaryState(
            loadingState = LoadingState.SUCCESS,
            dictionaryNameValidation = ValidateResult(
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
        onAction = {},
        goBack = {},
    )
}

@Preview(showBackground = true)
@Composable
fun ModifyDictionaryPresenterPreview3() {
    ModifyDictionaryPresenter(
        state = ModifyDictionaryState(
            loadingState = LoadingState.FAILED,
            loadingError = "Can't load this fucking dictionary"
        ),
        onAction = {},
        goBack = {},
    )
}

