package com.ovolk.dictionary.presentation.exam.components.variants_and_hints

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.presentation.exam.components.Variants
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewAnswerVariants
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewHints

@Composable
fun VariantsAndHints(
    isVariantsExpanded: Boolean,
    isHintsExpanded: Boolean,
    hints: List<HintItem>,
    answerVariants: List<ExamAnswerVariant>,
    onAction: (ExamAction) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val variantsText =
                if (isVariantsExpanded) stringResource(id = R.string.exam_hide_variants) else stringResource(
                    id = R.string.exam_show_variants
                )
            val hintsText =
                if (isHintsExpanded) stringResource(id = R.string.exam_hide_hints) else stringResource(
                    id = R.string.exam_show_hints
                )

            if (answerVariants.isNotEmpty()) {
                OutlinedButton(onClick = { onAction(ExamAction.ToggleShowVariants) }) {
                    Text(
                        text = variantsText.uppercase(),
                        color = colorResource(id = R.color.blue)
                    )
                }
            }

            if (hints.isNotEmpty()) {
                OutlinedButton(onClick = { onAction(ExamAction.ToggleHints) }) {
                    Text(
                        text = hintsText.uppercase(),
                        color = colorResource(id = R.color.blue)
                    )
                }
            }
        }

        if (isVariantsExpanded && answerVariants.isNotEmpty()) {
            Variants(answerVariants = answerVariants, onAction = onAction)
        }

        if (isHintsExpanded && hints.isNotEmpty()) {
            Hints(hints = hints)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VariantsAndHintsPreview1() {
    VariantsAndHints(
        isVariantsExpanded = false,
        isHintsExpanded = false,
        hints = emptyList(),
        answerVariants = emptyList(),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun VariantsAndHintsPreview2() {
    VariantsAndHints(
        isVariantsExpanded = true,
        isHintsExpanded = false,
        hints = emptyList(),
        answerVariants = getPreviewAnswerVariants(),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun VariantsAndHintsPreview3() {
    VariantsAndHints(
        isVariantsExpanded = false,
        isHintsExpanded = false,
        hints = getPreviewHints(),
        answerVariants = emptyList(),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun VariantsAndHintsPreview4() {
    VariantsAndHints(
        isVariantsExpanded = false,
        isHintsExpanded = true,
        hints = getPreviewHints(),
        answerVariants = emptyList(),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun VariantsAndHintsPreview5() {
    VariantsAndHints(
        isVariantsExpanded = true,
        isHintsExpanded = true,
        hints = getPreviewHints(),
        answerVariants = getPreviewAnswerVariants(),
        onAction = {}
    )
}