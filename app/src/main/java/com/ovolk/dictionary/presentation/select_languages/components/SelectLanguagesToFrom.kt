package com.ovolk.dictionary.presentation.select_languages.components

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
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.compose.SearchBar
import com.ovolk.dictionary.presentation.core.compose.languages.PreferredLanguages
import com.ovolk.dictionary.presentation.select_languages.LanguageToFromState
import com.ovolk.dictionary.presentation.select_languages.LanguagesToFromActions

@Composable
fun SelectLanguagesToFrom(title: String, state: LanguageToFromState, onAction: (LanguagesToFromActions) -> Unit) {
    Column {
        Header(title = title, wiBackButton = state.headerWithBackButton)

        Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_gutter))) {
            SearchBar(
                onSearch = { query -> onAction(LanguagesToFromActions.OnSearchLanguagesTo(query)) },
                onPressCross = { onAction(LanguagesToFromActions.OnSearchLanguagesTo("")) }
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.small_gutter)
            ),
            modifier = Modifier.weight(1f)
        ) {
            if (state.type == LanguagesType.LANG_TO && state.preferredLanguages.isNotEmpty()) {
                item {
                    PreferredLanguages(
                        languages = state.preferredLanguages,
                        onCheck = { language -> onAction(LanguagesToFromActions.ToggleCheck(language)) },
                        title = "Preferred languages"
                    )
                }
            }

            items(state.filteredLanguageList) { language ->
                LanguageCheckBox(
                    language = language,
                    onCheck = { language -> onAction(LanguagesToFromActions.ToggleCheck(language)) }
                )
            }
        }
        Button(
            onClick = { onAction(LanguagesToFromActions.GoNext) },
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
fun SelectLanguagesToFromPreview() {
    SelectLanguagesToFrom(
        title="select native language",
        state = LanguageToFromState(
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