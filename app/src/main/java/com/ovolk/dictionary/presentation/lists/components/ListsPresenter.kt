package com.ovolk.dictionary.presentation.lists.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.list_full.LoadingState
import com.ovolk.dictionary.presentation.lists.ListsAction
import com.ovolk.dictionary.presentation.lists.ListsState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListsPresenter(
    state: ListsState,
    onAction: (ListsAction) -> Unit,
) {
    val selectedLists = state.list.groupBy { it.isSelected }[true]
    val atLeastOneListSelected = selectedLists != null

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
            if (state.list.isNotEmpty() && state.isLoadingList == LoadingState.SUCCESS) {
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

            if (state.isLoadingList == LoadingState.SUCCESS && state.list.isEmpty()) {
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
                Header(selectedLists = selectedLists, onAction = onAction)

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
                ),
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "Politics",
                    count = 10,
                    id = 2L,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                ),
                com.ovolk.dictionary.domain.model.lists.ListItem(
                    title = "LolKek",
                    count = 4,
                    id = 3L,
                    isSelected = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
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