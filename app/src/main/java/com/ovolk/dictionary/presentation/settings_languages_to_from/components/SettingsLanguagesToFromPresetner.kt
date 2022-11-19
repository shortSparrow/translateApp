package com.ovolk.dictionary.presentation.settings_languages_to_from.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.presentation.core.compose.SearchBar
import com.ovolk.dictionary.presentation.core.compose.languages.PreferredLanguages
import com.ovolk.dictionary.presentation.select_languages.components.LanguageCheckBox
import com.ovolk.dictionary.presentation.settings_languages_to_from.SettingsLanguagesToFromActions
import com.ovolk.dictionary.presentation.settings_languages_to_from.SettingsLanguagesToFromState

@Composable
fun SettingsLanguagesToFromPresenter(
    state: SettingsLanguagesToFromState,
    onAction: (SettingsLanguagesToFromActions) -> Unit,
) {
    Column {
        Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_gutter))) {
            SearchBar(
                onSearch = { query ->
                    onAction(
                        SettingsLanguagesToFromActions.OnSearchLanguagesToFrom(
                            query
                        )
                    )
                },
                onPressCross = { onAction(SettingsLanguagesToFromActions.OnSearchLanguagesToFrom("")) }
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.small_gutter)
            ),
            modifier = Modifier.weight(1f)
        ) {
            if (state.selectedLanguages.isNotEmpty()) {
                item {
                    PreferredLanguages(
                        languages = state.selectedLanguages,
                        onCheck = { language ->
                            onAction(
                                SettingsLanguagesToFromActions.ToggleCheck(
                                    language
                                )
                            )
                        },
                        title = stringResource(id = R.string.settings_languages_from_to_selected_lang)
                    )
                }
            }

            items(state.filteredLanguageList) { language ->
                LanguageCheckBox(
                    language = language,
                    onCheck = { language ->
                        onAction(
                            SettingsLanguagesToFromActions.ToggleCheck(
                                language
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsLanguagesToScreenPreview() {
    SettingsLanguagesToFromPresenter(
        state = SettingsLanguagesToFromState(
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
            selectedLanguages = listOf(
                Language(
                    langCode = "uk",
                    name = "Ukrainian",
                    nativeName = "українська",
                    isChecked = true
                ),

                )
        ),
        onAction = {},
    )
}