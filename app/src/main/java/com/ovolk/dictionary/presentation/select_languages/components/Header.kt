package com.ovolk.dictionary.presentation.select_languages.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import com.ovolk.dictionary.R

@Composable
fun Header(
    title: String,
    wiBackButton: Boolean = true
) {
    val localView = LocalView.current
    fun goBack() {
        localView.findNavController().popBackStack()
    }

    Column {
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier
                .align(Alignment.Start)
        ) {
            Box(
                Modifier.fillMaxWidth(),
            ) {

                if (wiBackButton) {
                    Surface(
                        Modifier
                            .width(45.dp)
                            .height(45.dp)
                            .align(Alignment.CenterStart),
                        shape = CircleShape,
                        color = Color.Transparent
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.clickable {goBack() }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.back),
                                contentDescription = stringResource(id = R.string.cd_go_back),
                                tint = colorResource(R.color.grey),
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                            )
                        }
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.h6) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.high,
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                text = title
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewHeader() {
    Header(
        title = "Language from",
        wiBackButton = true
    )
}