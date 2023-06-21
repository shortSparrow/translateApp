package com.ovolk.dictionary.presentation.lists.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.presentation.lists.ListsAction

@Composable
fun Header(
    selectedLists: List<ListItem>?,
    onAction: (ListsAction) -> Unit,
) {
    val isVisibleDeleteButton = selectedLists != null

    Column {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Box(
                Modifier.fillMaxWidth(),
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.h6) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.high,
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                text = stringResource(id = R.string.lists_screen_title)
                            )
                        }
                    }
                }

                Box(
                    Modifier
                        .align(Alignment.CenterEnd),
                ) {
                    Row {
                        if (selectedLists?.size == 1) {
                            Surface(
                                Modifier
                                    .width(45.dp)
                                    .height(45.dp)
                                    .align(Alignment.CenterVertically),
                                shape = CircleShape,
                                color = Color.Transparent
                            ) {
                                Box(
                                    Modifier
                                        .clickable {
                                            onAction(
                                                ListsAction.OpenModalRenameList(
                                                    currentName = selectedLists[0].title,
                                                    listId = selectedLists[0].id
                                                )
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.edit),
                                        stringResource(id = R.string.lists_screen_cd_rename_selected_list),
                                        tint = colorResource(R.color.grey),
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                }
                            }
                        }

                        if (isVisibleDeleteButton) {
                            Surface(
                                Modifier
                                    .width(45.dp)
                                    .height(45.dp)
                                    .align(Alignment.CenterVertically),
                                shape = CircleShape,
                                color = Color.Transparent
                            ) {
                                Box(
                                    Modifier
                                        .clickable { onAction(ListsAction.DeleteSelectedLists) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.delete_active),
                                        stringResource(id = R.string.lists_screen_cd_deleted_selected_lists),
                                        tint = colorResource(R.color.red),
                                        modifier = Modifier
                                            .width(25.dp)
                                            .height(25.dp)
                                    )
                                }
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
fun ComposablePreviewHeader() {
    Header(
        selectedLists = listOf(
            ListItem(
                id = 0L,
                count = 1,
                title = "sport",
                isSelected = true,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                dictionaryId = 1L,
            )
        ),
        onAction = {})
}