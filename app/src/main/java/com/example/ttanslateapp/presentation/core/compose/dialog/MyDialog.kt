package com.example.ttanslateapp.presentation.core.compose.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ttanslateapp.R


@Composable
fun MyDialog(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    title: String
) {

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Column(
            Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(15.dp),
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 30.dp)
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .weight(1f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Cross(onClose = { onDismissRequest() })
                }

                content()
            }
        }
    }
}

@Composable
fun Cross(onClose: () -> Unit) {
    Button(
        onClick = { onClose() },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.blue)),
        modifier = Modifier
            .width(30.dp)
            .height(30.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.close),
            stringResource(id = R.string.cd_close_dialog),
            tint = Color.White,
            modifier = Modifier
                .width(14.dp)
                .height(14.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposableDialog() {
    MyDialog(
        onDismissRequest = {},
        content = {
            Text(text = "Content")
        },
        title = "My Dialog"
    )
}