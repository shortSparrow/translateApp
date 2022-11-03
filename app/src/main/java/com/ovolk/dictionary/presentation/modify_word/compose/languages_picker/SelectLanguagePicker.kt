package com.ovolk.dictionary.presentation.modify_word.compose.languages_picker

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult


val selectLanguagePickerWidth = 110.dp

@Composable
fun SelectLanguagePicker(
    dropDownItem: List<SelectLanguage>,
    onSelect: (lang: SelectLanguage) -> Unit,
    onAddNewLangPress: () -> Unit,
    error: ValidateResult,
) {
    var expanded by remember { mutableStateOf(false) }
    val transition = updateTransition(expanded, label = "selectIsOpen")
    val errorColor =
        colorResource(id = R.color.red) // TODO change on my own color (only after rewrite old code on compose)
    val borderColor = if (error.successful) {
        colorResource(id = R.color.blue)
    } else {
        errorColor
    }

    val rotateArrow by transition.animateFloat(
        label = "rotateArrow",
        targetValueByState = { state ->
            when (state) {
                true -> 90f
                false -> -90f
            }
        },
        transitionSpec = {
            tween(
                durationMillis = 150,
                easing = LinearEasing
            )
        },
    )

    Column(
        modifier = Modifier
            .width(selectLanguagePickerWidth),

        ) {
        Surface(
            shape = RoundedCornerShape(50),
            border = BorderStroke(width = 1.dp, color = borderColor),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(6.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.language),
                        contentDescription = stringResource(id = R.string.cd_lang_icon)
                    )
                    Text(
                        text = dropDownItem.find { it.isChecked }?.langCode?.uppercase() ?: "",
                        Modifier.padding(start = 10.dp)
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.prev_arrow),
                    contentDescription = stringResource(id = R.string.cd_arrow_lang),
                    modifier = Modifier.rotate(rotateArrow)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(selectLanguagePickerWidth),

                ) {
                dropDownItem.forEach {
                    DropdownMenuItem(onClick = {
                        onSelect(it)
                        expanded = false
                    }) {
                        Text(it.langCode.uppercase())
                    }
                }

                Divider()
                DropdownMenuItem(onClick = {
                    onAddNewLangPress()
                    expanded = false
                }) {
                    Text(
                        stringResource(id = R.string.add_new),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Text(
            text = error.errorMessage ?: "",
            color = errorColor,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            softWrap = false
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SelectLanguagePreview() {
    Row {
        SelectLanguagePicker(
            dropDownItem = listOf(
                SelectLanguage(langCode = "UA", name = "ukrainian", nativeName = "Українська"),
            ),
            onSelect = {},
            error = ValidateResult(errorMessage = "this field is required"),
            onAddNewLangPress = {}
        )
    }

}