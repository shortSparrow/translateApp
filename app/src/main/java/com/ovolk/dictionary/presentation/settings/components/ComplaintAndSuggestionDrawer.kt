package com.ovolk.dictionary.presentation.settings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomDrawerState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewNearestFeatureList
import com.ovolk.dictionary.util.px

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ComplaintAndSuggestionDrawer(
    drawerState: BottomDrawerState,
    nearestFeatureList: List<NearestFeature>,
) {
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current

    val brush = Brush.horizontalGradient(listOf(Color(0xFF08a8e5), Color(0xFF2eebd8)))

    val anchors = mapOf(
        configuration.screenHeightDp.px.toFloat() to BottomDrawerValue.Closed,
        LocalDensity.current.run { 400.dp.toPx() } to BottomDrawerValue.Expanded
    )

    Column(
        modifier = Modifier
            .height(400.dp)
            .fillMaxSize()
    ) {
        val swipeable = Modifier
            .swipeable(
                state = drawerState,
                anchors = anchors,
                orientation = Orientation.Vertical,
                enabled = true,
                resistance = null
            )

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
                            color = colorResource(
                                id = R.color.orange
                            )
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun ComplaintAndSuggestionDrawerPreview() {
    ComplaintAndSuggestionDrawer(
        nearestFeatureList = getPreviewNearestFeatureList(),
        drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    )
}

