package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.core.chip.TranslateChipItem
import com.ovolk.dictionary.presentation.core.flow_row.FlowRow
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewTranslates

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordIsCheckedPart(
    status: ExamWordStatus,
    currentWordFreeze: Boolean,
    isTranslateExpanded: Boolean,
    isHiddenTranslateDescriptionExpanded: Boolean,
    translates: List<Translate>,
    onAction: (ExamAction) -> Unit
) {
    val toggleTranslatesText =
        if (isTranslateExpanded) stringResource(id = R.string.exam_hide_current_translates)
        else stringResource(id = R.string.exam_show_current_translates)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.small_gutter))
    ) {
        if (status == ExamWordStatus.FAIL) {
            OutlinedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = dimensionResource(id = R.dimen.gutter)),
                onClick = { onAction(ExamAction.OnPressAddHiddenTranslate) }) {
                Text(text = stringResource(id = R.string.exam_add_hidden_translate).uppercase())
            }

            TextButton(onClick = { onAction(ExamAction.ToggleHiddenTranslateDescription) }) {
                Text(text = stringResource(id = R.string.exam_hidden_translate_description_title))
            }

            if (isHiddenTranslateDescriptionExpanded) {
                Text(text = stringResource(id = R.string.exam_hidden_translate_description))
            }
        }


        if (currentWordFreeze) {
            TextButton(onClick = { onAction(ExamAction.ToggleTranslates) }) {
                Text(text = toggleTranslatesText)
            }

            if (isTranslateExpanded) {
                FlowRow {
                    translates.forEach { translate ->
                        Box(
                            modifier =
                            Modifier
                                .padding(dimensionResource(id = R.dimen.small_gutter))
                                .combinedClickable(
                                    onLongClick = {
                                        onAction(ExamAction.OnLongPressHiddenTranslate(translate.id))
                                    },
                                    onClick = { },
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                )
                        ) {
                            TranslateChipItem(
                                title = translate.value,
                                isHidden = translate.isHidden
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WordIsCheckedPartPreview() {
    WordIsCheckedPart(
        status = ExamWordStatus.SUCCESS,
        currentWordFreeze = true,
        isTranslateExpanded = false,
        isHiddenTranslateDescriptionExpanded = false,
        translates = getPreviewTranslates(),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun WordIsCheckedPartPreview2() {
    WordIsCheckedPart(
        status = ExamWordStatus.FAIL,
        currentWordFreeze = true,
        isTranslateExpanded = false,
        isHiddenTranslateDescriptionExpanded = false,
        translates = getPreviewTranslates(),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun WordIsCheckedPartPreview3() {
    WordIsCheckedPart(
        status = ExamWordStatus.FAIL,
        currentWordFreeze = true,
        isTranslateExpanded = true,
        isHiddenTranslateDescriptionExpanded = false,
        translates = getPreviewTranslates(),
        onAction = {}
    )
}