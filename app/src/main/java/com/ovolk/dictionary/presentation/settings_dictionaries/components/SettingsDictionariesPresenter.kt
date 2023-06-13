package com.ovolk.dictionary.presentation.settings_dictionaries.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.core.header.HeaderWithTwoRightActions
import com.ovolk.dictionary.presentation.settings_dictionaries.SettingsDictionariesAction
import com.ovolk.dictionary.presentation.settings_dictionaries.SettingsDictionariesState

@Composable
fun SettingsDictionariesPresenter(
    state: SettingsDictionariesState,
    onAction: (SettingsDictionariesAction) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            AddButton(
                onClick = { onAction(SettingsDictionariesAction.AddNewDictionary) },
                contentDescription = "ff"
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            HeaderWithTwoRightActions(
                onBack = onBack,
                title = "Select dictionary",
                firstButton = {
                    if (state.dictionaryList.any { it.isSelected }) {
                        Icon(
                            painter = painterResource(R.drawable.delete_active),
                            stringResource(id = R.string.lists_screen_cd_deleted_selected_lists),
                            tint = colorResource(R.color.red),
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp)
                        )
                    }
                },
                onFirstButtonClick = { onAction(SettingsDictionariesAction.DeleteDictionary) },
                secondButton = {
                    if (state.dictionaryList.any { it.isSelected }) {
                        Icon(
                            painter = painterResource(R.drawable.edit),
                            stringResource(id = R.string.lists_screen_cd_rename_selected_list),
                            tint = colorResource(R.color.grey),
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                        )
                    }
                },
                onSecondButtonClick = { onAction(SettingsDictionariesAction.EditDictionary) },
            )
            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {

                LazyColumn {
                    items(state.dictionaryList) { dictionary ->
                        DictionaryItem(
                            dictionary = dictionary,
                            isSomeDictionarySelected = state.dictionaryList.any { it.isSelected },
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsLanguageScreenPreview() {
    SettingsDictionariesPresenter(
        state = SettingsDictionariesState(
            dictionaryList = listOf(
                Dictionary(
                    id = 0L,
                    title = "EN-UA",
                    isActive = true,
                    isSelected = false,
                    langFromCode = "EN",
                    langToCode = "UA",
                )
            )
        ),
        onAction = {},
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsLanguageScreenPreview2() {
    SettingsDictionariesPresenter(
        state = SettingsDictionariesState(
            dictionaryList = listOf(
                Dictionary(
                    id = 0L,
                    title = "FR-PL",
                    isActive = false,
                    isSelected = false,
                    langFromCode = "FR",
                    langToCode = "PL",
                ),
                Dictionary(
                    id = 1L,
                    title = "EN-UA",
                    isActive = false,
                    isSelected = true,
                    langFromCode = "EN",
                    langToCode = "UA",
                )
            )
        ),
        onAction = {},
        onBack = {}
    )
}