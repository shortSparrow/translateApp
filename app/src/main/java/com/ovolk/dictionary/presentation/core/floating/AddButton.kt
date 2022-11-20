package com.ovolk.dictionary.presentation.core.floating

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R

@Composable
fun AddButton(
    onClick: () -> Unit,
    contentDescription: String
) {
    Column {
        Button(
            onClick = { onClick() },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.blue)),
            modifier = Modifier
                .width(55.dp)
                .height(55.dp),

            ) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription,
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewAddButton() {
    AddButton(onClick = {}, contentDescription = "Add new list")
}
