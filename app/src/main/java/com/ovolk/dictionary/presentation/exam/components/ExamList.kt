package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus.*
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.util.compose.click_effects.withoutEffectClick
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewExamListAllStatus
import kotlinx.coroutines.launch

val dots = listOf(1, 2, 3, 4, 5, 6, 7)

@Composable
fun ExamList(
    examWordList: List<ExamWord>,
    isAllExamWordsLoaded: Boolean,
    activeWordPosition: Int = 0,
    onAction: (ExamAction) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(activeWordPosition) {
        coroutineScope.launch {
            listState.animateScrollToItem(activeWordPosition)
        }
    }

    LazyRow(state = listState) {
        itemsIndexed(examWordList) { i, word ->
            val border = if (i == activeWordPosition) BorderStroke(
                2.dp,
                colorResource(id = R.color.blue)
            ) else null

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (examWordList.size - i < 10) {
                    onAction(ExamAction.OnLoadNextPageWords)
                }
                Surface(
                    shape = CircleShape,
                    border = border,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(
                                when (word.status) {
                                    SUCCESS -> colorResource(id = R.color.green)
                                    FAIL -> colorResource(id = R.color.red)
                                    IN_PROCESS -> colorResource(id = R.color.blue)
                                    UNPROCESSED -> colorResource(id = R.color.grey)
                                }
                            )
                            .withoutEffectClick { onAction(ExamAction.OnSelectActiveWord(i)) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = i.toString())
                    }
                }

                if (i == examWordList.size - 1 && isAllExamWordsLoaded) {

                } else {
                    dots.map {
                        Text(
                            text = ".",
                            fontSize=20.sp,
                            style = TextStyle(
                                baselineShift= BaselineShift(0.5f)
                            ),
                            modifier = Modifier.padding(horizontal = 1.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExamListPreview() {
    ExamList(
        examWordList = getPreviewExamListAllStatus(),
        isAllExamWordsLoaded = true,
        onAction = {}
    )
}

//@Preview(showBackground = true)
//@Composable
//fun ExamListPreview2() {
//    ExamList(
//        examWordList = getPreviewExamListAllStatus().mapIndexed { i, item ->
//            if (i == 0) {
//                item.status = UNPROCESSED
//            }
//            if (i == 1) {
//                item.status = IN_PROCESS
//                item
//            }
//            item
//        },
//        isAllExamWordsLoaded = true,
//        activeWordPosition = 1,
//        onAction = {}
//    )
//}

