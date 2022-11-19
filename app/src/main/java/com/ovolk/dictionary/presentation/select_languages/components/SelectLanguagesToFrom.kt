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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.compose.SearchBar
import com.ovolk.dictionary.presentation.core.compose.header.Header
import com.ovolk.dictionary.presentation.core.compose.header.OneButtonOffset
import com.ovolk.dictionary.presentation.core.compose.header.TwoButtonOffset
import com.ovolk.dictionary.presentation.core.compose.header.ZeroButtonOffset
import com.ovolk.dictionary.presentation.core.compose.languages.PreferredLanguages
import com.ovolk.dictionary.presentation.select_languages.LanguageToFromState
import com.ovolk.dictionary.presentation.select_languages.LanguagesToFromActions

@Composable
fun SelectLanguagesToFrom(
    title: String,
    state: LanguageToFromState,
    onAction: (LanguagesToFromActions) -> Unit,
    navController: NavHostController
) {
    Column {
        Header(
            title = title,
            withBackButton = state.headerWithBackButton,
            navController = navController,
            titleHorizontalOffset = if (state.headerWithBackButton) OneButtonOffset else ZeroButtonOffset
        )

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
                        title = stringResource(id = R.string.settings_languages_from_to_preferred_lang)
                    )
                }
            }

            items(state.filteredLanguageList) { language ->
                LanguageCheckBox(
                    language = language,
                    onCheck = { lang -> onAction(LanguagesToFromActions.ToggleCheck(lang)) }
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
            Text(
                text = stringResource(id = R.string.next).uppercase(),
                color = colorResource(id = R.color.white)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectLanguagesToFromPreview() {
    SelectLanguagesToFrom(
        title = "select native language",
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
        navController = rememberNavController(),
        onAction = {}
    )
}