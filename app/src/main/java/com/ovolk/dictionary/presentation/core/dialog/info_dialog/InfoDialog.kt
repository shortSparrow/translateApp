package com.ovolk.dictionary.presentation.core.dialog.info_dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.BaseDialog


@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit,
    message: String,
    buttonText: String? = null,
    onClick: () -> Unit,
    fontSize: TextUnit? = null,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Center
) {
    val buttonText = buttonText ?: stringResource(id = R.string.yes)

    BaseDialog(onDismissRequest = onDismissRequest) {
        Text(
            text = message,
            modifier = Modifier.padding(bottom = 20.dp),
            fontWeight = fontWeight,
            fontSize = fontSize ?: dimensionResource(id = R.dimen.dialog_title_font_size).value.sp,
            textAlign = textAlign,
            color = colorResource(id = R.color.grey),
        )

        OutlinedButton(onClick = onClick) {
            Text(text = buttonText)
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