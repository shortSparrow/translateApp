package com.ovolk.dictionary.presentation.modify_word.compose.question_wrapper

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.util.compose.click_effects.opacityClick

@Composable
fun ApplyContainer(absolutePosition: Boolean, content: @Composable (() -> Unit)) {
    if (absolutePosition) Box { content() } else Column { content() }
}

@Composable
fun QuestionWrapper(
    absolutePosition: Boolean = true,
    question: String = "",
    onAction: (ModifyWordAction) -> Unit,
    content: @Composable (() -> Unit)? = null,
) {
    ApplyContainer(absolutePosition) {
        Image(
            modifier = Modifier
                .padding(end = 10.dp)
                .size(20.dp)
                .opacityClick { onAction(ModifyWordAction.ToggleFieldDescribeModalOpen(question)) },
            painter = painterResource(id = R.drawable.help),
            contentDescription = stringResource(id = R.string.modify_word_field_describe_cd)
        )
        content?.let { it() }
    }
}