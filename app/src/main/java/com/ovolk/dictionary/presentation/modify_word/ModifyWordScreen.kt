package com.ovolk.dictionary.presentation.modify_word

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.compose.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.compose.header.Header
import com.ovolk.dictionary.presentation.core.compose.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.list_full.components.getPreviewTranslates
import com.ovolk.dictionary.presentation.modify_word.compose.alerts.AddToList
import com.ovolk.dictionary.presentation.modify_word.compose.hints.HintPart
import com.ovolk.dictionary.presentation.modify_word.compose.hints.getPreviewHints
import com.ovolk.dictionary.presentation.modify_word.compose.languages_picker.LanguagesPicker
import com.ovolk.dictionary.presentation.modify_word.compose.translates.TranslatePart
import com.ovolk.dictionary.util.compose.click_effects.opacityClick


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ModifyWordScreen(
    state: ComposeState,
    languageState: Languages,
    translateState: Translates,
    hintState: Hints,
    onAction: (ModifyWordAction) -> Unit,
    onTranslateAction: (ModifyWordTranslatesAction) -> Unit,
    onHintAction: (ModifyWordHintsAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSourceScreen = remember { MutableInteractionSource() }

    val headerTitle = if (state.modifyMode == ModifyWordModes.MODE_EDIT) {
        state.englishWord
    } else {
        stringResource(id = R.string.modify_word_add_new_word)
    }
    val firstRightIcon: (@Composable () -> Unit)? =
        if (state.modifyMode == ModifyWordModes.MODE_EDIT) {
            {
                Icon(
                    painter = painterResource(R.drawable.delete_active),
                    stringResource(id = R.string.delete),
                    tint = colorResource(R.color.red),
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        } else {
            null
        }

    if (state.isOpenDeleteWordModal) {
        ConfirmDialog(
            question = stringResource(id = R.string.modify_word_confirm_delete_title),
            onAcceptClick = { onAction(ModifyWordAction.DeleteWord) },
            onDeclineClick = { onAction(ModifyWordAction.ToggleDeleteModalOpen) })
    }


    // TODO add handler on back click (alert)

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .clickable(
                interactionSource = interactionSourceScreen,
                indication = null
            ) { keyboardController?.hide() }) {
            Header(
                title = headerTitle,
                withBackButton = true,
                firstRightIcon = firstRightIcon,
                onFirstRightIconClick = { onAction(ModifyWordAction.ToggleDeleteModalOpen) },
            )

            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(1f)
                ) {
                    LanguagesPicker(state = languageState, onAction = onAction)
                }

                OutlinedErrableTextField(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gutter)),
                    value = state.englishWord,
                    onValueChange = { value -> onAction(ModifyWordAction.OnChangeEnglishWord(value)) },
                    label = { Text(text = stringResource(id = R.string.modify_word_english_word_hint)) },
                    errorMessage = state.englishWordError.errorMessage,
                    isError = !state.englishWordError.successful,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    value = state.transcriptionWord,
                    onValueChange = { value ->
                        onAction(ModifyWordAction.OnChangeEnglishTranscription(value))
                    },
                    label = { Text(text = stringResource(id = R.string.modify_word_transcription)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                )

                TranslatePart(translateState = translateState, onAction = onTranslateAction)

                OutlinedTextField(
                    value = state.descriptionWord,
                    onValueChange = { value -> onAction(ModifyWordAction.OnChangeDescription(value)) },
                    label = { Text(text = stringResource(id = R.string.modify_word_description)) },
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(1f),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                )

                // TODO audio

                OutlinedErrableTextField(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gutter)),
                    value = state.priorityValue,
                    onValueChange = { value -> onAction(ModifyWordAction.OnChangePriority(value)) },
                    label = { Text(text = stringResource(id = R.string.modify_word_priority)) },
                    errorMessage = state.priorityError.errorMessage,
                    isError = !state.priorityError.successful,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                )

                AddToList(
                    state = state,
                    addNewList = { title: String -> onAction(ModifyWordAction.AddNewList(title)) },
                    onSelectList = { id: Long -> onAction(ModifyWordAction.OnSelectList(id)) },
                    onAction = onAction
                )

                Text(
                    text = stringResource(id = R.string.modify_word_additional),
                    modifier = Modifier
                        .padding(bottom = 0.dp, top = 20.dp)
                        .opacityClick(onClick = { onAction(ModifyWordAction.ToggleVisibleAdditionalPart) }),
                    color = colorResource(id = R.color.blue)
                )

                if (state.isAdditionalFieldVisible) {
                    HintPart(hintsState = hintState, onAction = onHintAction)
                }

                Button(
                    onClick = { onAction(ModifyWordAction.OnPressSaveWord) },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.save).uppercase(),
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ModifyWordScreenPreview() {
    ModifyWordScreen(
        state = ComposeState(isAdditionalFieldVisible = true),
        languageState = Languages(),
        translateState = Translates(
            translates = getPreviewTranslates()
        ),
        hintState = Hints(hints = getPreviewHints()),
        onAction = {},
        onHintAction = {},
        onTranslateAction = {}
    )
}