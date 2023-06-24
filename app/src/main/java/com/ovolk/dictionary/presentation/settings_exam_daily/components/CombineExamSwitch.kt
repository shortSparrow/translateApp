package com.ovolk.dictionary.presentation.settings_exam_daily.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R

@Composable
fun CombineExamSwitch(
    languagesForDescription: List<String>,
    isDoubleLanguageExamEnable: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = R.string.settings_daily_exam_words_is_double_lang_exam_label),
            )

            Text(
                buildAnnotatedString {
                    append("When you pass the exam, you will alternate languages. The first word will be ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        if (languagesForDescription.isEmpty()) {
                            appendInlineContent("loading")
                        } else {
                            append(languagesForDescription[0])
                        }
                    }
                    append(" and you will have to translate it into ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        if (languagesForDescription.isEmpty()) {
                            appendInlineContent("loading")
                        } else {
                            append(languagesForDescription[1])
                        }
                    }
                    append(" and the second, on the contrary, is ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        if (languagesForDescription.isEmpty()) {
                            appendInlineContent("loading")
                        } else {
                            append(languagesForDescription[1])
                        }
                    }
                    append(" and you will have to translate it into ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        if (languagesForDescription.isEmpty()) {
                            appendInlineContent("loading")
                        } else {
                            append(languagesForDescription[0])
                        }
                    }
                },
                fontSize = 10.sp,
                color = colorResource(id = R.color.light_grey),
                inlineContent = mapOf(
                    Pair(
                        "loading",
                        InlineTextContent(
                            Placeholder(
                                width = 1.em,
                                height = 1.em,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                            )
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize(),
                                strokeWidth = 2.dp
                            )
                        }
                    )
                )
            )
        }

        Switch(
            checked = isDoubleLanguageExamEnable,
            onCheckedChange = { onCheckedChange() },
            modifier = Modifier.padding(start = 5.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.blue),
                checkedTrackColor = colorResource(id = R.color.blue),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CombineExamSwitchPreview() {
    CombineExamSwitch(
        languagesForDescription = listOf("uk", "en"),
        isDoubleLanguageExamEnable = false,
        onCheckedChange = {},
    )
}


@Preview(showBackground = true)
@Composable
fun CombineExamSwitchPreview2() {
    CombineExamSwitch(
        languagesForDescription = emptyList(),
        isDoubleLanguageExamEnable = true,
        onCheckedChange = {},
    )
}