package com.ovolk.dictionary.presentation.core.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.util.compose.WhiteRippleTheme


@Composable
fun CustomSnackbar(snackState: CustomSnackbarHostState) {
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackState.state,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = snackState.offset.x, y = snackState.offset.y)
        ) {
            CustomSnackbarLayout(snackState)
        }
    }
}

@Composable
fun CustomSnackbarLayout(state: CustomSnackbarHostState) {
    fun onClick() {
        if (state.isHideOnAction) {
            state.hideSnackbar()
        }
        state.data.action?.invoke()
    }

    val backgroundColor = when (state.data) {
        is SnackBarError -> {
            colorResource(id = R.color.red)
        }

        is SnackBarSuccess -> {
            colorResource(id = R.color.green_3)
        }

        is SnackBarInfo -> {
            colorResource(id = R.color.blue_3)
        }

       is SnackBarAlert -> {
            colorResource(id = R.color.orange)
        }

        else -> {
            Color.Black
        }
    }

    Snackbar(backgroundColor = backgroundColor, modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (state.data) {
                    is SnackBarError -> {
                        Icon(
                            painterResource(id = R.drawable.error_sign),
                            contentDescription = "error happend",
                            tint = colorResource(id = R.color.white)
                        )
                    }

                    is SnackBarSuccess -> {
                        Image(
                            painterResource(id = R.drawable.check_mark),
                            contentDescription = "action was succeed",
                        )
                    }

                    is SnackBarInfo -> {
                        Icon(
                            painterResource(id = R.drawable.stars),
                            contentDescription = "action was succeed",
                            tint = colorResource(id = R.color.white)
                        )
                    }

                    is SnackBarAlert -> {
                        Icon(
                            painterResource(id = R.drawable.error_sign),
                            contentDescription = "action was succeed",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                }

                Text(
                    state.data.message,
                    modifier = Modifier.padding(start = 15.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if (state.data.actionTitle != null && state.data.action != null) {
                CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme) {
                    TextButton(onClick = ::onClick) {
                        Text(text = state.data.actionTitle, color = Color.White)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CustomSnackbarLayoutPreview() {
    Column(modifier = Modifier.padding(10.dp)) {
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarSuccess(message = "Hello"),
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarSuccess(message = "Hello", actionTitle = "click", action = {}),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSnackbarLayoutPreview2() {
    Column(modifier = Modifier.padding(10.dp)) {
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarError(message = "Oops"),
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarError(
                    message = "something went wrong",
                    actionTitle = "try again",
                    action = {}),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSnackbarLayoutPreview3() {
    Column(modifier = Modifier.padding(10.dp)) {
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarInfo(message = "Nice"),
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarInfo(
                    message = "dictionary mark as active",
                    actionTitle = "open one",
                    action = {}),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSnackbarLayoutPreview4() {
    Column(modifier = Modifier.padding(10.dp)) {
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarAlert(message = "Hmmm..."),
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        CustomSnackbarLayout(
            state = CustomSnackbarHostState(
                data = SnackBarAlert(
                    message = "looks like you don't have permission",
                    actionTitle = "try again",
                    action = {}),
            )
        )
    }
}
