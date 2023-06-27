package com.ovolk.dictionary.presentation.modify_word

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarSuccess
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.modify_word.compose.ModifyWordPresenter
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes
import com.ovolk.dictionary.util.compose.BackHandler

@Composable
fun ModifyWordScreen(navController: NavHostController) {

    fun listener() = object : ModifyWordViewModel.Listener {
        override fun onDeleteWord() {
            // TODO maye add shack bars for all deletes
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

    val viewModel = hiltViewModel<ModifyWordViewModel>()
    val state = viewModel.composeState
    val translateState = viewModel.translateState
    val hintState = viewModel.hintState
    val recordState = viewModel.recordAudio.recordState


    if (viewModel.listener == null) {
        viewModel.listener = listener()
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