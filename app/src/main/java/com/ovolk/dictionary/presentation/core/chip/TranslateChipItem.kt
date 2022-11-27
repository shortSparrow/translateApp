package com.ovolk.dictionary.presentation.core.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R

@Composable
fun TranslateChipItem(title: String, isHidden: Boolean = false) {
    val color = if (isHidden) colorResource(id = R.color.light_grey) else Color.Transparent
    val alpha = if (isHidden) 0.5f else 1f
    val textColor = if (isHidden) Color.White else colorResource(id = R.color.grey_2)

    Column {
        Surface(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, color = colorResource(id = R.color.grey_2)),
            color = color,
            modifier = Modifier.alpha(alpha)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(10.dp),
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewTranslateChipItem1() {
    TranslateChipItem(
        title = "Яблуко",
        isHidden = false
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewTranslateChipItem2() {
    TranslateChipItem(
        title = "Яблуко",
        isHidden = true
    )
}
