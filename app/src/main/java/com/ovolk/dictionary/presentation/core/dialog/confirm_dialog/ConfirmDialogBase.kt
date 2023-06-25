package com.ovolk.dictionary.presentation.core.dialog.confirm_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.BaseDialog


@Composable
fun ConfirmDialogBase(
    onDismissRequest: (() -> Unit)? = null,
    message: @Composable () -> Unit,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = ConfirmDialogType.OK_BUTTON_RED
) {
    val okButtonColor = when (type) {
        ConfirmDialogType.OK_BUTTON_RED -> colorResource(id = R.color.red)
        ConfirmDialogType.NO_BUTTON_RED -> colorResource(id = R.color.blue)
        ConfirmDialogType.NO_RED -> colorResource(id = R.color.blue)
    }

    val noButtonColor = when (type) {
        ConfirmDialogType.OK_BUTTON_RED -> colorResource(id = R.color.blue)
        ConfirmDialogType.NO_BUTTON_RED -> colorResource(id = R.color.red)
        ConfirmDialogType.NO_RED -> colorResource(id = R.color.blue)
    }

    BaseDialog(
        onDismissRequest = {
            if (onDismissRequest != null) {
                onDismissRequest()
            } else {
                onDeclineClick()
            }
        },
    ) {
        Box(Modifier.align(Alignment.CenterHorizontally)) {
            message()
        }

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

