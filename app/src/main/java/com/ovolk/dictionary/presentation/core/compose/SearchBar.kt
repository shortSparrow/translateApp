package com.ovolk.dictionary.presentation.core.compose

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R


@Composable
fun SearchBar(
    onSearch: (query: String) -> Unit,
    onPressCross: () -> Unit,
    searchedValue: String? = null
) {
    // TODO remove uncontrollable if. Use only controllable
    val isControlled = searchedValue != null
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
                value = searchedValue ?: text,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    if (!isControlled) {
                        text = it
                    }
                    onSearch(it)
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if ((!isControlled && text.isNotEmpty()) || (isControlled && searchedValue?.isNotEmpty() == true)) {
                        IconButton(onClick = {
                            if (!isControlled) {
                                text = ""
                            }
                            onPressCross()
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
                        contentDescription = stringResource(id = R.string.cd_search_icon),
                        tint = colorResource(id = R.color.grey),
                    )
                },
                placeholder = { Text(text = stringResource(id = R.string.search_word_placeholder)) }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewSearchBar() {
    SearchBar(onSearch = {}, onPressCross = {})
}