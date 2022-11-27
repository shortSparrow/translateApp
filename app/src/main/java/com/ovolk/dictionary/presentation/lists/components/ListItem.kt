package com.ovolk.dictionary.presentation.lists.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.presentation.lists.ListsAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    item: ListItem,
    onAction: (ListsAction) -> Unit,
    onItemClick: (listId: Long, listName: String) -> Unit,
    atLeastOneListSelected: Boolean
) {
    val borderColor =
        if (item.isSelected) colorResource(id = R.color.green) else colorResource(id = R.color.blue_2)

    ConstraintLayout(
        Modifier
            .padding(bottom = 20.dp)
    ) {
        val (dd, mark) = createRefs()
        Surface(
            Modifier
                .constrainAs(dd) {
                    start.linkTo(parent.start, margin = 40.dp)
                    end.linkTo(parent.end, margin = 40.dp)
                    width = Dimension.matchParent
                },
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onLongClick = {
                            onAction(ListsAction.SelectList(item.id))
                        },
                        onClick = {
                            if (atLeastOneListSelected) {
                                onAction(ListsAction.SelectList(item.id))
                            } else {
                                onItemClick(item.id, item.title)
                            }
                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = false,
                            color = colorResource(id = if (item.isSelected) R.color.blue_2 else R.color.green)
                        ),
                    )
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(vertical = 16.dp, horizontal = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = item.count.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (item.isSelected) {
            Image(
                painter = painterResource(
                    id = R.drawable.check_mark,

                    ),
                contentDescription = stringResource(id = R.string.lists_screen_cd_mark),
                Modifier
                    .constrainAs(mark) {
                        start.linkTo(dd.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(dd.bottom)
                        top.linkTo(dd.top)
                    }
                    .width(20.dp),
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposableListItemPreview() {
    ListItem(
        item = ListItem(
            id = 0L,
            title = "Sport",
            isSelected = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        onItemClick = {_: Long, _: String ->  },
        onAction = {},
        atLeastOneListSelected = false,
    )
}

@Preview(showBackground = true)
@Composable
fun ComposableListItemPreview2() {
    ListItem(
        item = ListItem(
            id = 0L,
            title = "Sport",
            isSelected = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        onItemClick = {_: Long, _: String ->  },
        onAction = {},
        atLeastOneListSelected = false,
    )
}