package com.ovolk.dictionary.presentation.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
    searchedValue: String? = null,
    placeholderTextId: @Composable () -> Unit = {
        Text(text = stringResource(id = R.string.search_word_placeholder))
    }
) {
    val isControlled = searchedValue != null
    var text by remember {
        mutableStateOf("")
    }
    var isSearchBarFocused by remember {
        mutableStateOf(false)
    }
    val borderColor =
        if (isSearchBarFocused) colorResource(id = R.color.blue) else colorResource(id = R.color.grey)
    val borderWidth = if (isSearchBarFocused) 1.5.dp else 1.dp

    Column(
        modifier = Modifier
    ) {
        Surface(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(borderWidth, borderColor),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isSearchBarFocused = it.hasFocus
                    },
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
                        tint = borderColor,
                    )
                },
                placeholder = placeholderTextId,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    SearchBar(onSearch = {}, onPressCross = {})
}