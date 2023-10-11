package com.ovolk.dictionary.presentation.settings.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R

@Composable
fun ComplaintAndSuggestionItem(
    image: Painter,
    imageContentDescription: String = "",
    title: String,
    description: String,
    expandedComponent: (@Composable () -> Unit)? = null,
    initialIsExpanded: Boolean = false
) {
    var isExpanded by remember {
        mutableStateOf(initialIsExpanded)
    }

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        elevation = 2.dp
    ) {
        Column(
            Modifier
                .padding(vertical = 15.dp, horizontal = 20.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = image,
                    contentDescription = imageContentDescription,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.size(20.dp))
                Column {
                    Text(
                        text = title,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    )
                    Text(
                        text = description,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }

            if (isExpanded && expandedComponent != null) {
                Spacer(modifier = Modifier.height(15.dp))
                expandedComponent()
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ComplaintAndSuggestionItemPreview() {
    Column(Modifier.padding(20.dp)) {
        ComplaintAndSuggestionItem(
            image = painterResource(id = R.drawable.localization),
            title = "Title",
            description = "description",
            expandedComponent = {
                SelectionContainer {
                    Text(
                        style = TextStyle(fontSize = 14.sp),
                        text = buildAnnotatedString {
                            append("Just write me to ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("my_email@gmail.com")
                            }
                        },
                    )
                }
            }
        )
        ComplaintAndSuggestionItem(
            image = painterResource(id = R.drawable.localization),
            title = "Title",
            description = "description",
            initialIsExpanded = true,
            expandedComponent = {
                SelectionContainer {
                    Text(
                        style = TextStyle(fontSize = 14.sp),
                        text = buildAnnotatedString {
                            append("Just write me to ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("my_email@gmail.com")
                            }
                        },
                    )
                }
            }
        )
    }
}