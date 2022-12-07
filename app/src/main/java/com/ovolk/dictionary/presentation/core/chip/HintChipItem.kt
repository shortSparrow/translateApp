package com.ovolk.dictionary.presentation.core.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R

@Composable
fun HintChipItem(title: String, onHintPress: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, color = colorResource(id = R.color.grey_2)),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .clickable { onHintPress() }
                    .fillMaxWidth(1f)
                    .padding(10.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HintChipItemPreview() {
    HintChipItem(
        title = "це фрукт",
        onHintPress = {},
    )
}
