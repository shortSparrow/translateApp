package com.ovolk.dictionary.presentation.modify_word.compose.languages_picker

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.presentation.core.ignore_height_wrapper.IgnoreHeightWrapper

const val HEADER_ANIMATION_TIME = 150
const val HEIGHT_ANIMATION_TIME = 250

@Composable
fun SelectDictionaryPicker(
    selectedDictionary: Dictionary?,
    dictionaryList: List<Dictionary>,
    validation: ValidateResult,
    expanded: Boolean = false,
    setExpanded: (value: Boolean) -> Unit,
    onSelectDictionary: (dictionaryId: Long) -> Unit,
    onPressAddNewDictionary: () -> Unit,
) {
    val transition = updateTransition(expanded, label = "selectIsOpen")
    val errorColor =
        colorResource(id = R.color.red)
    val borderColor = if (validation.successful) {
        colorResource(id = R.color.blue)
    } else {
        errorColor
    }

    fun onAddNewDictionaryPress() {
        onPressAddNewDictionary()
        setExpanded(false)
    }

    fun onDictionaryItemPress(item: Dictionary) {
        onSelectDictionary(item.id)
        setExpanded(false)
    }


    val selectLanguagePickerWidth by transition.animateDp(
        label = "width",
        targetValueByState = { state ->
            when (state) {
                true -> 300.dp
                false -> 140.dp
            }
        },
        transitionSpec = {
            if (transition.currentState) {
                tween(
                    durationMillis = HEADER_ANIMATION_TIME,
                    easing = LinearEasing,
                    delayMillis = HEIGHT_ANIMATION_TIME + 50
                )
            } else {
                tween(durationMillis = HEADER_ANIMATION_TIME, easing = EaseInOut)
            }
        },
    )

    val rotateArrow by transition.animateFloat(
        label = "rotateArrow",
        targetValueByState = { state ->
            when (state) {
                true -> 90f
                false -> -90f
            }
        },
        transitionSpec = {
            if (transition.currentState) {
                tween(
                    durationMillis = HEADER_ANIMATION_TIME,
                    easing = LinearEasing,
                    delayMillis = HEIGHT_ANIMATION_TIME + 50
                )
            } else {
                tween(durationMillis = HEADER_ANIMATION_TIME, easing = EaseInOut)
            }
        },
    )

    val maxHeight by transition.animateDp(
        label = "height",
        targetValueByState = { state ->
            when (state) {
                true -> 200.dp
                false -> 0.dp
            }
        },
        transitionSpec = {
            if (transition.currentState) {
                tween(
                    durationMillis = HEIGHT_ANIMATION_TIME,
                    easing = LinearEasing,
                )

            } else {
                tween(
                    durationMillis = HEIGHT_ANIMATION_TIME,
                    easing = LinearEasing,
                    delayMillis = HEADER_ANIMATION_TIME + 50
                )
            }
        },
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            border = BorderStroke(width = 1.dp, color = borderColor),
            modifier = Modifier
                .width(selectLanguagePickerWidth)
                .zIndex(1F),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { setExpanded(!expanded) }
                    .padding(6.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.language),
                        contentDescription = stringResource(id = R.string.cd_lang_icon)
                    )
                    Text(
                        text = selectedDictionary?.title ?: "",
                        Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.prev_arrow),
                    contentDescription = stringResource(id = R.string.cd_arrow_lang),
                    modifier = Modifier
                        .rotate(rotateArrow)
                        .size(20.dp),
                )
            }
        }
        if (!validation.successful && validation.errorMessage != null) {
            IgnoreHeightWrapper(calculatedHeight = 0) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = validation.errorMessage,
                        color = colorResource(id = R.color.red),
                        maxLines = 2,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp),
            border = BorderStroke(width = 1.dp, color = borderColor),
            modifier = Modifier
                .widthIn(0.dp, selectLanguagePickerWidth)
                .offset(y = ((-20).dp))
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp),
                )
                .zIndex(0F),
        ) {
            Column() {
                LazyColumn(
                    modifier = Modifier
                        .heightIn(50.dp, maxHeight)
                        .padding(top = 20.dp)

                ) {
                    items(dictionaryList) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDictionaryItemPress(item) }
                                .padding(vertical = 10.dp, horizontal = 10.dp),
                        ) {
                            Text(text = item.title, modifier = Modifier)
                        }
                        Divider()
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedButton(onClick = ::onAddNewDictionaryPress) {
                                Text(
                                    text = "Add new Dictionary".uppercase(),
                                    modifier = Modifier,
                                    color = colorResource(id = R.color.blue)
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SelectLanguagePreview() {
    SelectDictionaryPicker(
        selectedDictionary = Dictionary(
            // TODO remove isSelected from this Dictionary. Create just dictionary and SelectableDictionary for settings
            id = 0L,
            title = "FR-PL",
            isActive = false,
            isSelected = false,
            langFromCode = "FR",
            langToCode = "PL",
        ),
        dictionaryList = listOf(
            Dictionary(
                id = 0L,
                title = "FR-PL",
                isActive = false,
                isSelected = false,
                langFromCode = "FR",
                langToCode = "PL",
            ),
            Dictionary(
                id = 1L,
                title = "EN-UA",
                isActive = false,
                isSelected = true,
                langFromCode = "EN",
                langToCode = "UA",
            )
        ),
        validation = ValidateResult(successful = true),
        setExpanded = {},
        onSelectDictionary = {},
        onPressAddNewDictionary = {},
    )
}

@Preview(showBackground = true)
@Composable
fun SelectLanguagePreview2() {
    SelectDictionaryPicker(
        selectedDictionary = Dictionary(
            id = 1L,
            title = "EN-UA",
            isActive = false,
            isSelected = true,
            langFromCode = "EN",
            langToCode = "UA",
        ),
        dictionaryList = listOf(
            Dictionary(
                id = 0L,
                title = "FR-PL",
                isActive = false,
                isSelected = false,
                langFromCode = "FR",
                langToCode = "PL",
            ),
            Dictionary(
                id = 1L,
                title = "EN-UA",
                isActive = false,
                isSelected = true,
                langFromCode = "EN",
                langToCode = "UA",
            )
        ),
        validation = ValidateResult(errorMessage = "this field is required"),
        setExpanded = {},
        expanded = true,
        onSelectDictionary = {},
        onPressAddNewDictionary = {},
    )
}

@Preview(showBackground = true)
@Composable
fun SelectLanguagePreview3() {
    SelectDictionaryPicker(
        selectedDictionary = null,
        dictionaryList = emptyList(),
        validation = ValidateResult(errorMessage = "this field is required", successful = false),
        setExpanded = {},
        onSelectDictionary = {},
        onPressAddNewDictionary = {},
    )
}