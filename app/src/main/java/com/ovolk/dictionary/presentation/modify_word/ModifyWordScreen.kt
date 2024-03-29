package com.ovolk.dictionary.presentation.modify_word

import androidx.activity.compose.BackHandler
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarSuccess
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.modify_word.compose.ModifyWordPresenter
import com.ovolk.dictionary.presentation.modify_word.view_model.ModifyWordViewModel
import com.ovolk.dictionary.presentation.navigation.graph.CommonRotes

@Composable
fun ModifyWordScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<ModifyWordViewModel>()
    val state by viewModel.globalState.collectAsState()
    val translateState by viewModel.translateState.collectAsState()
    val hintState by viewModel.hintState.collectAsState()
    val recordState by viewModel.recordState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.listener = object : ModifyWordViewModel.Listener {
            override fun onDeleteWord() {
                // TODO maye add snack bars for all deletes
                GlobalSnackbarManger.showGlobalSnackbar(
                    duration = SnackbarDuration.Short,
                    data = SnackBarSuccess(
                        message = DictionaryApp.applicationContext()
                            .getString(R.string.modify_word_success_delete_word)
                    ),
                )
                navController.popBackStack()
            }

            override fun onSaveWord() {
                GlobalSnackbarManger.showGlobalSnackbar(
                    duration = SnackbarDuration.Short,
                    data = SnackBarSuccess(
                        message = DictionaryApp.applicationContext()
                            .getString(R.string.modify_word_saved_word_success)
                    ),
                )
                navController.popBackStack()
            }

            override fun goBack() {
                navController.popBackStack()
            }

            override fun toAddNewDictionary() {
                navController.navigate("${CommonRotes.MODIFY_DICTIONARY}/mode=${ModifyDictionaryModes.MODE_ADD}")
            }
        }
    }

    BackHandler {
        viewModel.onComposeAction(ModifyWordAction.GoBack())
    }

    ModifyWordPresenter(
        state = state,
        translateState = translateState,
        hintState = hintState,
        recordState = recordState,
        onRecordAction = viewModel::onRecordAction,
        onAction = viewModel::onComposeAction,
        onTranslateAction = viewModel::onTranslateAction,
        onHintAction = viewModel::onHintAction
    )
}