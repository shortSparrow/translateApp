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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.CoreDialog
import com.ovolk.dictionary.presentation.core.scrollableWrapper.ScrollableWrapperComponent


@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit,
    message: String,
    description: String? = null,
    buttonText: String? = null,
    onClick: () -> Unit,
    titleFontSize: TextUnit? = null,
    descriptionFontSize: TextUnit? = null,
    titleWeight: FontWeight = FontWeight.Bold,
    descriptionWeight: FontWeight = FontWeight.Medium,
    textAlign: TextAlign = TextAlign.Center
) {
    val buttonText = buttonText ?: stringResource(id = R.string.yes)

    CoreDialog(onDismissRequest = onDismissRequest) {
        ScrollableWrapperComponent {
            Text(
                text = message,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter)),
                fontWeight = titleWeight,
                fontSize = titleFontSize
                    ?: dimensionResource(id = R.dimen.dialog_title_font_size).value.sp,
                textAlign = textAlign,
                color = colorResource(id = R.color.grey),
            )

            if (description != null) {
                Text(
                    text = description,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.gutter)),
                    fontWeight = descriptionWeight,
                    fontSize = descriptionFontSize
                        ?: dimensionResource(id = R.dimen.dialog_description_font_size).value.sp,
                    textAlign = textAlign,
                    color = colorResource(id = R.color.grey),
                )

            }

            OutlinedButton(onClick = onClick) {
                Text(text = buttonText)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InfoDialogPreview() {
    InfoDialog(
        onDismissRequest = {},
        message = "The exam is complete",
        buttonText = "OK",
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun InfoDialogWithDescriptionPreview() {
    InfoDialog(
        onDismissRequest = {},
        message = "The exam is complete",
        description = "long description for describing all needed cases for user",
        buttonText = "OK",
        onClick = {},
    )
}