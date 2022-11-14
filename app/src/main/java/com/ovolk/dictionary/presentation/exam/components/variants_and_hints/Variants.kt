package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamAnswerVariant
import com.ovolk.dictionary.presentation.core.compose.flow_row.FlowRow
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewAnswerVariants

@Composable
fun Variants(answerVariants: List<ExamAnswerVariant>, onAction: (ExamAction) -> Unit) {
    FlowRow {
        answerVariants.map {
            val backgroundColor =
                if (it.isSelected) colorResource(id = R.color.blue)
                else colorResource(id = R.color.white)

            val borderColor =
                if (it.isSelected) colorResource(id = R.color.blue)
                else colorResource(id = R.color.grey)

            Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor
                ),
                color = backgroundColor,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.small_gutter))
                    .clickable { onAction(ExamAction.OnSelectVariant(it)) }

            ) {
                Text(
                    text = it.value,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.small_gutter))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VariantsPreview() {
    Variants(answerVariants = getPreviewAnswerVariants(), onAction = {})
}