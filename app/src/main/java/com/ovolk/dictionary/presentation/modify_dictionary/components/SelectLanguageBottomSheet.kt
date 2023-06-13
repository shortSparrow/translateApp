package com.ovolk.dictionary.presentation.modify_dictionary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryAction
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryState
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToFromPresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectLanguageBottomSheet(
    state: ModifyDictionaryState,
    onAction: (ModifyDictionaryAction) -> Unit,
    body: @Composable () -> Unit
) {
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val scope = rememberCoroutineScope()

    fun clearLanguageSearchField() {
        onAction(ModifyDictionaryAction.OnSearchLanguage(""))
    }
    fun closeModal() {
        scope.launch {
            clearLanguageSearchField()
            bottomSheetScaffoldState.hide()
            onAction(ModifyDictionaryAction.CloseLanguageBottomSheet)
        }
    }

    LaunchedEffect(state.languageBottomSheet.isOpen) {
        scope.launch {
            if (state.languageBottomSheet.isOpen) {
                bottomSheetScaffoldState.show()
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetScaffoldState.currentValue }.collect {
            if (it == ModalBottomSheetValue.Hidden) {
                closeModal()
            }
        }
    }


    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .heightIn(max = 650.dp)
            ) {
                Divider(
                    modifier = Modifier
                        .height(5.dp)
                        .width(60.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .align(Alignment.CenterHorizontally)
                        .background(colorResource(id = R.color.light_grey))
                )

                Box(modifier = Modifier.padding(top = 25.dp)) {
                    SettingsLanguagesToFromPresenter(
                        languageList = state.languageBottomSheet.languageList,
                        onSelect = { langCode ->
                            scope.launch {
                                onAction(ModifyDictionaryAction.OnSelectLanguage(languageCode = langCode))
                                delay(400)
                                closeModal()
                            }
                        },
                        onSearch = { query -> onAction(ModifyDictionaryAction.OnSearchLanguage(query)) },
                        shouldClearSearchField = bottomSheetScaffoldState.currentValue == ModalBottomSheetValue.Hidden
                    )
                }
            }
        },
    ) {
        body()
    }
}
