package com.ovolk.dictionary.presentation.core.ignore_height_wrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout

@Composable
fun IgnoreHeightWrapper(
    modifier: Modifier = Modifier,
    calculatedWidth: Int? = null,
    calculatedHeight: Int? = null,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(0, 0)
                        }
                    }
            ) {
                content()
            }
        }
    ) { measurables, constraints ->
        val placeable = measurables.firstOrNull()?.measure(constraints)
        layout(calculatedWidth ?: constraints.maxWidth, calculatedHeight ?: constraints.maxHeight) {
            placeable?.placeRelative(0, 0)
        }
    }
}