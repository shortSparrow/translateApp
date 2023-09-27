package com.ovolk.dictionary.presentation.core.dialog.confirm_dialog

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.utils.getDescription
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.utils.getTitle


enum class ConfirmDialogType { OK_BUTTON_RED, NO_BUTTON_RED, NO_RED }


@Composable
fun ConfirmDialog(
    onDismissRequest: (() -> Unit)? = null,
    title: String,
    titleColor: Color? = null,
    titleFontSize: TextUnit? = null,
    description: String? = null,
    descriptionFontSize: TextUnit? = null,
    descriptionColor: Color? = null,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = ConfirmDialogType.OK_BUTTON_RED
) {
    ConfirmBaseDialog(
        onDismissRequest = onDismissRequest,
        title = getTitle(title = title, titleColor = titleColor, titleFontSize = titleFontSize),
        description = getDescription(
            description = description,
            descriptionFontSize = descriptionFontSize,
            descriptionColor = descriptionColor
        ),
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
    title: String,
    titleColor: Color? = null,
    titleFontSize: TextUnit? = null,
    description: @Composable () -> Unit,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = ConfirmDialogType.OK_BUTTON_RED
) {
    ConfirmBaseDialog(
        onDismissRequest = onDismissRequest,
        title = getTitle(title = title, titleColor = titleColor, titleFontSize = titleFontSize),
        description = description,
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
    title: @Composable () -> Unit,
    description: String? = null,
    descriptionFontSize: TextUnit? = null,
    descriptionColor: Color? = null,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = ConfirmDialogType.OK_BUTTON_RED
) {
    ConfirmBaseDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        description = getDescription(
            description = description,
            descriptionFontSize = descriptionFontSize,
            descriptionColor = descriptionColor
        ),
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
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    confirmButtonText: String? = null,
    declineButtonText: String? = null,
    type: ConfirmDialogType = ConfirmDialogType.OK_BUTTON_RED
) {
    ConfirmBaseDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        description = description,
        onAcceptClick = onAcceptClick,
        onDeclineClick = onDeclineClick,
        confirmButtonText = confirmButtonText,
        declineButtonText = declineButtonText,
        type = type,
    )
}


@Preview(showBackground = true)
@Composable
fun ConfirmDialogWithDescriptionPreview() {
    ConfirmDialog(
        onDismissRequest = {},
        title = "Are you want to delete this values?",
        description = "some description. Long text which describes user something",
        onAcceptClick = {},
        onDeclineClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmDialogWithDescriptionPreview2() {
    ConfirmDialog(
        title = "Are you want to delete this values?",
        onAcceptClick = { },
        onDeclineClick = { },
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmDialogWithDescription3() {
    ConfirmDialog(
        onDismissRequest = {},
        title = "Are you want to delete this values?",
        description = {
            Text(
                text = "Text in Compose ❤️",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        val brush =
                            Brush.horizontalGradient(listOf(Color.Cyan, Color.Red, Color.Magenta))
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        },
        onAcceptClick = {},
        onDeclineClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmDialogWithDescriptionPreview4() {
    ConfirmDialog(
        title = "Are you want to delete this values?",
        onAcceptClick = { },
        onDeclineClick = { },
        confirmButtonText="GO TO THE SETTINGS"
    )
}