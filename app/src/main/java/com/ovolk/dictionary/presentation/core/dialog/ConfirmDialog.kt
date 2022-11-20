package com.ovolk.dictionary.presentation.core.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialogType.*

enum class ConfirmDialogType {OK_BUTTON_RED, NO_BUTTON_RED, NO_RED}

@Composable
fun ConfirmDialog(
    onDismissRequest: (() -> Unit)? = null,
    question: String,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = OK_BUTTON_RED
) {
    val okButtonColor = when(type) {
        OK_BUTTON_RED -> colorResource(id = R.color.red)
        NO_BUTTON_RED -> colorResource(id = R.color.blue)
        NO_RED -> colorResource(id = R.color.blue)
    }

    val noButtonColor = when(type) {
        OK_BUTTON_RED -> colorResource(id = R.color.blue)
        NO_BUTTON_RED -> colorResource(id = R.color.red)
        NO_RED -> colorResource(id = R.color.blue)
    }

    Dialog(
        onDismissRequest = {
            if (onDismissRequest != null) {
                onDismissRequest()
            } else {
                onDeclineClick()
            }
        },
    ) {
        Surface(shape = RoundedCornerShape(15.dp)) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 15.dp),
            ) {

                Text(
                    text = question,
                    Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    OutlinedButton(onClick = onDeclineClick) {
                        Text(
                            text = declineButtonText ?: stringResource(id = R.string.no),
                            color = noButtonColor
                        )
                    }
                    OutlinedButton(onClick = onAcceptClick) {
                        Text(
                            text = confirmButtonText ?: stringResource(id = R.string.yes),
                            color = okButtonColor
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ConfirmDialogPreview() {
    ConfirmDialog(
        onDismissRequest = {},
        question = "Are you want to delete this values?",
        onAcceptClick = {},
        onDeclineClick = {}
    )
}