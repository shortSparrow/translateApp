package com.ovolk.dictionary.presentation.localization.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.presentation.core.SearchBar
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.core.select_language.LanguageListItem
import com.ovolk.dictionary.presentation.localization.LocalizationAction
import com.ovolk.dictionary.presentation.localization.LocalizationState
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewLanguagesList

@Composable
fun LocalizationPresenter(state: LocalizationState, onAction: (LocalizationAction) -> Unit) {
    if (!state.isLoading) {
        Column {
            if (state.isConfirmAppChangeLanguageModalOpen) {
                ConfirmDialog(
                    title = "Change app language to ${state.isConfirmAppChangeLanguage?.nativeName}",
                    onAcceptClick = { onAction(LocalizationAction.OnConfirmChangeAppLanguage(state.isConfirmAppChangeLanguage?.langCode)) },
                    onDeclineClick = {
                        onAction(LocalizationAction.OnCloseConfirmChangeAppLanguageModal)
                    },
                )
            }

            Header(
                title = stringResource(id = R.string.app_localization_title),
                withBackButton = true,
                onBackButtonClick = { onAction(LocalizationAction.OnPressGoBack) },
            )

            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {
                Row(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.gutter))) {
                    Text(text = stringResource(id = R.string.app_localization_search_current_language))

                    Text(text = state.selectedLanguage?.nativeName!!, fontWeight = FontWeight.Bold)
                }


                SearchBar(
                    onSearch = { onAction(LocalizationAction.OnSearchLanguage(it)) },
                    onPressCross = { onAction(LocalizationAction.OnSearchLanguage("")) },
                    placeholderTextId = { Text(text = stringResource(id = R.string.app_localization_search_lang_placeholder)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn() {
                    items(state.languageFilteredList) { language ->
                        LanguageListItem(language = language, onCheck = { lang ->
                            onAction(
                                LocalizationAction.OnOpenConfirmChangeAppLanguageModal(lang)
                            )
                        })
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocalizationPresenterPreview() {
    LocalizationPresenter(
        state = LocalizationState(
            isLoading = false, selectedLanguage = Language(
                langCode = "UK",
                name = "Ukrainian",
                nativeName = "Українська",
                isChecked = true,
            ), languageFilteredList = getPreviewLanguagesList()
        ),
        onAction = {},
    )
}