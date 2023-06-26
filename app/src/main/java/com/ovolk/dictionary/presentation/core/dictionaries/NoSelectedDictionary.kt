package com.ovolk.dictionary.presentation.core.dictionaries

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R

@Composable
fun NoSelectedDictionary() {
    NoDictionaryBase(
        onPressAddNewDictionary = null,
        buttonText = null,
        imageDescription = stringResource(id = R.string.no_selected_dictionary_cd),
        title = stringResource(id = R.string.no_selected_dictionary_select_dictionary_message),
        description = stringResource(id = R.string.no_selected_dictionary_select_dictionary_description),
    )
}

@Composable
@Preview(showBackground = true)
fun NoSelectedDictionaryPreview() {
    NoSelectedDictionary()
}