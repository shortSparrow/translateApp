package com.ovolk.dictionary.presentation.core.dropdown_menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

const val HEIGHT_ANIMATION_DURATION = 150

// position hardcoded, suitable only for header right icon
@Composable
fun ShowMoreIconsDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit
) {
    val expandedStates = remember { MutableTransitionState(false) }
    val calculatedHeight = remember { mutableStateOf(0.dp) }

    expandedStates.targetState = expanded

    if (expandedStates.currentState || expandedStates.targetState) {
        val transformOriginState = remember { mutableStateOf(TransformOrigin.Center) }

        val transition = updateTransition(expandedStates, "DropDownMenu")
        val popupPositionProvider = object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                return IntOffset(windowSize.width - popupContentSize.width - 25, 200)
            }
        }

        val height by transition.animateDp(
            transitionSpec = {
                if (false isTransitioningTo true) {
                    // Dismissed to expanded
                    tween(
                        durationMillis = HEIGHT_ANIMATION_DURATION,
                        easing = LinearEasing,
                        delayMillis = 50
                    )
                } else {
                    // Expanded to dismissed.
                    tween(durationMillis = 0)
                }
            },
            label = "show more height",
        ) {
            if (it && calculatedHeight.value != 0.dp) {
                200.dp
            } else {
                0.dp
            }
        }

        val localDensity = LocalDensity.current
        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = popupPositionProvider,
            properties = properties,
        ) {
            Box(
                modifier = Modifier.height(calculatedHeight.value)
            ) {
                Card(
                    modifier = Modifier
                        .heightIn(0.dp, height)
                        .graphicsLayer {
                            transformOrigin = transformOriginState.value
                        },
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = modifier
                            .width(IntrinsicSize.Max)
                            .verticalScroll(rememberScrollState())
                            .onGloballyPositioned { coordinates ->
                                val columnHeightDp =
                                    with(localDensity) { coordinates.size.height.toDp() }

                                if (calculatedHeight.value == 0.dp) {
                                    calculatedHeight.value = columnHeightDp
                                }
                            },
                        content = content
                    )
                }
            }
        }
    }
}