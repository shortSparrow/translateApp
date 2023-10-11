package com.ovolk.dictionary.presentation.exam.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus.FAIL
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus.IN_PROCESS
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus.SUCCESS
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus.UNPROCESSED
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.util.compose.click_effects.clickWithoutFeedback
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewExamListAllStatus
import com.ovolk.dictionary.util.px
import kotlinx.coroutines.launch

val dots = listOf(1, 2, 3, 4, 5, 6, 7)
const val circleSize = +30 + 10 + 6 // 30 - size round; 10 - padding; 6 - internal text padding

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExamList(
    examWordList: List<ExamWord>,
    isAllExamWordsLoaded: Boolean,
    activeWordPosition: Int = 0,
    onAction: (ExamAction) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    LaunchedEffect(activeWordPosition) {
        coroutineScope.launch {
            listState.animateScrollToItem(
                activeWordPosition,
                -configuration.screenWidthDp.px / 2 + circleSize
            )
        }
    }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
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
                                .sizeIn(30.dp, 30.dp)
                                .clip(CircleShape)
                                .background(
                                    when (word.status) {
                                        SUCCESS -> colorResource(id = R.color.green)
                                        FAIL -> colorResource(id = R.color.red)
                                        IN_PROCESS -> colorResource(id = R.color.blue)
                                        UNPROCESSED -> colorResource(id = R.color.grey)
                                    }
                                )
                                .clickWithoutFeedback { onAction(ExamAction.OnSelectActiveWord(i)) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = (i + 1).toString(), modifier = Modifier.padding(3.dp))
                        }
                    }

                    if (i != examWordList.size - 1 || !isAllExamWordsLoaded) {
                        dots.map {
                            Text(
                                text = ".",
                                fontSize = 20.sp,
                                style = TextStyle(
                                    baselineShift = BaselineShift(0.5f)
                                ),
                                modifier = Modifier.padding(horizontal = 1.dp)
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
fun ExamListPreview() {
    ExamList(
        examWordList = getPreviewExamListAllStatus(),
        isAllExamWordsLoaded = true,
        onAction = {}
    )
}
