package com.ovolk.dictionary.presentation.dictionary_words.components

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
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.presentation.core.SearchBar
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.core.word_item.WordItem
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsAction
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsState
import com.ovolk.dictionary.presentation.word_list.components.NothingFound
import com.ovolk.dictionary.presentation.word_list.components.WordListIsEmpty
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewWordList

@Composable
fun DictionaryWords(
    state: DictionaryWordsState, onAction: (DictionaryWordsAction) -> Unit, goBack: () -> Unit
) {

    if (state.isDeleteConfirmModalOpen) {
        DeleteDictionaryDialog(
            isDictionaryActive = state.dictionary?.isActive == true,
            dictionaryTitle = state.dictionary?.title ?: stringResource(id = R.string.unknown),
            onConfirmDelete = { onAction(DictionaryWordsAction.OnPressConfirmDelete) },
            onDecline = { onAction(DictionaryWordsAction.HandleDeleteDictionaryModal(false)) },
        )
    }


    Scaffold(
        floatingActionButton = {
            if (state.loadingStatus == LoadingState.SUCCESS && state.totalWordListSize != 0) {
                AddButton(
                    onClick = { onAction(DictionaryWordsAction.OnPressAddNewWord) },
                    contentDescription = stringResource(id = R.string.cd_add_new_word_to_dictionary)
                )
            }
        },
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            DictionaryHeader(
                isActive = state.dictionary?.isActive == true,
                dictionaryTitle = state.dictionary?.title,
                onBackButtonClick = { goBack() },
                onAction = onAction,
            )
            if (state.loadingStatus == LoadingState.FAILED) {
                FailedLoadDictionary()
            }

            if (state.loadingStatus == LoadingState.SUCCESS && state.totalWordListSize == 0) {
                WordListIsEmpty(onPressAddNewWord = { onAction(DictionaryWordsAction.OnPressAddNewWord) })
            }

            if (state.loadingStatus == LoadingState.SUCCESS) {
                Box(
                    Modifier.padding(
                        start = dimensionResource(id = R.dimen.gutter),
                        end = dimensionResource(id = R.dimen.gutter),
                        top = 20.dp
                    )
                ) {
                    SearchBar(
                        onSearch = { query -> onAction(DictionaryWordsAction.OnSearchWord(query)) },
                        onPressCross = { onAction(DictionaryWordsAction.OnSearchWord("")) },
                        searchedValue = state.searchValue
                    )
                }

                if (state.filteredWordList.isEmpty() && state.totalWordListSize != 0) {
                    NothingFound()
                }

                if (state.filteredWordList.isEmpty() && state.totalWordListSize == 0) {
                    WordListIsEmpty(onPressAddNewWord = { onAction(DictionaryWordsAction.OnPressAddNewWord) })
                }

                LazyColumn {
                    items(items = state.filteredWordList) { word ->
                        WordItem(
                            word = word,
                            onWordClick = { wordId ->
                                onAction(DictionaryWordsAction.OnPressWordItem(wordId))
                            },
                            onPlayAudioClick = { onStartListener, wordItem, onCompletionListener ->
                                onAction(
                                    DictionaryWordsAction.PlayAudio(
                                        onStartListener = onStartListener,
                                        word = wordItem,
                                        onCompletionListener = onCompletionListener
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DictionaryWordsPreview() {
    DictionaryWords(
        state = DictionaryWordsState(
            dictionary = Dictionary(
                id = 1L,
                title = "EN-UK",
                langToCode = "UK",
                langFromCode = "EN",
                isActive = true,
            ),
            totalWordListSize = getPreviewWordList().size,
            filteredWordList = getPreviewWordList(),
            loadingStatus = LoadingState.SUCCESS
        ),
        onAction = {},
        goBack = {},
    )
}

@Preview(showBackground = true)
@Composable
fun DictionaryWordsPreview2() {
    DictionaryWords(
        state = DictionaryWordsState(
            loadingStatus = LoadingState.FAILED
        ),
        onAction = {},
        goBack = {},
    )
}