package com.ovolk.dictionary.presentation.modify_word.compose.languages_picker

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult


@Composable
fun SelectDictionaryPicker(
    selectedDictionary: Dictionary?,
    dictionaryList: List<Dictionary>,
    error: ValidateResult,
    expanded: Boolean = false,
    setExpanded: (value: Boolean) -> Unit,
    onSelectDictionary: (dictionaryId: Long) -> Unit,
    onPressAddNewDictionary: () -> Unit,
) {
//    var expanded by remember { mutableStateOf(false) }
    val transition = updateTransition(expanded, label = "selectIsOpen")
    val errorColor =
        colorResource(id = R.color.red)
    val borderColor = if (error.successful) {
        colorResource(id = R.color.blue)
    } else {
        errorColor
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
            tween(durationMillis = 150, easing = LinearEasing)
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
            tween(durationMillis = 150, easing = LinearEasing)
        },
    )

    val height by transition.animateDp(
        label = "height",
        targetValueByState = { state ->
            when (state) {
                true -> 200.dp
                false -> 0.dp
            }
        },
        transitionSpec = {
            tween(durationMillis = 150, easing = LinearEasing, delayMillis = 150)
        },
    )

//    Popup(
//        properties = PopupProperties(dismissOnClickOutside = true),
//        onDismissRequest = { expanded = false },
//    ) {
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

        Popup(properties = PopupProperties(dismissOnClickOutside = true)) {

        }

        Surface(
            shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp),
            border = BorderStroke(width = 1.dp, color = borderColor),
            modifier = Modifier
//                    .width(selectLanguagePickerWidth)
                .width(if (selectLanguagePickerWidth.value.toInt() == 300) 300.dp else 0.dp)
                .offset(y = ((-20).dp))
                .zIndex(0F),
        ) {
            LazyColumn(
                modifier = Modifier
                    .height(height)
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)

            ) {
                items(dictionaryList) { item ->
                    Text(text = item.title, modifier = Modifier.clickable {
                        onSelectDictionary(item.id)
                        setExpanded(false)
                    })
                }

                item {
                    Text(text = "Add new Dictionary", modifier = Modifier.clickable {
                        onPressAddNewDictionary()
                        setExpanded(false)
                    })
                }
            }
        }
    }
//    }
}


@Preview(showBackground = true)
@Composable
fun SelectLanguagePreview() {
    SelectDictionaryPicker(
        selectedDictionary = Dictionary(
            // TODO remove isSelected from this Dictionary
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
        error = ValidateResult(successful = true),
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
        error = ValidateResult(errorMessage = "this field is required"),
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
        error = ValidateResult(errorMessage = "this field is required"),
        setExpanded = {},
        onSelectDictionary = {},
        onPressAddNewDictionary = {},
    )
}