package com.ovolk.dictionary.presentation.dictionary_list.components

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.LoadingState
import com.ovolk.dictionary.domain.model.dictionary.SelectableDictionary
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.dictionaries.NoDictionaries
import com.ovolk.dictionary.presentation.core.floating.AddButton
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.core.header.TwoButtonOffset
import com.ovolk.dictionary.presentation.dictionary_list.DictionaryListAction
import com.ovolk.dictionary.presentation.dictionary_list.DictionaryListState

@Composable
fun SettingsDictionariesPresenter(
    state: DictionaryListState,
    onAction: (DictionaryListAction) -> Unit,
    onBack: () -> Unit
) {

    if (state.isDeleteDictionaryModalOpen) {
        val dictionaries = state.dictionaryList.filter { it.isSelected }
        val selectedDictionary = dictionaries.joinToString(", ") { it.title }

        val isDeletedActive = dictionaries.find { it.isActive } != null
        val deleteActiveDictionaryInfo = if (isDeletedActive) {
            "\n \n ${stringResource(id = R.string.dictionary_list_screen_remove_active_dictionary)}"
        } else ""

        val description =
            pluralStringResource(
                id = R.plurals.dictionary_list_delete_dictionary_description,
                count = dictionaries.size,
            ) + deleteActiveDictionaryInfo

        ConfirmDialog(
            title = pluralStringResource(
                id = R.plurals.dictionary_list_delete_dictionary_title,
                count = dictionaries.size,
                selectedDictionary
            ),
            description = description,
            descriptionColor = colorResource(id = R.color.red),
            onAcceptClick = { onAction(DictionaryListAction.DeleteDictionary) },
            onDeclineClick = {
                onAction(DictionaryListAction.ToggleOpenDeleteDictionaryModal(false))
            }
        )
    }

    val firstRightIcon: (@Composable () -> Unit)? =
        if (state.dictionaryList.filter { it.isSelected }.size == 1) {
            {
                Icon(
                    painter = painterResource(R.drawable.edit),
                    stringResource(id = R.string.lists_screen_cd_rename_selected_list),
                    tint = colorResource(R.color.grey),
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
            }
        } else null


    val secondRightIcon: (@Composable () -> Unit)? =
        if (state.dictionaryList.any { it.isSelected }) {
            {
                Icon(
                    painter = painterResource(R.drawable.delete_active),
                    stringResource(id = R.string.lists_screen_cd_deleted_selected_lists),
                    tint = colorResource(R.color.red),
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                )
            }
        } else null

    Scaffold(
        floatingActionButton = {
            AddButton(
                onClick = { onAction(DictionaryListAction.AddNewDictionary) },
                contentDescription = stringResource(id = R.string.add_new_dictionary)
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Header(
                titleHorizontalOffset = TwoButtonOffset,
                withBackButton = true,
                onBackButtonClick = onBack,
                title = stringResource(id = R.string.dictionary_list_screen_title),
                secondRightIcon = secondRightIcon,
                onSecondRightIconClick = {
                    onAction(DictionaryListAction.ToggleOpenDeleteDictionaryModal(true))
                },
                firstRightIcon = firstRightIcon,
                onFirstRightIconClick = { onAction(DictionaryListAction.EditDictionary) },
            )

            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.gutter))) {

                if (state.loadingState == LoadingState.SUCCESS) {

                    if (state.dictionaryList.isEmpty()) {
                        Box(modifier = Modifier.padding(bottom = 45.dp)) {
                            NoDictionaries(onPressAddNewDictionary = { onAction(DictionaryListAction.AddNewDictionary) })
                        }
                    }

                    if (state.dictionaryList.isNotEmpty()) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsLanguageScreenPreview() {
    SettingsDictionariesPresenter(
        state = DictionaryListState(
            dictionaryList = listOf(
                SelectableDictionary(
                    id = 0L,
                    title = "EN-UA",
                    isActive = true,
                    isSelected = false,
                    langFromCode = "EN",
                    langToCode = "UA",
                )
            ),
            loadingState = LoadingState.SUCCESS,
        ),
        onAction = {},
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsLanguageScreenPreview2() {
    SettingsDictionariesPresenter(
        state = DictionaryListState(
            dictionaryList = listOf(
                SelectableDictionary(
                    id = 0L,
                    title = "FR-PL",
                    isActive = false,
                    isSelected = false,
                    langFromCode = "FR",
                    langToCode = "PL",
                ),
                SelectableDictionary(
                    id = 1L,
                    title = "EN-UA",
                    isActive = false,
                    isSelected = true,
                    langFromCode = "EN",
                    langToCode = "UA",
                )
            ),
            loadingState = LoadingState.SUCCESS,
        ),
        onAction = {},
        onBack = {}
    )
}


@Preview(showBackground = true)
@Composable
fun SettingsLanguageScreenPreview3() {
    SettingsDictionariesPresenter(
        state = DictionaryListState(
            dictionaryList = listOf(),
            loadingState = LoadingState.SUCCESS,
        ),
        onAction = {},
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsLanguageScreenPreview4() {
    SettingsDictionariesPresenter(
        state = DictionaryListState(
            dictionaryList = listOf(
                SelectableDictionary(
                    id = 0L,
                    title = "FR-PL",
                    isActive = false,
                    isSelected = false,
                    langFromCode = "FR",
                    langToCode = "PL",
                ),
                SelectableDictionary(
                    id = 1L,
                    title = "EN-UA",
                    isActive = false,
                    isSelected = true,
                    langFromCode = "EN",
                    langToCode = "UA",
                ),
            ),
            isDeleteDictionaryModalOpen = true
        ),
        onAction = {},
        onBack = {}
    )
}