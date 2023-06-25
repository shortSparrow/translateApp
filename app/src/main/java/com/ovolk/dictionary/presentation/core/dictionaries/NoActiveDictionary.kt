package com.ovolk.dictionary.presentation.core.dictionaries

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R

@Composable
fun NoActiveDictionary(onPressAddNewDictionary: () -> Unit) {
    NoDictionaryBase(
        onPressAddNewDictionary = { onPressAddNewDictionary() },
        buttonText = stringResource(id = R.string.create_dictionary).uppercase(),
        imageDescription = stringResource(id = R.string.no_active_dictionary_image_cd),
        title = stringResource(id = R.string.no_active_dictionary_message),
        description = stringResource(id = R.string.no_active_dictionary_description),
    )
}

@Composable
@Preview(showBackground = true)
fun NoActiveDictionaryPreview() {
    NoActiveDictionary(onPressAddNewDictionary = {})
}