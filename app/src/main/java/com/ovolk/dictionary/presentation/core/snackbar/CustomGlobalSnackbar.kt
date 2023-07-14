package com.ovolk.dictionary.presentation.core.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger

@Composable
fun CustomGlobalSnackbar() {
    val globalSnackbarManger = GlobalSnackbarManger
    val globalSnackbarState =
        globalSnackbarManger.customSnackbarHostState.collectAsState()

    globalSnackbarState.value?.let { snackState ->
        CustomSnackbar(snackState = snackState)
    }
}