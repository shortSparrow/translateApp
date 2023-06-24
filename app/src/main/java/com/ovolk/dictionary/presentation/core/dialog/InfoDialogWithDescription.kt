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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ovolk.dictionary.R


@Composable
fun InfoDialogWithDescription(
    onDismissRequest: () -> Unit,
    message: String,
    description: String?=null,
    buttonText: String? = null,
    onClick: () -> Unit,
    titleFontSize: TextUnit = 18.sp,
    descriptionFontSize: TextUnit = 14.sp,
    titleWeight: FontWeight = FontWeight.Bold,
    descriptionWeight: FontWeight = FontWeight.Medium,
    textAlign: TextAlign = TextAlign.Center
) {
    val buttonText = buttonText ?: stringResource(id = R.string.yes)

    BaseDialog(onDismissRequest = onDismissRequest) {
        Text(
            text = message,
            modifier = Modifier.padding(bottom = 20.dp),
            fontWeight = titleWeight,
            fontSize = titleFontSize,
            textAlign = textAlign,
            color = colorResource(id = R.color.grey),
        )

        if (description != null) {
            Text(
                text = description,
                modifier = Modifier.padding(bottom = 20.dp),
                fontWeight = descriptionWeight,
                fontSize = descriptionFontSize,
                textAlign = textAlign,
                color = colorResource(id = R.color.grey),
            )

        }

        OutlinedButton(onClick = onClick) {
            Text(text = buttonText)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InfoDialogWithDescriptionPreview() {
    InfoDialogWithDescription(
        onDismissRequest = {},
        message = "The exam is complete",
        buttonText = "OK",
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun InfoDialogWithDescriptionPreview2() {
    InfoDialogWithDescription(
        onDismissRequest = {},
        message = "The exam is complete",
        description = "long description for describing all needed cases for user",
        buttonText = "OK",
        onClick = {},
    )
}