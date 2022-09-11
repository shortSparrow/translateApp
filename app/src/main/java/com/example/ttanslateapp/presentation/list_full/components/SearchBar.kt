package com.example.ttanslateapp.presentation.list_full.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.list_full.ListFullAction


@Composable
fun SearchBar(
    onAction: (ListFullAction) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
    ) {
        Surface(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Gray),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    text = it
                    onAction(ListFullAction.SearchWord(it))
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            text = ""
                            onAction(ListFullAction.SearchWord(""))
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null,
                            )
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search icon",
                        tint = colorResource(id = R.color.grey),
                    )
                },
                placeholder = { Text(text = "Search word...") }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewSearchBar() {
    SearchBar(onAction = {})
}