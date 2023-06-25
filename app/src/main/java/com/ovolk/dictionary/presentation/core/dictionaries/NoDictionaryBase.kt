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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R

@Composable
fun NoDictionaryBase(
    onPressAddNewDictionary: (() -> Unit)?,
    buttonText: String?,
    imageDescription: String = "",
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_list),
            contentDescription = imageDescription,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter))
        )
        Text(
            text = title,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
            fontSize = 20.sp,
            color = colorResource(id = R.color.grey_2),
            textAlign = TextAlign.Center,
        )
        Text(
            text = description,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
            color = colorResource(id = R.color.grey_2),
            textAlign = TextAlign.Center
        )

        if (buttonText != null && onPressAddNewDictionary != null) {
            Button(onClick = onPressAddNewDictionary) {
                Text(
                    text = stringResource(id = R.string.create_dictionary).uppercase(),
                    color = Color.White
                )
            }
        }
    }
}
