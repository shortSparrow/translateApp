package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.presentation.exam.NavigateButtons
import com.ovolk.dictionary.presentation.exam.NavigateButtons.NEXT
import com.ovolk.dictionary.presentation.exam.NavigateButtons.PREVIOUS


@Composable
fun ArrowButton(
    type: NavigateButtons,
    isDisabled: Boolean = false,
    onAction: (ExamAction) -> Unit
) {
    val alpha = if (isDisabled) 0.3f else 1f
    fun onClick() {
        if (!isDisabled) {
            onAction(ExamAction.OnPressNavigate(type))
        }
    }
    Surface(
        shape = CircleShape,
        border = BorderStroke(2.dp, color = colorResource(id = R.color.blue)),
        modifier = Modifier.size(50.dp).alpha(alpha),
    ) {

        Box(
            modifier = Modifier.clickable(enabled = !isDisabled) { onClick() },
            contentAlignment = Alignment.Center,
        ) {
            when (type) {
                NEXT -> Icon(
                    painter = painterResource(id = R.drawable.next_arrow),
                    contentDescription = stringResource(id = R.string.exam_next_word_button_cd),
                    tint = colorResource(id = R.color.blue),
                    modifier = Modifier.size(30.dp)
                )
                PREVIOUS -> Icon(
                    painter = painterResource(id = R.drawable.prev_arrow),
                    contentDescription = stringResource(id = R.string.exam_prev_word_button_cd),
                    tint = colorResource(id = R.color.blue),
                    modifier = Modifier.size(30.dp)

                )
            }
        }
    }
}

@Preview
@Composable
fun ArrowButtonPreviewNext() {
    ArrowButton(type = NEXT, isDisabled = true, onAction = {})
}

@Preview
@Composable
fun ArrowButtonPreviewPrevious() {
    ArrowButton(type = PREVIOUS, onAction = {})
}