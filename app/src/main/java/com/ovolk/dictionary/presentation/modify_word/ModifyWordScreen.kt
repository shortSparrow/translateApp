package com.ovolk.dictionary.presentation.modify_word

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.DictionaryApp

@Composable
fun ModifyWordScreen(navController: NavHostController) {

    fun showMessage(text: String) {
        Toast.makeText(
            DictionaryApp.applicationContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }


    fun listener() = object : ModifyWordViewModel.Listener {
        override fun onDeleteWord() {
            showMessage(
                DictionaryApp.applicationContext()
                    .getString(R.string.modify_word_success_delete_word)
            )
            navController.popBackStack()
        }

        override fun onSaveWord() {
            showMessage(
                DictionaryApp.applicationContext()
                    .getString(R.string.modify_word_saved_word_success)
            )
            navController.popBackStack()
        }
    }


    val viewModel = hiltViewModel<ModifyWordViewModel>()
    val state = viewModel.composeState
    val languageState = viewModel.languageState
    val translateState = viewModel.translateState
    val hintState = viewModel.hintState
    val recordState = viewModel.recordAudio.recordState


    if (viewModel.listener == null) {
        viewModel.listener = listener()
    }

    ModifyWordPresenter(
        state = state,
        languageState = languageState,
        translateState = translateState,
        hintState = hintState,
        recordState = recordState,
        onRecordAction = viewModel::onRecordAction,
        onAction = viewModel::onComposeAction,
        onTranslateAction = viewModel::onTranslateAction,
        onHintAction = viewModel::onHintAction
    )
    // TODO add
//        audioResolver.requestPermission(
//            requireContext()
//        )
}