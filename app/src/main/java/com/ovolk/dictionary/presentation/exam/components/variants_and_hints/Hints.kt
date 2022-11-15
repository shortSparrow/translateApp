package com.ovolk.dictionary.presentation.exam.components.variants_and_hints

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewHints


@Composable
fun Hints(hints: List<HintItem>) {
    val visibleHints = remember {
        mutableStateListOf(hints[0])
    }

    fun handlePlusPress() {
        visibleHints.add(hints[visibleHints.size])
    }

    Column() {
        visibleHints.forEachIndexed { index, hint ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.small_gutter))
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, colorResource(id = R.color.grey)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = hint.value, modifier = Modifier.padding(10.dp))
                }
                if (index == visibleHints.size - 1 && index != hints.size - 1) {
                    Surface(
                        shape = CircleShape, color = colorResource(id = R.color.blue),
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { handlePlusPress() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "show next hint"
                        )

                    }
                } else {
                    Box(Modifier.size(40.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HintsPreview() {
    Hints(hints = getPreviewHints())
}