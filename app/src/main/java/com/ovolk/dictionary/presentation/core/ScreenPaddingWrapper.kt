package com.ovolk.dictionary.presentation.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ovolk.dictionary.R

@Composable
fun ScreenPaddingWrapper(
    content: @Composable () -> Unit
) {
    Box(Modifier.padding(dimensionResource(id = R.dimen.gutter))) {
        content()
    }
}