package com.ovolk.dictionary.presentation.core.dictionaries

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R

@Composable
fun NoActiveDictionary(onPressAddNewDictionary: () -> Unit) {
    NoDictionaryBase(
        onPressAddNewDictionary = { onPressAddNewDictionary() },
        buttonText = "create dictionary".uppercase(),
        imageDescription = "there is not active dictionary",
        title = "Looks lie you don't have active dictionary",
        description = "At first create dictionary where you can put your lists",
    )
}

@Composable
@Preview(showBackground = true)
fun NoActiveDictionaryPreview() {
    NoActiveDictionary(onPressAddNewDictionary = {})
}