package com.ovolk.dictionary.presentation.modify_word

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.compose.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.list_full.components.getPreviewTranslates
import com.ovolk.dictionary.presentation.modify_word.compose.alerts.AddToList
import com.ovolk.dictionary.presentation.modify_word.compose.languages_picker.LanguagesPicker
import com.ovolk.dictionary.presentation.modify_word.compose.hints.HintPart
import com.ovolk.dictionary.presentation.modify_word.compose.hints.getPreviewHints
import com.ovolk.dictionary.presentation.modify_word.compose.translates.TranslatePart

@Composable
fun ModifyWordScreen(
    state: ComposeState,
    languageState: Languages,
//    translatesState: Translates,
//    hintState: Hints,
    onAction: (ModifyWordAction) -> Unit,
    onTranslateAction: (ModifyWordTranslatesAction) -> Unit,
    onHintAction: (ModifyWordHintsAction) -> Unit,
) {
    Log.d("XXXX", "ModifyWordScreen")
    // TODO add header
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(id = R.dimen.gutter))
    ) {
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
            isError = !state.englishWordError.successful
        )

        OutlinedErrableTextField(
            value = state.transcriptionWord,
            onValueChange = { value -> onAction(ModifyWordAction.OnChangeEnglishTranscription(value)) },
            label = { Text(text = stringResource(id = R.string.modify_word_transcription)) },
        )


//        TranslatePart(translatesState = translatesState, onAction = onAction)
//
//        OutlinedTextField(
//            value = state.descriptionWord,
//            onValueChange = { value -> onAction(ModifyWordAction.OnChangeDescription(value)) },
//            label = { Text(text = stringResource(id = R.string.modify_word_description)) },
//            modifier = Modifier
//                .height(100.dp)
//                .fillMaxWidth(1f)
//        )
//
//        // TODO audio
//
//        OutlinedErrableTextField(
//            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gutter)),
//            value = state.priorityValue,
//            onValueChange = { value -> onAction(ModifyWordAction.OnChangePriority(value)) },
//            label = { Text(text = stringResource(id = R.string.modify_word_priority)) },
//            errorMessage = state.priorityError.errorMessage,
//            isError = !state.priorityError.successful,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
//
//        AddToList(
//            state = state,
//            addNewList = { title: String -> onAction(ModifyWordAction.AddNewList(title)) },
//            onSelectList = { id: Long -> onAction(ModifyWordAction.OnSelectList(id)) },
//            onAction = onAction
//        )
//
//        Text(
//            text = stringResource(id = R.string.modify_word_additional),
//            modifier = Modifier.clickable { onAction(ModifyWordAction.ToggleVisibleAdditionalPart) },
//            color = colorResource(id = R.color.blue_2)
//        )
//
//        if (state.isAdditionalFieldVisible) {
//            HintPart(hintsState = hintState, onAction = onAction)
//        }
//
//
//        Button(onClick = { onAction(ModifyWordAction.OnPressSaveWord) }) {
//            Text(text = "Save")
//        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ModifyWordScreenPreview() {
//    ModifyWordScreen(
//        state = ComposeState(isAdditionalFieldVisible = true),
//        languageState = Languages(),
//        translatesState = Translates(
//            translates = getPreviewTranslates()
//        ),
//        hintState = Hints(hints = getPreviewHints()),
//        onAction = {}
//    )
//}