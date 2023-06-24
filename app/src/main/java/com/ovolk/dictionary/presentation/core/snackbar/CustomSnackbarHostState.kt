package com.ovolk.dictionary.presentation.core.snackbar

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class SnackbarOffset(val x: Dp = 0.dp, val y: Dp = 0.dp)

class CustomSnackbarHostState(
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val data: SnackBarType,
    val isHideOnAction: Boolean = false,
    val offset: SnackbarOffset = SnackbarOffset(0.dp,0.dp),
) {
    val state = SnackbarHostState()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun hideSnackbar() {
        state.currentSnackbarData?.dismiss()
    }

    fun showSnackbar() {
        coroutineScope.launch {
            state.showSnackbar(message = "", duration = duration)
        }
    }
}