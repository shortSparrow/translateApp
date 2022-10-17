package com.ovolk.dictionary.presentation.settings_languages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.select_languages.components.Header
import com.ovolk.dictionary.presentation.settings_languages.SettingsLanguagesAction
import com.ovolk.dictionary.presentation.settings_languages.SettingsLanguagesState
import com.ovolk.dictionary.util.compose.OnLifecycleEvent
import timber.log.Timber

@Composable
fun SettingsLanguageScreen(
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
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                onAction(SettingsLanguagesAction.OnFocusScreen)
                Timber.d("RESUME")
            }
            else -> {}
        }
    }

    Column {
        Header(title = "Select translate languages", wiBackButton = true)
        Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Translate lang to:", modifier = padding)

                if (state.languagesTo.isNotEmpty()) {
                    LazyRow {
                        items(state.languagesTo) {
                            OutlinedButton(
                                onClick = { langPress(LanguagesType.LANG_TO) },
                                modifier = padding
                            ) {
                                Text(text = it.langCode)
                            }
                        }
                    }
                } else {
                    OutlinedButton(onClick = {  langPress(LanguagesType.LANG_TO) }) {
                        Text(text = "select lang")
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Translate lang from:", modifier = padding)
                if (state.languagesFrom.isNotEmpty()) {
                    LazyRow {
                        items(state.languagesFrom) {
                            OutlinedButton(
                                onClick = { langPress(LanguagesType.LANG_FROM) },
                                modifier = padding
                            ) {
                                Text(text = it.langCode)
                            }
                        }
                    }
                } else {
                    OutlinedButton(onClick = {  langPress(LanguagesType.LANG_FROM) }) {
                        Text(text = "select lang")
                    }
                }

            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingsLanguageScreenPreview() {
    SettingsLanguageScreen(
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