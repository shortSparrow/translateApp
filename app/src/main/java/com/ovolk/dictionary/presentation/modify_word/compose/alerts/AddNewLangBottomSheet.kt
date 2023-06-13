package com.ovolk.dictionary.presentation.modify_word.compose.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.modify_word.AddNewLangModal
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.presentation.settings_languages_to_from.SettingsLanguagesToFromViewModel
import com.ovolk.dictionary.presentation.settings_languages_to_from.components.SettingsLanguagesToFromPresenter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddNewLangBottomSheet(
    state: AddNewLangModal,
    onAction: (ModifyWordAction) -> Unit,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            BottomSheetValue.Collapsed,
        ),
    )
    var initialOpen by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val viewModel = hiltViewModel<SettingsLanguagesToFromViewModel>()

    fun closeModal() {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
            delay(100)
            onAction(ModifyWordAction.CloseAddNewLanguageModal)
            viewModel.clearCurrentType()
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(100)
            bottomSheetScaffoldState.bottomSheetState.expand()
            initialOpen = true
        }
    }

    Dialog(
        onDismissRequest = { closeModal() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            BottomSheetScaffold(
                backgroundColor = Color.Transparent,
                scaffoldState = bottomSheetScaffoldState,
                modifier = Modifier.padding(top = 70.dp),
                sheetContent = {
                    if (state.type == null) return@BottomSheetScaffold

                    viewModel.setCurrentType(state.type)
                    val languageState = viewModel.state

                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed && initialOpen) {
                        closeModal()
                    }

                    Column(modifier = Modifier.padding(top = 15.dp)) {
                        Divider(
                            modifier = Modifier
                                .height(5.dp)
                                .width(60.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .align(Alignment.CenterHorizontally)
                                .background(colorResource(id = R.color.light_grey))
                        )


                        Box(modifier = Modifier.padding(top = 25.dp)) {
//                            SettingsLanguagesToFromPresenter(
//                                state = languageState,
//                                onAction = viewModel::onAction
//                            )
                        }
                    }
                },
                sheetPeekHeight = 0.dp
            ) {
                // TODO change on Coulmn and make slide
                Box {
                }
            }
        }
    }
}
