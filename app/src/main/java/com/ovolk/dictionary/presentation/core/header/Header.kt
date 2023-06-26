package com.ovolk.dictionary.presentation.core.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R

val ZeroButtonOffset = 0.dp
val OneButtonOffset = 45.dp
val TwoButtonOffset = 90.dp

@Composable
fun BackButton(onClick: () -> Unit) {
    IconWrapper(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.back),
            stringResource(id = R.string.cd_go_back),
            tint = colorResource(R.color.grey),
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
        )
    }
}

@Composable
fun IconWrapper(onClick: () -> Unit, content: @Composable () -> Unit) {
    Surface(
        Modifier
            .width(45.dp)
            .height(45.dp),
        shape = CircleShape,
        color = Color.Transparent
    ) {
        Box(
            Modifier.clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun BaseHeader(
    title: @Composable RowScope.() -> Unit,
    withBackButton: Boolean = true,
    onBackButtonClick: (() -> Unit)? = null,
    firstRightIcon: (@Composable () -> Unit)? = null,
    onFirstRightIconClick: (() -> Unit)? = null,
    secondRightIcon: (@Composable () -> Unit)? = null,
    onSecondRightIconClick: (() -> Unit)? = null,
    titleHorizontalOffset: Dp = OneButtonOffset
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(45.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(titleHorizontalOffset)) {
            if (withBackButton) {
                BackButton(onClick = onBackButtonClick!!)
            }
        }

        title()

        Row(
            modifier = Modifier.width(titleHorizontalOffset),
            horizontalArrangement = Arrangement.End
        ) {
            if (firstRightIcon != null && onFirstRightIconClick != null) {
                IconWrapper(onClick = onFirstRightIconClick) {
                    firstRightIcon()
                }
            }
            if (secondRightIcon != null && onSecondRightIconClick != null) {
                IconWrapper(onClick = onSecondRightIconClick) {
                    secondRightIcon()
                }
            }
        }
    }
}

@Composable
fun Header(
    title: @Composable RowScope.() -> Unit,
    withBackButton: Boolean = true,
    onBackButtonClick: (() -> Unit)? = null,
    firstRightIcon: (@Composable () -> Unit)? = null,
    onFirstRightIconClick: (() -> Unit)? = null,
    secondRightIcon: (@Composable () -> Unit)? = null,
    onSecondRightIconClick: (() -> Unit)? = null,
    titleHorizontalOffset: Dp = OneButtonOffset
) {
    BaseHeader(
        title = title,
        withBackButton = withBackButton,
        onBackButtonClick = onBackButtonClick,
        firstRightIcon = firstRightIcon,
        onFirstRightIconClick = onFirstRightIconClick,
        secondRightIcon = secondRightIcon,
        onSecondRightIconClick = onSecondRightIconClick,
        titleHorizontalOffset = titleHorizontalOffset,
    )
}

@Composable
fun Header(
    title: String,
    withBackButton: Boolean = true,
    onBackButtonClick: (() -> Unit)? = null,
    firstRightIcon: (@Composable () -> Unit)? = null,
    onFirstRightIconClick: (() -> Unit)? = null,
    secondRightIcon: (@Composable () -> Unit)? = null,
    onSecondRightIconClick: (() -> Unit)? = null,
    titleHorizontalOffset: Dp = OneButtonOffset
) {
    BaseHeader(
        title = {
            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                Text(
                    maxLines = 1,
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        withBackButton = withBackButton,
        onBackButtonClick = onBackButtonClick,
        firstRightIcon = firstRightIcon,
        onFirstRightIconClick = onFirstRightIconClick,
        secondRightIcon = secondRightIcon,
        onSecondRightIconClick = onSecondRightIconClick,
        titleHorizontalOffset = titleHorizontalOffset,
    )
}


@Composable
@Preview(showBackground = true)
fun HeaderPreview() {
    Header(
        title = "Language to translate from",
        withBackButton = true,
        onBackButtonClick = {},
        titleHorizontalOffset = 45.dp
    )
}

@Composable
@Preview(showBackground = true)
fun HeaderPreview2() {
    Header(
        title = "Modify word",
        withBackButton = true,
        onBackButtonClick = {},
        firstRightIcon = {
            Icon(
                painter = painterResource(R.drawable.exam),
                stringResource(id = R.string.lists_screen_cd_rename_selected_list),
                tint = colorResource(R.color.grey),
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        },
        onFirstRightIconClick = {},
    )
}

@Composable
@Preview(showBackground = true)
fun HeaderPreview3() {
    Header(
        title = "Modify word",
        withBackButton = true,
        onBackButtonClick = {},
        firstRightIcon = {
            Icon(
                painter = painterResource(R.drawable.edit),
                stringResource(id = R.string.edit),
                tint = colorResource(R.color.grey),
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        },
        onFirstRightIconClick = {},
        secondRightIcon = {
            Icon(
                painter = painterResource(R.drawable.delete_active),
                stringResource(id = R.string.delete),
                tint = colorResource(R.color.red),
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        },
        onSecondRightIconClick = {},
        titleHorizontalOffset = TwoButtonOffset
    )
}