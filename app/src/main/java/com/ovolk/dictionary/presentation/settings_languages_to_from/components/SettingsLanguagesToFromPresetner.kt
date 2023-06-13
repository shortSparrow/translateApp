package com.ovolk.dictionary.presentation.settings_languages_to_from.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.presentation.core.SearchBar
import com.ovolk.dictionary.presentation.select_languages.components.LanguageCheckBox

@Composable
fun SettingsLanguagesToFromPresenter(
    languageList: List<Language>,
    onSearch: (value: String) -> Unit,
    onSelect: (langCode: String) -> Unit,
    shouldClearSearchField: Boolean
) {

    var searchText by remember {
        mutableStateOf("")
    }
//    val filteredLanguageList = remember { mutableStateListOf<Language>() }

//    LaunchedEffect(Unit) {
//        filteredLanguageList.addAll(languageList)
//    }
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
                //                onSearch = { query ->
//                    val newList = languageList.filter {
//                        it.name.lowercase()
//                            .contains(query.lowercase()) || it.nativeName.lowercase()
//                            .contains(query.lowercase())
//                    }
//
//                    filteredLanguageList.clear()
//                    filteredLanguageList.addAll(newList.toMutableStateList())
//                },
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.small_gutter)
            ),
            modifier = Modifier.weight(1f)
        ) {
//            if (state.selectedLanguages.isNotEmpty()) {
//                item {
//                    PreferredLanguages(
//                        languages = listOf(selectedLang as Language), // TODO remove list
//                        onCheck = { language ->
//                            onAction(
//                                SettingsLanguagesToFromActions.ToggleCheck(
//                                    language
//                                )
//                            )
//                        },
//                        title = stringResource(id = R.string.settings_languages_from_to_selected_lang)
//                    )
//                }
//            }

            items(languageList) { language ->
                LanguageCheckBox(
                    language = language,
                    onCheck = { language -> onSelect(language.langCode) }
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SettingsLanguagesToScreenPreview() {
//    SettingsLanguagesToFromPresenter(
//        state = SettingsLanguagesToFromState(
//            filteredLanguageList = listOf(
//                Language(
//                    langCode = "rm",
//                    name = "Romansh",
//                    nativeName = "rumantsch grischun"
//                ),
//                Language(
//                    langCode = "sd",
//                    name = "Sindhi",
//                    nativeName = "सिन्धी"
//                ),
//                Language(
//                    langCode = "uk",
//                    name = "Ukrainian",
//                    nativeName = "українська",
//                    isChecked = true
//                ),
//                Language(
//                    langCode = "en",
//                    name = "English",
//                    nativeName = "English"
//                ),
//                Language(
//                    langCode = "se",
//                    name = "Northern Sami",
//                    nativeName = "Davvisámegiella"
//                ),
//                Language(
//                    langCode = "sm",
//                    name = "Samoan",
//                    nativeName = "gagana faa Samoa"
//                ),
//            ),
//            selectedLanguages = listOf(
//                Language(
//                    langCode = "uk",
//                    name = "Ukrainian",
//                    nativeName = "українська",
//                    isChecked = true
//                ),
//
//                )
//        ),
//        onAction = {},
//    )
//}