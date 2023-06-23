package com.ovolk.dictionary.presentation.dictionary_words.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.presentation.core.SearchBar
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.dropdown_menu.ShowMoreIconsDropdownMenu
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.core.word_item.WordItem
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsAction
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsState
import com.ovolk.dictionary.presentation.settings_dictionaries.SettingsDictionariesAction
import com.ovolk.dictionary.presentation.word_list.components.NothingFound
import com.ovolk.dictionary.presentation.word_list.components.WordListIsEmpty
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewWordList

@Composable
fun DictionaryWords(
    state: DictionaryWordsState, onAction: (DictionaryWordsAction) -> Unit, goBack: () -> Unit
) {

    if (state.isDeleteConfirmModalOpen) {
        DeleteDictionaryDialog(
            dictionaryTitle = state.dictionary?.title ?: "unkown",
            onConfirmDelete = { onAction(DictionaryWordsAction.OnPressConfirmDelete) },
            onDecline = { onAction(DictionaryWordsAction.HandleDeleteDictionaryModal(false)) },
        )
    }


    Scaffold(
        floatingActionButton = {
            if (state.loadingStatus == LoadingState.SUCCESS && state.totalWordListSize != 0) {
                AddButton(
                    onClick = { onAction(DictionaryWordsAction.OnPressAddNewWord) },
                    contentDescription = "add new word"
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
                // TODO
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
                isSelected = false,
            ),
            totalWordListSize = getPreviewWordList().size,
            filteredWordList = getPreviewWordList(),
            loadingStatus = LoadingState.SUCCESS
        ),
        onAction = {},
        goBack = {},
    )
}