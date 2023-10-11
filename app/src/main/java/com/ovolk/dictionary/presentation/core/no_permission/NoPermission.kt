package com.ovolk.dictionary.presentation.core.no_permission

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.util.compose.click_effects.opacityClick

@Composable
fun NoPermission(
    message: String,
    buttonText: String,
    onProvidePermissionClick: () -> Unit,
    withHelpIcon: Boolean = false,
    onHelpIconClick: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(1f),
        contentAlignment = Alignment.Center
    ) {
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.red),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(15.dp)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painterResource(id = R.drawable.error_sign),
                        contentDescription = stringResource(id = R.string.cd_action_error),
                        tint = colorResource(id = R.color.red)
                    )
                    Text(
                        text = message,
                        modifier = Modifier.padding(start = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
                OutlinedButton(onClick = { onProvidePermissionClick() }) {
                    Text(text = buttonText)
                }
            }
            if (withHelpIcon && onHelpIconClick != null) {
                Image(
                    painterResource(id = R.drawable.help),
                    contentDescription = stringResource(id = R.string.cd_action_error),
                    modifier = Modifier
                        .padding(bottom = 10.dp, end = 10.dp)
                        .width(15.dp)
                        .align(Alignment.BottomEnd)
                        .opacityClick { onHelpIconClick() },
                )
            }

        }

    }
}


@Composable
@Preview(showBackground = true)
fun NoPermissionPreview() {
    NoPermission(
        message = stringResource(id = R.string.exam_reminder_no_permission_title),
        buttonText = stringResource(id = R.string.exam_reminder_no_permission_request_button),
        onProvidePermissionClick = { },
        withHelpIcon = true,
        onHelpIconClick = { },
    )
}

@Composable
@Preview(showBackground = true)
fun NoPermissionPreview1() {
    NoPermission(
        message = stringResource(id = R.string.exam_reminder_no_permission_title),
        buttonText = stringResource(id = R.string.exam_reminder_no_permission_request_button),
        onProvidePermissionClick = { },
        withHelpIcon = false,
    )
}

