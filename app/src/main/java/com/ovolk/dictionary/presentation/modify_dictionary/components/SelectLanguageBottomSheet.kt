package com.ovolk.dictionary.presentation.modify_dictionary.components

import androidx.activity.compose.BackHandler
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
import com.ovolk.dictionary.domain.model.select_languages.Language
import com.ovolk.dictionary.presentation.core.select_language.SelectLanguages
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectLanguageBottomSheet(
    languageList: List<Language>,
    preferredLanguages: List<Language>,
    isBottomSheetOpen: Boolean,
    body: @Composable () -> Unit,
    onSearchLanguage: (query: String) -> Unit,
    closeLanguageBottomSheet: () -> Unit,
    onSelectLanguage: (langCode: String) -> Unit
) {
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val scope = rememberCoroutineScope()

    fun clearLanguageSearchField() {
        onSearchLanguage("")
    }

    fun closeModal() {
        scope.launch {
            clearLanguageSearchField()
            bottomSheetScaffoldState.hide()
            closeLanguageBottomSheet()
        }
    }

    if(bottomSheetScaffoldState.isVisible) {
        BackHandler() {
            closeModal()
        }
    }
    LaunchedEffect(isBottomSheetOpen) {
        scope.launch {
            if (isBottomSheetOpen) {
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
        sheetShape = MaterialTheme.shapes.small,
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
                    SelectLanguages(
                        languageList = languageList,
                        onSelect = { langCode ->
                            scope.launch {
                                onSelectLanguage(langCode)
                                delay(400)
                                closeModal()
                            }
                        },
                        onSearch = { query -> onSearchLanguage(query) },
                        shouldClearSearchField = bottomSheetScaffoldState.currentValue == ModalBottomSheetValue.Hidden,
                        preferredLanguages = preferredLanguages
                    )
                }
            }
        },
    ) {
        body()
    }
}
