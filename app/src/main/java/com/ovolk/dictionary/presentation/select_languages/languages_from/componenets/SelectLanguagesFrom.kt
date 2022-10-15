package com.ovolk.dictionary.presentation.select_languages.languages_from.componenets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.compose.SearchBar
import com.ovolk.dictionary.presentation.select_languages.Language
import com.ovolk.dictionary.presentation.select_languages.LanguageState
import com.ovolk.dictionary.presentation.select_languages.LanguagesFromActions
import com.ovolk.dictionary.presentation.select_languages.components.Header
import com.ovolk.dictionary.presentation.select_languages.components.LanguageCheckBox

@Composable
fun SelectLanguagesFrom(state: LanguageState, onAction: (LanguagesFromActions) -> Unit) {
    Column {
        Header(title = "select language from", wiBackButton = false)

        Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_gutter))) {
            SearchBar(
                onSearch = { query -> onAction(LanguagesFromActions.OnSearchLanguages(query)) },
                onPressCross = { onAction(LanguagesFromActions.OnSearchLanguages("")) }
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.small_gutter)
            ),
            modifier = Modifier.weight(1f)
        ) {
            items(state.filteredLanguageList) { language ->
                LanguageCheckBox(
                    language = language,
                    onCheck = { language -> onAction(LanguagesFromActions.ToggleCheck(language)) }
                )
            }
        }
        Button(
            onClick = { onAction(LanguagesFromActions.NavigateToHome) },
            enabled = state.languageList.find { it.isChecked } != null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    bottom = dimensionResource(
                        id = R.dimen.small_gutter
                    )
                )
        ) {
            Text(text = "Next".uppercase(), color = colorResource(id = R.color.white))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectLanguagesFromPreview() {
    SelectLanguagesFrom(
        state = LanguageState(
            filteredLanguageList = listOf(
                Language(
                    langCode = "rm",
                    name = "Romansh",
                    nativeName = "rumantsch grischun"
                ),
                Language(
                    langCode = "sd",
                    name = "Sindhi",
                    nativeName = "सिन्धी"
                ),
                Language(
                    langCode = "uk",
                    name = "Ukrainian",
                    nativeName = "українська",
                    isChecked = true
                ),
                Language(
                    langCode = "en",
                    name = "English",
                    nativeName = "English"
                ),
                Language(
                    langCode = "se",
                    name = "Northern Sami",
                    nativeName = "Davvisámegiella"
                ),
                Language(
                    langCode = "sm",
                    name = "Samoan",
                    nativeName = "gagana faa Samoa"
                ),
            ),
            preferredLanguages = listOf(
                Language(
                    langCode = "uk",
                    name = "Ukrainian",
                    nativeName = "українська",
                    isChecked = true
                ),
                Language(
                    langCode = "en",
                    name = "English",
                    nativeName = "English"
                ),
            )
        ),
        onAction = {}
    )
}