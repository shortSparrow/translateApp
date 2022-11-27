package com.ovolk.dictionary.presentation.word_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
fun NothingFound() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.nothing_found),
            contentDescription = stringResource(id = R.string.nothing_found),
            modifier = Modifier
                .size(250.dp)
                .padding(dimensionResource(id = R.dimen.gutter))
        )
        Text(
            text = stringResource(id = R.string.nothing_found),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.gutter)),
            fontSize = 18.sp,
            color = colorResource(id = R.color.grey),
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true)
@Composable
fun NothingFoundPreview() {
    NothingFound()
}