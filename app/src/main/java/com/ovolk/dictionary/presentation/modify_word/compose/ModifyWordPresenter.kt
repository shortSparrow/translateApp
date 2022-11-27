package com.ovolk.dictionary.presentation.modify_word.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.dialog.InfoDialog
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.modify_word.*
import com.ovolk.dictionary.presentation.modify_word.compose.alerts.AddToList
import com.ovolk.dictionary.presentation.modify_word.compose.hints.HintPart
import com.ovolk.dictionary.presentation.modify_word.compose.languages_picker.LanguagesPicker
import com.ovolk.dictionary.presentation.modify_word.compose.question_wrapper.QuestionWrapper
import com.ovolk.dictionary.presentation.modify_word.compose.record_audio.RecordAudioWrapper
import com.ovolk.dictionary.presentation.modify_word.compose.text_fields.TextFieldDescription
import com.ovolk.dictionary.presentation.modify_word.compose.text_fields.TextFieldEnglishWord
import com.ovolk.dictionary.presentation.modify_word.compose.text_fields.TextFieldPriority
import com.ovolk.dictionary.presentation.modify_word.compose.text_fields.TextFieldTranscription
import com.ovolk.dictionary.presentation.modify_word.compose.translates.TranslatePart
import com.ovolk.dictionary.util.compose.click_effects.opacityClick
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewHints
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewTranslates


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ModifyWordPresenter(
    state: ComposeState,
    languageState: Languages,
    translateState: Translates,
    hintState: Hints,
    recordState: RecordAudioState,
    onRecordAction: (RecordAudioAction) -> Unit,
    onAction: (ModifyWordAction) -> Unit,
    onTranslateAction: (ModifyWordTranslatesAction) -> Unit,
    onHintAction: (ModifyWordHintsAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSourceScreen = remember { MutableInteractionSource() }

    val headerTitle = if (state.modifyMode == ModifyWordModes.MODE_EDIT) {
        remember { state.englishWord }
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

    if (state.isOpenUnsavedChanges) {
        ConfirmDialog(
            question = stringResource(id = R.string.modify_word_unsaved_changes),
            onAcceptClick = { onAction(ModifyWordAction.GoBack(false)) },
            onDeclineClick = { onAction(ModifyWordAction.ToggleUnsavedChanges) })
    }

    if (state.isFieldDescribeModalOpen) {
        InfoDialog(
            onDismissRequest = { onAction(ModifyWordAction.ToggleFieldDescribeModalOpen("")) },
            message = state.fieldDescribeModalQuestion,
            onClick = { onAction(ModifyWordAction.ToggleFieldDescribeModalOpen("")) },
            buttonText = stringResource(id = R.string.ok).uppercase(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }

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
                onBackButtonClick = { onAction(ModifyWordAction.GoBack()) },
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

                TextFieldEnglishWord(
                    englishWord = state.englishWord,
                    englishWordError = state.englishWordError,
                    onAction = onAction
                )
                TextFieldTranscription(
                    transcriptionWord = state.transcriptionWord,
                    onAction = onAction
                )

                val describeTranslateField =
                    stringResource(id = R.string.modify_word_field_describe_translation)
                QuestionWrapper(onAction = onAction, question = describeTranslateField) {
                    TranslatePart(translateState = translateState, onAction = onTranslateAction)
                }

                TextFieldDescription(
                    descriptionWord = state.descriptionWord,
                    onAction = onAction
                )

                RecordAudioWrapper(
                    word = state.englishWord,
                    recordState = recordState,
                    onAction = onRecordAction
                )

                val describePriorityField =
                    stringResource(id = R.string.modify_word_field_describe_priority)
                QuestionWrapper(onAction = onAction, question = describePriorityField) {
                    TextFieldPriority(
                        priorityValue = state.priorityValue,
                        priorityError = state.priorityError,
                        onAction = onAction
                    )
                }

                val describeListsField =
                    stringResource(id = R.string.modify_word_field_describe_lists)
                QuestionWrapper(
                    onAction = onAction,
                    absolutePosition = false,
                    question = describeListsField
                ) {
                    AddToList(state = state, onAction = onAction)
                }

                Text(
                    text = stringResource(id = R.string.modify_word_additional),
                    modifier = Modifier
                        .padding(bottom = 10.dp, top = 20.dp)
                        .opacityClick(
                            isDisabled = false,
                            onClick = { onAction(ModifyWordAction.ToggleVisibleAdditionalPart) }),
                    color = colorResource(id = R.color.blue)
                )

                if (state.isAdditionalFieldVisible) {
                    val describeHintField =
                        stringResource(id = R.string.modify_word_field_describe_hint)
                    QuestionWrapper(onAction = onAction, question = describeHintField) {
                        HintPart(hintsState = hintState, onAction = onHintAction)
                    }
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
    ModifyWordPresenter(
        state = ComposeState(isAdditionalFieldVisible = true),
        languageState = Languages(),
        translateState = Translates(
            translates = getPreviewTranslates()
        ),
        hintState = Hints(hints = getPreviewHints()),
        recordState = RecordAudioState(),
        onRecordAction = {},
        onAction = {},
        onHintAction = {},
        onTranslateAction = {},
    )
}