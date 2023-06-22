package com.ovolk.dictionary.presentation.lists.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun NoSelectedDictionary() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_list),
            contentDescription = stringResource(id = R.string.cd_nothing_found),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter))
        )
        Text(
            text = "Please select dictionary",
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
            fontSize = 20.sp,
            color = colorResource(id = R.color.grey_2),
            textAlign = TextAlign.Center,
        )

        Text(
            text = "Each list must be attached to the dictionary",
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
            color = colorResource(id = R.color.grey_2),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true)
fun NoSelectedDictionaryPreview() {
    NoSelectedDictionary()
}