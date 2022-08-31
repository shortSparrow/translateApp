package com.example.ttanslateapp.presentation.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ttanslateapp.R
import com.example.ttanslateapp.domain.model.lists.ListItem

@Composable
fun Greeting(
    state: ListsState,
    onAction: (ListsAction) -> Unit
) {
    val messages: List<String>
    Column {
        TopAppBar(backgroundColor = Color.Transparent, elevation = 0.dp) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            text = "Lists"
                        )
                    }
                }
            }
        }

        LazyColumn(Modifier) {
            items(items = state.list) { item ->


                val borderColor =
                    if (item.isSelected) colorResource(id = R.color.green) else colorResource(id = R.color.blue_2)

                ConstraintLayout(
                    Modifier.padding(bottom = 20.dp)
                ) {
                    val (dd, mark) = createRefs()
                    Box(
                        Modifier
                            .constrainAs(dd) {
                                start.linkTo(parent.start, margin = 40.dp)
                                end.linkTo(parent.end, margin = 40.dp)
                                width = Dimension.matchParent
                            }

                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(vertical = 16.dp, horizontal = 25.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(text = item.title.uppercase(), fontWeight = FontWeight.Bold,fontSize = 16.sp)
                            Text(text = item.count.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    if (item.isSelected) {
                        Text(
                            text = "+",
                            Modifier
                                .constrainAs(mark) {
                                    start.linkTo(dd.end)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(dd.bottom)
                                    top.linkTo(dd.top)
                                }
                                .width(20.dp)
                                .background(Color.Red)
                        )
                    }

                }


//                Box(
//                    Modifier
//                        .padding(bottom = 20.dp)
//
//                ) {
//                    Row(
//                        Modifier
//                            .background(Color.Green)
//                            .padding(horizontal = 40.dp)
//                            .fillMaxWidth()
//                            .border(
//                                width = 2.dp,
//                                color = colorResource(id = R.color.blue_2),
//                                shape = RoundedCornerShape(10.dp)
//                            )
//                            .padding(vertical = 16.dp, horizontal = 25.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                    ) {
//                        Text(text = item.title)
//                        Text(text = item.count.toString())
//                    }
//
//                    if (item.isSelected) {
//                        Text(
//                            text = "+",
//                            Modifier
//                                .width(20.dp)
//                                .padding(start = 16.dp)
//                                .background(Color.Red)
//                        )
//                    }
//
//                }


            }
        }
    }


}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreview() {
    Greeting(
        state = ListsState(
            list = listOf(
                ListItem(
                    title = "Sport",
                    count = 0
                ),
                ListItem(
                    title = "Politics",
                    count = 10
                ),
                ListItem(
                    title = "LolKek",
                    count = 4,
                    isSelected = true
                ),
            )
        ),
        onAction = {}
    )
}