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

fun getTitle(
    title: String,
    titleColor: Color? = null,
    titleFontSize: TextUnit? = null,
): @Composable () -> Unit {
    return {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = titleFontSize
                ?: dimensionResource(id = R.dimen.dialog_title_font_size).value.sp,
            color = titleColor ?: Color.Black
        )
    }

}