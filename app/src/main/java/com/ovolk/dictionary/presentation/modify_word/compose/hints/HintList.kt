package com.ovolk.dictionary.presentation.modify_word.compose.hints

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.presentation.core.compose.chip.HintChipItem
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction

@Composable
fun HintList(
    hints: List<HintItem>,
    focusRequesterTranslates: FocusRequester,
    onAction: (ModifyWordAction) -> Unit
) {
    Column() {
        hints.map { hint ->
            var expanded by remember { mutableStateOf(false) }
            fun onPressEditTranslate() {
                expanded = false
                onAction(ModifyWordAction.OnPressEditHint(hint))
                focusRequesterTranslates.requestFocus()
            }

            Surface(modifier = Modifier.padding(8.dp), shape = RoundedCornerShape(8.dp)) {
                Box {
                    HintChipItem(
                        title = hint.value,
                        onHintPress = { expanded = !expanded },
                        onDeleteHintPress = { onAction(ModifyWordAction.OnDeleteHint(hint.localId)) }
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = false),
                ) {
                    DropdownMenuItem(onClick = ::onPressEditTranslate) {
                        Text(
                            stringResource(id = R.string.edit),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HintListPreview() {
    HintList(
        hints = getPreviewHints(),
        onAction = {},
        focusRequesterTranslates = remember { FocusRequester() }
    )
}