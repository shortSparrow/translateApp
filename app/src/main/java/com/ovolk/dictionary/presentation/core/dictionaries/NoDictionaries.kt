package com.ovolk.dictionary.presentation.core.dictionaries

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoDictionaries(onPressAddNewDictionary: () -> Unit) {
    NoDictionaryBase(
        onPressAddNewDictionary = { onPressAddNewDictionary() },
        buttonText = "create dictionary".uppercase(),
        imageDescription = "there are not any dictionary",
        title = "Looks lie you don't have any dictionary",
        description = "At first create dictionary where you can put your lists",
    )
}

@Composable
@Preview(showBackground = true)
fun NoDictionariesPreview() {
    NoDictionaries(onPressAddNewDictionary = {})
}