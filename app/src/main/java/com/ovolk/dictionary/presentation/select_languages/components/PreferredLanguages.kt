package com.ovolk.dictionary.presentation.select_languages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.select_languages.Language
import com.ovolk.dictionary.presentation.select_languages.LanguagesToActions

@Composable
fun PreferredLanguages(langList: List<Language>, onCheck: (language:Language) -> Unit) {
    Column(Modifier.padding(top = dimensionResource(id = R.dimen.small_gutter))) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift(0.3f),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("* ")
                }
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) {
                    append("Preferred languages")
                }

            }
        )
        langList.forEach { language ->
            LanguageCheckBox(
                language = language,
                onCheck = { language -> onCheck(language) })
        }

        Divider(
            color = colorResource(id = R.color.grey), thickness = 2.dp, modifier = Modifier.padding(
                vertical = dimensionResource(
                    id = R.dimen.small_gutter
                )
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preferredLanguagesPreview() {
    Column {
        PreferredLanguages(
            langList = listOf(
                Language(
                    langCode = "uk",
                    name = "Ukrainian",
                    nativeName = "Українська",
                    isChecked = true
                ),
                Language(langCode = "en", name = "English", nativeName = "English"),
            ),
            onCheck = {}
        )
    }
}