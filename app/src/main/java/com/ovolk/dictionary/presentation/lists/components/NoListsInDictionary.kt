package com.ovolk.dictionary.presentation.lists.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R

@Composable
fun NoListsInDictionary(onPressAddNewList: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_list),
            contentDescription = stringResource(id = R.string.lists_screen_no_list_for_dictionary),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter))
        )
        Text(
            text = stringResource(id = R.string.lists_screen_no_list_for_dictionary),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
            fontSize = 20.sp,
            color = colorResource(id = R.color.grey_2)
        )

        Button(onClick = onPressAddNewList) {
            Text(
                text = stringResource(id = R.string.lists_screen_add_new_list).uppercase(),
                color = Color.White
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NoListsInDictionaryPreview() {
    NoListsInDictionary(onPressAddNewList = {})
}