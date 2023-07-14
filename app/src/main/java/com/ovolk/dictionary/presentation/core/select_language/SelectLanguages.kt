package com.ovolk.dictionary.presentation.core.select_language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.presentation.core.SearchBar
import com.ovolk.dictionary.presentation.core.languages.PreferredLanguages
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewLanguagesList

@Composable
fun SelectLanguages(
    languageList: List<Language>,
    onSearch: (value: String) -> Unit,
    onSelect: (langCode: String) -> Unit,
    shouldClearSearchField: Boolean,
    preferredLanguages: List<Language>
) {

    var searchText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(shouldClearSearchField) {
        searchText = ""
    }


    Column {
        Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_gutter))) {
            SearchBar(
                onSearch = { query ->
                    searchText = query; onSearch(searchText)
                },
                searchedValue = searchText,
                onPressCross = { onSearch("") }
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.small_gutter)
            ),
            modifier = Modifier.weight(1f)
        ) {
            if (preferredLanguages.isNotEmpty()) {
                item {
                    PreferredLanguages(
                        languages = preferredLanguages,
                        onCheck = { language -> onSelect(language.langCode) },
                        title = stringResource(id = R.string.settings_languages_preferred_languages)
                    )
                }
            }

            items(languageList) { language ->
                LanguageListItem(
                    language = language,
                    onCheck = { language -> onSelect(language.langCode) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsLanguagesToScreenPreview() {
    SelectLanguages(
        languageList = getPreviewLanguagesList(),
        shouldClearSearchField = false,
        onSearch = {},
        onSelect = {},
        preferredLanguages = listOf(
            Language(
                langCode = "uk",
                name = "Ukrainian",
                nativeName = "українська",
                isChecked = true
            ),
        )
    )
}