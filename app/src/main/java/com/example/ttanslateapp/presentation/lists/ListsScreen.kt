package com.example.ttanslateapp.presentation.lists

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.ttanslateapp.R
import com.example.ttanslateapp.domain.model.lists.ListItem
import com.example.ttanslateapp.presentation.lists.components.DialogAddNewList
import com.example.ttanslateapp.presentation.lists.components.Header
import com.example.ttanslateapp.presentation.lists.components.ListItem
import timber.log.Timber

@Composable
fun ListsScreen(
    state: ListsState,
    onAction: (ListsAction) -> Unit,
    getNavController: () -> NavController
) {
    val atLeastOneListSelected = state.list.find { it.isSelected } != null

    fun saveNewList(title: String) {
        onAction(ListsAction.AddNewList(title))
    }

    fun onDeletePress() {
        onAction(ListsAction.DeletedSelectedLists)
    }

    if (state.modalList.isOpen) {
        DialogAddNewList(
            saveNewList = ::saveNewList,
            modalListState = state.modalList,
            onAction = onAction
        )
    }

    ConstraintLayout {
        val (imagePlus) = createRefs()

        Column(Modifier.fillMaxHeight()) {
            Header(
                isVisibleDeleteButton = atLeastOneListSelected,
                onDeletePress = ::onDeletePress
            )
            LazyColumn(Modifier.padding(top = 20.dp)) {
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

        Button(
            onClick = { onAction(ListsAction.OpenModal(ModalType.NEW)) },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.blue)),
            modifier = Modifier
                .constrainAs(imagePlus) {
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                }
                .width(55.dp)
                .height(55.dp),

            ) {
            Icon(
                painter = painterResource(R.drawable.add),
                "add new list",
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        }
    }


}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreview() {
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
            )
        ),
        onAction = {},
        getNavController = { NavController(null as Context) }
    )
}