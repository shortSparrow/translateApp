package com.ovolk.dictionary.presentation.select_languages.languages_to.components

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
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.presentation.core.compose.SearchBar
import com.ovolk.dictionary.presentation.select_languages.LanguageState
import com.ovolk.dictionary.presentation.select_languages.LanguagesToActions
import com.ovolk.dictionary.presentation.select_languages.components.Header
import com.ovolk.dictionary.presentation.select_languages.components.LanguageCheckBox
import com.ovolk.dictionary.presentation.select_languages.components.PreferredLanguages

@Composable
fun SelectLanguagesTo(state: LanguageState, onAction: (LanguagesToActions) -> Unit) {
    Column {
        Header(title = "select native language", wiBackButton = false)

        Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_gutter))) {
            SearchBar(
                onSearch = { query -> onAction(LanguagesToActions.OnSearchLanguagesTo(query)) },
                onPressCross = { onAction(LanguagesToActions.OnSearchLanguagesTo("")) }
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.small_gutter)
            ),
            modifier = Modifier.weight(1f)
        ) {
            item {
                PreferredLanguages(
                    langList = state.preferredLanguages,
                    onCheck = { language -> onAction(LanguagesToActions.ToggleCheck(language)) })
            }
            items(state.filteredLanguageList) { language ->
                LanguageCheckBox(
                    language = language,
                    onCheck = { language -> onAction(LanguagesToActions.ToggleCheck(language)) }
                )
            }
        }
        Button(
            onClick = { onAction(LanguagesToActions.NavigateToLanguagesToFrom) },
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
fun SelectLanguagePreview() {
    SelectLanguagesTo(
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