package com.ovolk.dictionary.presentation.word_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.word_list.WordListAction

@Composable
fun EmptyDictionary(onAction: (WordListAction) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = dimensionResource(id = R.dimen.large_gutter)
        ),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.empty_list),
                contentDescription = stringResource(id = R.string.full_lists_cd_list_is_empty),
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter))
            )
            Text(
                text = stringResource(id = R.string.full_lists_list_is_empty),
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
                fontSize = 20.sp,
                color = colorResource(id = R.color.grey_2)
            )

            Button(onClick = { onAction(WordListAction.OnPressAddNewWord) }) {
                Text(
                    text = stringResource(id = R.string.word_list_add_first_word).uppercase(),
                    color = Color.White
                )
            }
        }
    }
}


@Preview
@Composable
fun EmptyDictionaryPreview() {
    EmptyDictionary(onAction = {})
}