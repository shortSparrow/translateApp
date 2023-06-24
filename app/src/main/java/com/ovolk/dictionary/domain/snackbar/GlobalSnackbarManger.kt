package com.ovolk.dictionary.domain.snackbar

import androidx.compose.material.SnackbarDuration
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.presentation.core.snackbar.CustomSnackbarHostState
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarData
import com.ovolk.dictionary.presentation.core.snackbar.SnackbarOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object GlobalSnackbarManger {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    var customSnackbarHostState = MutableStateFlow<CustomSnackbarHostState?>(null)

    fun hideGlobalSnackbar() {
        customSnackbarHostState.value?.hideSnackbar()
    }

    fun showGlobalSnackbar(
        duration: SnackbarDuration = SnackbarDuration.Short,
        data: SnackBarData,
        isHideOnAction: Boolean = false,
        offset: SnackbarOffset =  SnackbarOffset(0.dp,(-80).dp)
    ) {
        // configure snackbar
        customSnackbarHostState.value = CustomSnackbarHostState(
            duration = duration,
            isHideOnAction = isHideOnAction,
            data = data,
            offset = offset,
        )

        // show configured snackbar
        coroutineScope.launch {
            customSnackbarHostState.value?.showSnackbar()
        }
    }
}