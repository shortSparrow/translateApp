package com.example.ttanslateapp.presentation.lists.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.lists.ListsViewModel
import com.example.ttanslateapp.presentation.lists.ModalListState
import com.example.ttanslateapp.presentation.lists.ModalType

@Composable
fun DialogAddNewList(
    saveNewList: (title: String) -> Unit,
    modalListState: ModalListState
) {
    val viewModel = hiltViewModel<ListsViewModel>()

    var newListName by remember {
        mutableStateOf(modalListState.initialValue)
    }
    Dialog(
        onDismissRequest = { viewModel.closeModalList() },
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .fillMaxWidth()
                .padding(15.dp),
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 30.dp),

                ) {
                Text(
                    text = modalListState.title,
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Cross(onClose = { viewModel.closeModalList() })
            }

            TextField(
                value = newListName,
                onValueChange = {
                    newListName = it
                },
                label = { Text("Label") }
            )
            Button(
                onClick = { saveNewList(newListName) },
                Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun Cross(onClose: () -> Unit) {
    Button(
        onClick = { onClose() },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.blue)),
        modifier = Modifier
            .width(30.dp)
            .height(30.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.close),
            "close ppup",
            tint = Color.White,
            modifier = Modifier
                .width(14.dp)
                .height(14.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposablePreview2() {
    DialogAddNewList(
        saveNewList = {},
        modalListState = ModalListState(
            isOpen = true,
            type = ModalType.NEW,
            title = "Add New List"
        )
    )
}