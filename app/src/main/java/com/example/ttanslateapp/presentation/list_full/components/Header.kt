package com.example.ttanslateapp.presentation.list_full.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.list_full.ListFullAction

@Composable
fun Header(
    onAction: (ListFullAction) -> Unit
) {
    Column {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Box(
                Modifier.fillMaxWidth(),
            ) {

                Surface(
                    Modifier
                        .width(45.dp)
                        .height(45.dp)
                        .align(Alignment.CenterStart),
                    shape = CircleShape,
                    color = Color.Transparent
                ) {
                    Box(
                        Modifier.clickable { onAction(ListFullAction.GoBack) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            stringResource(id = R.string.cd_go_back),
                            tint = colorResource(R.color.grey),
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp)
                        )
                    }
                }


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
                                text = stringResource(id = R.string.full_lists_title)
                            )
                        }
                    }
                }

                // TODO make exam logic
                Surface(
                    Modifier
                        .width(45.dp)
                        .height(45.dp)
                        .align(Alignment.CenterEnd),
                    shape = CircleShape,
                    color = Color.Transparent
                ) {
                    Box(
                        Modifier.clickable {onAction(ListFullAction.TakeExam)},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.exam),
                            stringResource(id = R.string.lists_screen_cd_rename_selected_list),
                            tint = colorResource(R.color.grey),
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


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewHeader() {
    Header(onAction = {})
}