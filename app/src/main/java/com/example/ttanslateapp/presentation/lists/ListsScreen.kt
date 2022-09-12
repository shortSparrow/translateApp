package com.example.ttanslateapp.presentation.lists

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ttanslateapp.R
import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.presentation.core.compose.dialog.ConfirmDialog
import com.example.ttanslateapp.presentation.core.compose.floating.AddButton
import com.example.ttanslateapp.presentation.list_full.LoadingState
import com.example.ttanslateapp.presentation.lists.components.DialogAddNewList
import com.example.ttanslateapp.presentation.lists.components.Header
import com.example.ttanslateapp.presentation.lists.components.ListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListsScreen(
    state: ListsState,
    onAction: (ListsAction) -> Unit,
    getNavController: () -> NavController
) {
    val selectedLists = state.list.groupBy { it.isSelected }[true]
    val atLeastOneListSelected = selectedLists != null


    if (state.modalList.isOpen) {
        DialogAddNewList(
            modalListState = state.modalList,
            onAction = onAction
        )
    }

    if (state.isOpenDeleteListModal) {
        ConfirmDialog(
            question = stringResource(id = R.string.lists_screen_confirm_delete_list),
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
                .fillMaxHeight()
                .padding(contentPadding)
        ) {
            Header(
                selectedLists = selectedLists,
                onAction = onAction
            )

            if (state.isLoadingList == LoadingState.PENDING) {
//                Text(text = "LOADING...")
            }

            if (state.isLoadingList == LoadingState.SUCCESS && state.list.isEmpty()) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_list),
                        contentDescription = stringResource(id = R.string.cd_nothing_found),
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter))
                    )
                    Text(
                        text = stringResource(id = R.string.nothing_found),
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.large_gutter))
                    )

                    OutlinedButton(onClick = { onAction(ListsAction.OpenModalNewList) }) {
                        Text(text = stringResource(id = R.string.lists_screen_add_new_list))
                    }
                }
            }

            if (state.isLoadingList == LoadingState.SUCCESS && state.list.isNotEmpty()) {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyColumn(contentPadding = PaddingValues(top = 20.dp, bottom = 60.dp)) {
                        items(items = state.list) { item ->
                            ListItem(
                                item = item,
                                onAction = onAction,
                                onItemClick = { listId: Long ->
                                    onAction(
                                        ListsAction.OnListItemPress(
                                            listId,
                                            getNavController()
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
    ListsScreen(
        state = ListsState(
            list = listOf(),
            isLoadingList = LoadingState.PENDING
        ),
        onAction = {},
        getNavController = { NavController(null as Context) }
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListsScreenSuccess() {
    ListsScreen(
        state = ListsState(
            list = listOf(
                ListItem(
                    title = "Sport",
                    count = 0,
                    id = 1L,
                ),
                ListItem(
                    title = "Politics",
                    count = 10,
                    id = 2L,
                ),
                ListItem(
                    title = "LolKek",
                    count = 4,
                    id = 3L,
                    isSelected = true
                ),
            ),
            isLoadingList = LoadingState.SUCCESS
        ),
        onAction = {},
        getNavController = { NavController(null as Context) }
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListsScreenEmpty() {
    ListsScreen(
        state = ListsState(
            list = listOf(),
            isLoadingList = LoadingState.SUCCESS
        ),
        onAction = {},
        getNavController = { NavController(null as Context) }
    )
}