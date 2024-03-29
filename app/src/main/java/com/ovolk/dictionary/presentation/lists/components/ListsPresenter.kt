package com.ovolk.dictionary.presentation.lists.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.dictionaries.DictionaryPicker
import com.ovolk.dictionary.presentation.core.dictionaries.NoDictionariesFoLists
import com.ovolk.dictionary.presentation.core.dictionaries.NoSelectedDictionary
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.core.scrollableWrapper.ScrollableWrapperScreen
import com.ovolk.dictionary.presentation.lists.ListsAction
import com.ovolk.dictionary.presentation.lists.ListsState
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ListsPresenter(
    state: ListsState,
    onAction: (ListsAction) -> Unit,
) {
    val selectedLists = state.list.groupBy { it.isSelected }[true]
    val atLeastOneListSelected = selectedLists != null
    val currentDictionary = state.currentDictionary.collectAsState()
    var expanded by remember {
        mutableStateOf(false)
    }

    if (state.modalList.isOpen) {
        DialogAddNewList(
            modalListState = state.modalList,
            modalError = state.modalError,
            onAction = onAction
        )
    }

    if (state.isOpenDeleteListModal) {
        val count = state.list.filter { it.isSelected }.size
        ConfirmDialog(
            title = pluralStringResource(
                id = R.plurals.lists_screen_confirm_delete_list_title,
                count = count
            ),
            description = pluralStringResource(
                id = R.plurals.lists_screen_confirm_delete_list_description,
                count = count
            ),
            descriptionColor = colorResource(id = R.color.red),
            onAcceptClick = { onAction(ListsAction.ConfirmDeleteSelectedLists) },
            onDeclineClick = { onAction(ListsAction.DeclineDeleteSelectedLists) })
    }

    val closeDictionaryPickerModifier = Modifier.pointerInteropFilter {
        if (expanded) {
            expanded = false
        }
        false
    }
    Scaffold(
        floatingActionButton = {
            if (state.list.isNotEmpty()
                && state.isLoadingList == LoadingState.SUCCESS
                && currentDictionary.value != null
            ) {
                AddButton(
                    onClick = { onAction(ListsAction.OpenModalNewList) },
                    contentDescription = stringResource(id = R.string.lists_screen_cd_add_new_list)
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            if (state.isLoadingList == LoadingState.PENDING) {
                return@Scaffold
            }

            Box(
                modifier = closeDictionaryPickerModifier,
            ) {
                Header(selectedLists = selectedLists, onAction = onAction)
            }


            Box(modifier = Modifier.zIndex(1f)) {
                Layout(
                    content = {
                        Box(
                            modifier = Modifier
                                .layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)
                                    layout(placeable.width, placeable.height) {
                                        placeable.placeRelative(0, 0)
                                    }
                                }
                        ) {
                            DictionaryPicker(
                                selectedDictionary = currentDictionary.value,
                                dictionaryList = state.dictionaryList,
                                validation = ValidateResult(successful = true),
                                expanded = expanded,
                                setExpanded = { expanded = it },
                                onSelectDictionary = { id ->
                                    onAction(ListsAction.OnSelectDictionary(id))
                                },
                                onPressAddNewDictionary = { onAction(ListsAction.PressAddNewDictionary) },
                            )
                        }
                    }
                ) { measurables, constraints ->
                    val placeable = measurables.firstOrNull()?.measure(constraints)
                    layout(constraints.maxWidth, 110) {
                        placeable?.placeRelative(0, 0)
                    }
                }
            }

            Column(modifier = closeDictionaryPickerModifier) {

                if (state.dictionaryList.isEmpty() && state.isLoadingList == LoadingState.SUCCESS) {
                    ScrollableWrapperScreen {
                        NoDictionariesFoLists(onPressAddNewDictionary = { onAction(ListsAction.PressAddNewDictionary) })
                    }
                } else if (state.isLoadingList == LoadingState.SUCCESS && currentDictionary.value == null) {
                    ScrollableWrapperScreen {
                        NoSelectedDictionary()
                    }
                } else if (state.isLoadingList == LoadingState.SUCCESS && state.list.isEmpty() && currentDictionary.value != null) {
                    ScrollableWrapperScreen {
                        NoListsInDictionary(onPressAddNewList = { onAction(ListsAction.OpenModalNewList) })
                    }
                }

                if (state.isLoadingList == LoadingState.SUCCESS && state.list.isNotEmpty()) {
                    CompositionLocalProvider(
                        LocalOverscrollConfiguration provides null
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                top = dimensionResource(id = R.dimen.gutter),
                                bottom = 60.dp
                            )
                        ) {
                            items(items = state.list) { item ->
                                ListItem(
                                    item = item,
                                    onAction = onAction,
                                    onItemClick = { listId: Long, listName: String ->
                                        onAction(
                                            ListsAction.OnListItemPress(
                                                listId,
                                                listName,
                                            )
                                        )
                                    },
                                    atLeastOneListSelected = atLeastOneListSelected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewListsScreenLoadedWithLists() {
    ListsPresenter(
        state = ListsState(
            currentDictionary = MutableStateFlow(
                Dictionary(
                    id = 1L,
                    langFromCode = "EN",
                    langToCode = "UK",
                    title = "EN-UK",
                    isActive = true,
                )
            ),
            dictionaryList = listOf(
                Dictionary(
                    id = 1L,
                    langFromCode = "EN",
                    langToCode = "UK",
                    title = "EN-UK",
                    isActive = true,
                )
            ),
            list = listOf(
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "Sport",
                    count = 0,
                    id = 1L,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    dictionaryId = 1L,
                ),
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "Politics",
                    count = 10,
                    id = 2L,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    dictionaryId = 1L,
                    isSelected = false,
                ),
            ),

            isLoadingList = LoadingState.SUCCESS
        ),
        onAction = {},
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListsScreenLoadedWithSelectedList() {
    ListsPresenter(
        state = ListsState(
            currentDictionary = MutableStateFlow(
                Dictionary(
                    id = 1L,
                    langFromCode = "EN",
                    langToCode = "UK",
                    title = "EN-UK",
                    isActive = true,
                )
            ),
            dictionaryList = listOf(
                Dictionary(
                    id = 1L,
                    langFromCode = "EN",
                    langToCode = "UK",
                    title = "EN-UK",
                    isActive = true,
                )
            ),
            list = listOf(
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "Sport",
                    count = 0,
                    id = 1L,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    dictionaryId = 1L,
                ),
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "Politics",
                    count = 10,
                    id = 2L,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    dictionaryId = 1L,
                    isSelected = true,
                ),
            ),

            isLoadingList = LoadingState.SUCCESS
        ),
        onAction = {},
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewListsScreenWithoutCurrentDictionary() {
    ListsPresenter(
        state = ListsState(
            currentDictionary = MutableStateFlow(null),
            dictionaryList = listOf(
                Dictionary(
                    id = 1L,
                    langFromCode = "EN",
                    langToCode = "UK",
                    title = "EN-UK",
                    isActive = true,
                )
            ),
            list = listOf(
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "Sport",
                    count = 0,
                    id = 1L,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    dictionaryId = 1L,
                ),
            ),

            isLoadingList = LoadingState.SUCCESS
        ),
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewListsScreenWithoutDictionaryList() {
    ListsPresenter(
        state = ListsState(
            currentDictionary = MutableStateFlow(null),
            dictionaryList = emptyList(),
            list = listOf(
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "Sport",
                    count = 0,
                    id = 1L,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    dictionaryId = 1L,
                ),
            ),

            isLoadingList = LoadingState.SUCCESS
        ),
        onAction = {},
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListsScreenLoading() {
    ListsPresenter(
        state = ListsState(
            list = listOf(),
            isLoadingList = LoadingState.PENDING
        ),
        onAction = {},
    )
}