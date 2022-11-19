package com.ovolk.dictionary.presentation.settings_languages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.compose.header.Header
import com.ovolk.dictionary.presentation.settings_languages.SettingsLanguagesAction
import com.ovolk.dictionary.presentation.settings_languages.SettingsLanguagesState
import com.ovolk.dictionary.util.compose.OnLifecycleEvent

@Composable
fun SettingsLanguagePresenter(
    state: SettingsLanguagesState,
    onAction: (SettingsLanguagesAction) -> Unit
) {
    val padding = Modifier.padding(
        horizontal = 4.dp
    )

    fun langPress(type: LanguagesType) {
        onAction(
            SettingsLanguagesAction.EditSelectedLanguages(type)
        )
    }

    // on focus load lang
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                onAction(SettingsLanguagesAction.OnFocusScreen)
            }
            else -> {}
        }
    }

    Column {
        Header(title = stringResource(id = R.string.settings_languages_title), withBackButton = true)
        Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            Column {
                Text(
                    text = stringResource(id = R.string.settings_languages_translate_language_to),
                    modifier = padding
                )

                if (state.languagesTo.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp)
                    ) {
                        items(state.languagesTo) {
                            OutlinedButton(
                                onClick = { langPress(LanguagesType.LANG_TO) },
                                modifier = padding
                            ) {
                                Text(text = it.langCode.uppercase())
                            }
                        }
                    }
                } else {
                    OutlinedButton(onClick = { langPress(LanguagesType.LANG_TO) }) {
                        Text(text = stringResource(id = R.string.settings_languages_select_language))
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            Column {
                Text(
                    text = stringResource(id = R.string.settings_languages_translate_language_from),
                    modifier = padding
                )
                if (state.languagesFrom.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp)
                    ) {
                        items(state.languagesFrom) {
                            OutlinedButton(
                                onClick = { langPress(LanguagesType.LANG_FROM) },
                                modifier = padding
                            ) {
                                Text(text = it.langCode.uppercase())
                            }
                        }
                    }
                } else {
                    OutlinedButton(onClick = { langPress(LanguagesType.LANG_FROM) }) {
                        Text(text = stringResource(id = R.string.settings_languages_select_language))
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 10.dp))
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingsLanguageScreenPreview() {
    SettingsLanguagePresenter(
        state = SettingsLanguagesState(
            languagesTo = listOf(
                Language(langCode = "UA", name = "Ukrainian", nativeName = "Українська"),
                Language(langCode = "PL", name = "Polish", nativeName = "Polski"),
            ),
            languagesFrom = listOf(
                Language(langCode = "EN", name = "English", nativeName = "English"),
            ),
        ),
        onAction = {}
    )
}