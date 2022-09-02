package com.example.ttanslateapp.presentation.lists.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ttanslateapp.R

@Composable
fun Header(isVisibleDeleteButton: Boolean, onDeletePress: () -> Unit) {
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
                                text = "Lists"
                            )
                        }
                    }
                }

                Box(
                    Modifier
                        .width(30.dp)
                        .align(Alignment.CenterEnd),
                ) {
                    if (isVisibleDeleteButton) {
                        Icon(
                            painter = painterResource(R.drawable.delete_active),
                            "delete selected lists",
                            tint = colorResource(R.color.red),
                            modifier = Modifier
                                .width(30.dp)
                                .fillMaxHeight()
                                .clickable { onDeletePress() }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreviewHeader() {
    Header(isVisibleDeleteButton = true, onDeletePress = {})
}