package com.ovolk.dictionary.presentation.lists.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.list_full.LoadingState
import com.ovolk.dictionary.presentation.lists.ListsAction
import com.ovolk.dictionary.presentation.lists.ListsState
import com.ovolk.dictionary.presentation.modify_word.compose.languages_picker.SelectDictionaryPicker
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalFoundationApi::class)
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
        ConfirmDialog(
            message = stringResource(id = R.string.lists_screen_confirm_delete_list),
            onAcceptClick = { onAction(ListsAction.ConfirmDeleteSelectedLists) },
            onDeclineClick = { onAction(ListsAction.DeclineDeleteSelectedLists) })
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
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            if (state.isLoadingList == LoadingState.PENDING) {
                return@Scaffold
            }

            Header(selectedLists = selectedLists, onAction = onAction)

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
                            SelectDictionaryPicker(
                                selectedDictionary = currentDictionary.value,
                                dictionaryList = state.dictionaryList,
                                error = ValidateResult(successful = true),
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


            if (state.dictionaryList.isEmpty() && state.isLoadingList == LoadingState.SUCCESS) {
                NoDictionaries(onPressAddNewDictionary = { onAction(ListsAction.PressAddNewDictionary) })
            } else if (state.isLoadingList == LoadingState.SUCCESS && currentDictionary.value == null) {
                NoSelectedDictionary()
            } else if (state.isLoadingList == LoadingState.SUCCESS && state.list.isEmpty() && currentDictionary.value != null) {
                NoListsInDictionary(onPressAddNewList = { onAction(ListsAction.OpenModalNewList) })
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
                    isSelected = false
                )
            ),
            dictionaryList = listOf(
                Dictionary(
                    id = 1L,
                    langFromCode = "EN",
                    langToCode = "UK",
                    title = "EN-UK",
                    isActive = true,
                    isSelected = false
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
                    isSelected = false
                )
            ),
            dictionaryList = listOf(
                Dictionary(
                    id = 1L,
                    langFromCode = "EN",
                    langToCode = "UK",
                    title = "EN-UK",
                    isActive = true,
                    isSelected = false
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
                    isSelected = false
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