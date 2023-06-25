package com.ovolk.dictionary.presentation.core.dialog.confirm_dialog

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.ConfirmDialogType.OK_BUTTON_RED

enum class ConfirmDialogType { OK_BUTTON_RED, NO_BUTTON_RED, NO_RED }

@Composable
fun ConfirmDialog(
    onDismissRequest: (() -> Unit)? = null,
    message: String,
    fontSize: TextUnit? = null,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = OK_BUTTON_RED
) {
    ConfirmDialogBase(
        onDismissRequest = onDismissRequest,
        message = {
            Text(
                text = message,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = fontSize
                    ?: dimensionResource(id = R.dimen.dialog_title_font_size).value.sp,
            )
        },
        onAcceptClick = onAcceptClick,
        onDeclineClick = onDeclineClick,
        confirmButtonText = confirmButtonText,
        declineButtonText = declineButtonText,
        type = type,
    )
}

@Composable
fun ConfirmDialog(
    onDismissRequest: (() -> Unit)? = null,
    message: @Composable () -> Unit,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = OK_BUTTON_RED
) {
    ConfirmDialogBase(
        onDismissRequest = onDismissRequest,
        message = message,
        onAcceptClick = onAcceptClick,
        onDeclineClick = onDeclineClick,
        confirmButtonText = confirmButtonText,
        declineButtonText = declineButtonText,
        type = type,
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ConfirmDialogPreview() {
    ConfirmDialog(
        onDismissRequest = {},
        message = "Are you want to delete this values?",
        onAcceptClick = {},
        onDeclineClick = {}
    )
}