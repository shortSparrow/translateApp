package com.ovolk.dictionary.presentation.core.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ovolk.dictionary.R


@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit,
    message: String,
    buttonText: String? = null,
    onClick: () -> Unit,
) {
    val buttonText = buttonText ?: stringResource(id = R.string.yes)

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Surface(shape = RoundedCornerShape(15.dp)) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(bottom = 20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.grey),
                )

                OutlinedButton(onClick = onClick) {
                    Text(text = buttonText)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun InfoDialogPreview() {
    InfoDialog(
        onDismissRequest = {},
        message = "The exam is complete",
        buttonText = "OK",
        onClick = {},
    )
}