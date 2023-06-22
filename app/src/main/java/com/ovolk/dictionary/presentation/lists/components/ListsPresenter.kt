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
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.list_full.LoadingState
import com.ovolk.dictionary.presentation.lists.ListsAction
import com.ovolk.dictionary.presentation.lists.ListsState
import com.ovolk.dictionary.presentation.modify_word.compose.languages_picker.SelectDictionaryPicker

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListsPresenter(
    state: ListsState,
    onAction: (ListsAction) -> Unit,
) {
    val selectedLists = state.list.groupBy { it.isSelected }[true]
    val atLeastOneListSelected = selectedLists != null
    val currentDictionary = state.currentDictionary.collectAsState()

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
//                Text(text = "LOADING...")
            }

            Header(selectedLists = selectedLists, onAction = onAction)

            var expanded by remember {
                mutableStateOf(false)
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



            // TODO what happens if dictionary list is not empty but active dictionary don't exist
            if (state.list.isEmpty()
//                && state.dictionaryList.isEmpty()
                && state.isLoadingList == LoadingState.SUCCESS
                && currentDictionary.value == null
            ) {
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
                        text = "Looks lie you don't have any dictionary",
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.grey_2),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "At first create dictionary where you can put your lists",
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
                        color = colorResource(id = R.color.grey_2),
                        textAlign = TextAlign.Center
                    )

                    Button(onClick = { onAction(ListsAction.PressAddNewDictionary) }) {
                        Text(
                            text = "create dictionary".uppercase(),
                            color = Color.White
                        )
                    }
                }
            }


            if (state.isLoadingList == LoadingState.SUCCESS && state.list.isEmpty() && currentDictionary.value != null) {
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
                            contentDescription = stringResource(id = R.string.cd_nothing_found),
                            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter))
                        )
                        Text(
                            text = stringResource(id = R.string.nothing_found),
                            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.medium_gutter)),
                            fontSize = 20.sp,
                            color = colorResource(id = R.color.grey_2)
                        )

                        Button(onClick = { onAction(ListsAction.OpenModalNewList) }) {
                            Text(
                                text = stringResource(id = R.string.lists_screen_add_new_list).uppercase(),
                                color = Color.White
                            )
                        }
                    }
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


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListsScreenPending() {
    ListsPresenter(
        state = ListsState(
            list = listOf(),
            isLoadingList = LoadingState.PENDING
        ),
        onAction = {},
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListsScreenSuccess() {
    ListsPresenter(
        state = ListsState(
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
                ),
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "LolKek",
                    count = 4,
                    id = 3L,
                    isSelected = true,
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

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListsScreenEmpty() {
    ListsPresenter(
        state = ListsState(
            list = listOf(),
            isLoadingList = LoadingState.SUCCESS
        ),
        onAction = {},
    )
}