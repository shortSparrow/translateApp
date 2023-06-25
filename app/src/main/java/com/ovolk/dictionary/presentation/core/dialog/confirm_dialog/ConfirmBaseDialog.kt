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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.BaseDialog

@Composable
fun ConfirmBaseDialog(
    onDismissRequest: (() -> Unit)? = null,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
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
            title()
        }
        if (description != null) {
            Box(
                Modifier
                    .padding(top = dimensionResource(id = R.dimen.small_gutter))
                    .align(Alignment.CenterHorizontally)
            ) {
                description()
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.gutter))
                .fillMaxWidth()
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