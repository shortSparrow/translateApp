package com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R

fun getDescription(
    description: String? = null,
    descriptionFontSize: TextUnit? = null,
    descriptionColor: Color? = null,
): (@Composable () -> Unit)? {
    if (description != null) {
        return {
            Text(
                text = description,
                fontWeight = FontWeight.Medium,
                fontSize = descriptionFontSize
                    ?: dimensionResource(id = R.dimen.dialog_description_font_size).value.sp,
                textAlign = TextAlign.Center,
                color = descriptionColor ?: colorResource(id = R.color.grey),
            )
        }
    }
    return null
}