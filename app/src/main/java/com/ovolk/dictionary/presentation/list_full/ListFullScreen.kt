package com.ovolk.dictionary.presentation.list_full

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.core.compose.floating.AddButton
import com.ovolk.dictionary.presentation.list_full.components.Header
import com.ovolk.dictionary.presentation.list_full.components.SearchBar
import com.ovolk.dictionary.presentation.list_full.components.WordItem

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListFullScreen(
    state: ListFullState,
    onAction: (ListFullAction) -> Unit
) {
//    val activity = LocalContext.current.getActivity<MainActivity>()
//    if (state.wordList.isEmpty() && !state.noAnyWords && state.loadingStatusWordList == LoadingState.SUCCESS) {
//        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
//    } else {
//        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//    }

    Scaffold(
        floatingActionButton = {
            if (state.wordList.isNotEmpty() && state.loadingStatusWordList == LoadingState.SUCCESS) {
                AddButton(
                    onClick = { onAction(ListFullAction.AddNewWord) },
                    contentDescription = "add new list"
                )
            }
        }
    ) {
        Column {
            Header(onAction = onAction, listName=state.listName)

            if (state.noAnyWords && state.loadingStatusWordList == LoadingState.SUCCESS) {
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

                       Button(onClick = { onAction(ListFullAction.AddNewWord) }) {
                           Text(
                               text = stringResource(id = R.string.full_lists_add_new_word).uppercase(),
                               color = Color.White
                           )
                       }
                   }
                }
            }

            if (state.loadingStatusWordList == LoadingState.SUCCESS) {
                Box(Modifier.padding(horizontal = 20.dp)) {
                    SearchBar(onAction = onAction)
                }

                if (state.wordList.isEmpty() && !state.noAnyWords) {
                    CompositionLocalProvider(
                        LocalOverscrollConfiguration provides null
                    ) {
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
                                    painter = painterResource(id = R.drawable.nothing_found),
                                    contentDescription = stringResource(id = R.string.nothing_found),
                                    modifier = Modifier
                                        .size(250.dp)
                                        .padding(bottom =  dimensionResource(id = R.dimen.medium_gutter))
                                )
                                Text(text = stringResource(id = R.string.nothing_found), fontSize = 20.sp)
                            }
                        }
                    }

                }
                if (state.wordList.isNotEmpty()) {
                    CompositionLocalProvider(
                        LocalOverscrollConfiguration provides null
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 70.dp)
                        ) {
                            items(state.wordList) { word ->
                                WordItem(word = word, onAction = onAction)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListFullScreen() {
    ListFullScreen(
        onAction = {},
        state = ListFullState(
            wordList = listOf(
                WordRV(
                    id = 1L,
                    langFrom = "EN",
                    langTo = "UA",
                    transcription = "Юнікорн",
                    value = "Unicorn",
                    sound = WordAudio(fileName = "PATH"),
                    translates = listOf(
                        Translate(
                            id = 1L,
                            localId = 1L,
                            createdAt = 13313131L,
                            updatedAt = 13343131L,
                            value = "Єдиноріг",
                            isHidden = false
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),

                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 1L,
                            localId = 1L,
                            createdAt = 13313131L,
                            updatedAt = 13343131L,
                            value = "Єдиноріг",
                            isHidden = false
                        ),
                    ),
                    description = "This is description"
                )
            ),
            loadingStatusWordList = LoadingState.SUCCESS
        ),
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListFullScreenEmpty() {
    ListFullScreen(
        onAction = {},
        state = ListFullState(
            wordList = emptyList(),
            noAnyWords = true,
        ),
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListFullScreenNoMath() {
    ListFullScreen(
        onAction = {},
        state = ListFullState(
            wordList = emptyList(),
            noAnyWords = false,
            loadingStatusWordList = LoadingState.SUCCESS
        ),
    )
}