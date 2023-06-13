package com.ovolk.dictionary.presentation.core.header


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
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


// TODO change list header
@Composable
fun HeaderWithTwoRightActions(
    title: String,
    firstButton: (@Composable () -> Unit)? = null,
    secondButton: (@Composable () -> Unit)? = null,
    onFirstButtonClick: (() -> Unit)? = null,
    onSecondButtonClick: (() -> Unit)? = null,
    onBack: () -> Unit
) {
    Column {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {

                BackButton(onClick = onBack)

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
                                text = title,
                            )
                        }
                    }
                }

                Box(Modifier.align(Alignment.CenterEnd)) {
                    Row {
                        if (secondButton != null) {
                            IconWrapper(onClick = {
                                if (onSecondButtonClick != null) onSecondButtonClick()
                            }) {
                                secondButton()
                            }
                        }

                        if (firstButton != null) {
                            IconWrapper(onClick = {
                                if (onFirstButtonClick != null) onFirstButtonClick()
                            }) {
                                firstButton()
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
fun ComposablePreviewHeaderWithTwoRightButtons() {
    HeaderWithTwoRightActions(
        title = "Header name",
        firstButton = {
            Icon(
                painter = painterResource(R.drawable.delete_active),
                stringResource(id = R.string.lists_screen_cd_deleted_selected_lists),
                tint = colorResource(R.color.red),
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
            )
        },
        secondButton = {
            Icon(
                painter = painterResource(R.drawable.edit),
                stringResource(id = R.string.lists_screen_cd_rename_selected_list),
                tint = colorResource(R.color.grey),
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
        },
        onBack = {}

    )
}