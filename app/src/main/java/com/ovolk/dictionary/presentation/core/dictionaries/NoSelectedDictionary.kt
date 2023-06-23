package com.ovolk.dictionary.presentation.core.dictionaries

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoSelectedDictionary() {
    NoDictionaryBase(
        onPressAddNewDictionary = null,
        buttonText = null,
        imageDescription = "there is no selected dictionary",
        title = "Please select dictionary",
        description = "Each list must be attached to the dictionary",
    )
}

@Composable
@Preview(showBackground = true)
fun NoSelectedDictionaryPreview() {
    NoSelectedDictionary()
}