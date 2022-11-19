package com.ovolk.dictionary.presentation.word_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.SearchBar
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.core.word_item.WordItem
import com.ovolk.dictionary.presentation.word_list.WordListAction
import com.ovolk.dictionary.presentation.word_list.WordListState
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewWordList


@Composable
fun WordList(state: WordListState, onAction: (WordListAction) -> Unit) {
    Scaffold(
        floatingActionButton = {
            if (!state.isLoading && state.totalWordListSize != 0) {
                AddButton(
                    onClick = { onAction(WordListAction.OnPressAddNewWord) },
                    contentDescription = stringResource(id = R.string.full_lists_cd_add_new_list)
                )
            }
        }
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            if (!state.isLoading && state.totalWordListSize == 0) {
                EmptyDictionary(onAction = onAction)
            }

            if (!state.isLoading && state.totalWordListSize != 0) {
                Box(
                    Modifier.padding(
                        start = dimensionResource(id = R.dimen.gutter),
                        end = dimensionResource(id = R.dimen.gutter),
                        top = 20.dp
                    )
                ) {
                    SearchBar(
                        onSearch = { query -> onAction(WordListAction.SearchWord(query)) },
                        onPressCross = { onAction(WordListAction.SearchWord("")) },
                        searchedValue = state.searchValue
                    )
                }

                if (state.filteredWordList.isEmpty()) {
                    NothingFound()
                }

                LazyColumn {
                    items(state.filteredWordList) { word ->
                        WordItem(
                            word = word,
                            onWordClick = { wordId -> onAction(WordListAction.OnPressWordItem(wordId)) },
                            onPlayAudioClick = { onStartListener, wordItem, onCompletionListener ->
                                onAction(
                                    WordListAction.PlayAudio(
                                        onStartListener = onStartListener,
                                        word = wordItem,
                                        onCompletionListener = onCompletionListener
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordListScreenPreview() {
    WordList(
        state = WordListState(
            filteredWordList = getPreviewWordList(),
            isLoading = false,
            totalWordListSize = 10,
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun WordListScreenPreview2() {
    WordList(
        state = WordListState(
            filteredWordList = emptyList(),
            isLoading = false,
            totalWordListSize = 10
        ),
        onAction = {}
    )
}


@Preview(showBackground = true)
@Composable
fun WordListScreenPreview3() {
    WordList(
        state = WordListState(
            filteredWordList = emptyList(),
            isLoading = false,
            totalWordListSize = 0
        ),
        onAction = {}
    )
}

