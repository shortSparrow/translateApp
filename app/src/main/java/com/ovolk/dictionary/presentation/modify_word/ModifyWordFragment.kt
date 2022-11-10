package com.ovolk.dictionary.presentation.modify_word

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.R
import com.ovolk.dictionary.databinding.FragmentModifyWordBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates.notNull

enum class ModifyWordModes { MODE_ADD, MODE_EDIT }

@AndroidEntryPoint
class ModifyWordFragment : BaseFragment<FragmentModifyWordBinding>(),
    AudioPermissionResolver.ResultListener {
    override val bindingInflater: BindingInflater<FragmentModifyWordBinding>
        get() = FragmentModifyWordBinding::inflate

    private val args by navArgs<ModifyWordFragmentArgs>()
    private val viewModel by viewModels<ModifyWordViewModel>()

    private var audioResolver: AudioPermissionResolver by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchRightMode()
        audioResolver = AudioPermissionResolver(requireActivity().activityResultRegistry, this)
        lifecycle.addObserver(audioResolver)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setContent {
            val viewModel = hiltViewModel<ModifyWordViewModel>()
            val state = viewModel.composeState
            val languageState = viewModel.languageState
            val translateState = viewModel.translateState
            val hintState = viewModel.hintState
//            val recordState = viewModel.recordState
            val recordState = viewModel.recordAudio.recordState


            if (viewModel.listener == null) {
                viewModel.listener = listener()
            }

            AppCompatTheme {
                ModifyWordScreen(
                    state = state,
                    languageState = languageState,
                    translateState = translateState,
                    hintState = hintState,
                    recordState=recordState,
                    onRecordAction=viewModel::onRecordAction,
                    onAction = viewModel::onComposeAction,
                    onTranslateAction = viewModel::onTranslateAction,
                    onHintAction = viewModel::onHintAction
                )
            }
        }
        audioResolver.requestPermission(
            requireContext()
        )
    }

    fun listener() = object : ModifyWordViewModel.Listener {
        override fun onDeleteWord() {
            showMessage(getString(R.string.modify_word_success_delete_word))
            findNavController().popBackStack()
        }

        override fun onSaveWord() {
            showMessage(getString(R.string.modify_word_saved_word_success))
            findNavController().popBackStack()
        }
    }

    override fun onAudioGranted() {

    }

    override fun showMessage(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun launchRightMode() {
        when (args.mode) {
            ModifyWordModes.MODE_EDIT -> viewModel.launchEditMode(args.wordId)
            ModifyWordModes.MODE_ADD -> viewModel.launchAddMode(
                args.wordValue,
                listId = args.listId // when open fragment from word list fragment
            )
        }
    }


//    private fun setRecordAudioBottomSheetClick(recordSheetDialog: RecordAudioBottomSheet) {
//        recordSheetDialog.callbackListener = object : RecordAudioBottomSheet.CallbackListener {
//            override fun saveAudio(fileName: String?) {
//                if (fileName != null) {
//                    Toast.makeText(
//                        requireContext(),
//                        getString(R.string.add_audio_success),
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                    viewModel.updateAudio(fileName)
//                } else {
//                    viewModel.updateAudio(null)
//                }
//                updateMicrophoneIcon(fileName)
//            }
//        }
//    }
//
//    private fun updateMicrophoneIcon(fileName: String?) = with(binding) {
//        if (fileName == null) {
//            recordEnglishPronunciation.setImageResource(R.drawable.mic_active)
//            isRecordAdded.visibility = View.INVISIBLE
//        } else {
//            recordEnglishPronunciation.setImageResource(R.drawable.mic_successful)
//            isRecordAdded.visibility = View.VISIBLE
//        }
//    }


}