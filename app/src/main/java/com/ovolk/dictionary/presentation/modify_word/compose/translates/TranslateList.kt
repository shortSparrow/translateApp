package com.ovolk.dictionary.presentation.modify_word.compose.translates

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.core.compose.chip.TranslateChipItem
import com.ovolk.dictionary.presentation.core.compose.flow_row.FlowRow
import com.ovolk.dictionary.presentation.list_full.components.getPreviewTranslates
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.presentation.modify_word.ModifyWordTranslatesAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TranslateList(
    translates: List<Translate>,
    focusRequesterTranslates: FocusRequester,
    onAction: (ModifyWordTranslatesAction) -> Unit
) {
    FlowRow {
        translates.map { translate ->
            var expanded by remember { mutableStateOf(false) }
            fun onPressEditTranslate() {
                expanded = false
                onAction(ModifyWordTranslatesAction.OnPressEditTranslate(translate))
                focusRequesterTranslates.requestFocus()
            }

            fun onPressRemoveTranslate() {
                expanded = false;
                onAction(ModifyWordTranslatesAction.OnPressDeleteTranslate(translate.localId))
            }

            Surface(modifier = Modifier.padding(8.dp), shape = RoundedCornerShape(8.dp)) {
                Box(
                    modifier = Modifier.combinedClickable(
                        onLongClick = {
                            onAction(ModifyWordTranslatesAction.OnLongPressTranslate(translate.localId))
                        },
                        onClick = { expanded = !expanded },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                    )
                ) {
                    TranslateChipItem(title = translate.value, isHidden = translate.isHidden)
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
                    DropdownMenuItem(onClick = ::onPressRemoveTranslate) {
                        Text(
                            stringResource(id = R.string.delete),
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
fun TranslateListPreview() {
    Column() {
        TranslateList(
            translates = getPreviewTranslates(),
            onAction = {},
            focusRequesterTranslates = remember { FocusRequester() }
        )
    }
}