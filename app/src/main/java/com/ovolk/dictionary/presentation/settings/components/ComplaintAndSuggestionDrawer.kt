package com.ovolk.dictionary.presentation.settings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewNearestFeatureList
import kotlin.math.roundToInt

val BottomNavigationHeight = 56.dp

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ComplaintAndSuggestionDrawer(
    swipeableState: SwipeableState<BottomDrawerValue>,
    nearestFeatureList: List<NearestFeature>,
) {
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current

    val brush = Brush.horizontalGradient(listOf(Color(0xFF08a8e5), Color(0xFF2eebd8)))

    // get 400 or if screen height less than take screen height - 100 (horizontal orientation)
    val height = min(configuration.screenHeightDp.dp - 100.dp, 400.dp)
    val screenHeight = configuration.screenHeightDp.dp
    val availableScreenHeight = screenHeight - BottomNavigationHeight

    val anchors = mapOf(
        LocalDensity.current.run { screenHeight.toPx() } to BottomDrawerValue.Closed,
        LocalDensity.current.run { (availableScreenHeight - height).toPx() } to BottomDrawerValue.Open,
    )


//    val squareSize = 50.dp
//
//    val swipeableState = rememberSwipeableState(1)
//    val sizePx = with(LocalDensity.current) { ((842).dp - BottomNavigationHeight).toPx() }
//    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states
//
//    Box(
//        modifier = Modifier
//            .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
//            .size(100.dp)
//            .background(Color.LightGray)
//    ) {
//        Box(
//            Modifier
//                .swipeable(
//                    state = swipeableState,
//                    anchors = anchors,
//                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
//                    orientation = Orientation.Vertical
//                )
//                .size(squareSize)
//                .background(Color.DarkGray)
//        )
//    }

    Surface(
        modifier = Modifier
            .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
            .background(Color.Yellow, shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
            .height(height),
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
    ) {

        val swipeable = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Vertical,
                resistance = null
            )

        Column {
            Box(swipeable) {
                Column(
                    modifier = Modifier
                        .background(brush)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                            .width(70.dp)
                            .background(
                                color = colorResource(id = R.color.white),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {
                        Text(
                            text = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_header_title),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.white),
                                fontSize = 19.sp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_header_message),
                            style = TextStyle(
                                color = colorResource(id = R.color.white),
                                fontSize = 15.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.gutter))
                        .verticalScroll(scrollState)
                ) {
                    ComplaintAndSuggestionItem(
                        image = painterResource(id = R.mipmap.bug),
                        imageContentDescription = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_bug_icon_cd),
                        title = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_bug_title),
                        description = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_bug_message),
                        expandedComponent = {
                            SelectionContainer {
                                Text(
                                    style = TextStyle(fontSize = 14.sp),
                                    text = buildAnnotatedString {
                                        append(stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_bug_description_1))
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append("formobileprima@gmail.com")
                                        }
                                        append(stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_bug_description_2))
                                    },
                                )
                            }
                        }
                    )

                    ComplaintAndSuggestionItem(
                        image = painterResource(id = R.mipmap.opinion),
                        imageContentDescription = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_opinion_icon_cd),
                        title = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_opinion_title),
                        description = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_opinion_message),
                        expandedComponent = {
                            SelectionContainer {
                                Text(
                                    style = TextStyle(fontSize = 14.sp),
                                    text = buildAnnotatedString {
                                        append(stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_opinion_description_1))
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append("formobileprima@gmail.com")
                                        }
                                        append(stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_opinion_description_2))
                                    },
                                )
                            }
                        }
                    )

                    ComplaintAndSuggestionItem(
                        image = painterResource(id = R.drawable.stars_gold),
                        imageContentDescription = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_next_features_icon_cd),
                        title = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_next_features_title),
                        description = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_next_features_message),
                        expandedComponent = {
                            Text(
                                text = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_next_features_disclaimer),
                                color = colorResource(id = R.color.orange),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Column {
                                if (nearestFeatureList.isNotEmpty()) {
                                    nearestFeatureList.forEach {
                                        NearestFeatureItem(feature = it)
                                    }
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.complaints_and_suggestions_bottom_sheet_next_features_no_features),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    )

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun ComplaintAndSuggestionDrawerPreview() {
    ComplaintAndSuggestionDrawer(
        nearestFeatureList = getPreviewNearestFeatureList(),
        swipeableState = rememberSwipeableState(BottomDrawerValue.Closed)
    )
}

