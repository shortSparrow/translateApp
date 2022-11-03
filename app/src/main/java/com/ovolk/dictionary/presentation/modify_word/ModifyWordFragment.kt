package com.ovolk.dictionary.presentation.modify_word

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentModifyWordBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.core.ConfirmDialog
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
    private val confirmDialog by lazy { ConfirmDialog(context = requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchRightMode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setContent {
            val viewModel = hiltViewModel<ModifyWordViewModel>()
            val state = viewModel.composeState
            val languageState = viewModel.languageState
//            val translatesState = viewModel.translateState
//            val hintState = viewModel.hintState

            AppCompatTheme {
                ModifyWordScreen(
                    state = state,
                    languageState = languageState,
//                    translatesState = translatesState,
//                    hintState = hintState,
                    onAction = viewModel::onComposeAction,
                    onTranslateAction = viewModel::onTranslateAction,
                    onHintAction = viewModel::onHintAction
                )
            }
        }
    }

    override fun onAudioGranted() {
        openRecordBottomSheet()
    }

    override fun showMessage(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun launchRightMode() {
//        when (args.mode) {
//            ModifyWordModes.MODE_EDIT -> viewModel.launchEditMode(args.wordId)
//            ModifyWordModes.MODE_ADD -> viewModel.launchAddMode(
//                args.wordValue,
//                listId = args.listId // when open fragment from word list fragment
//            )
//        }
    }

//    private fun setupView() = with(binding) {
//        recordEnglishPronunciation.setOnClickListener {
//            audioResolver.requestPermission(
//                requireContext()
//            )
//        }
//
//        confirmDialog
//            .setTitle(getString(R.string.modify_word_confirm_delete_title))
//            .handleOkClick {
//                viewModel.deleteWord(wordId = args.wordId)
//                Toast.makeText(
//                    binding.root.context,
//                    getString(R.string.modify_word_success_delete_word),
//                    Toast.LENGTH_SHORT
//                ).show()
//                findNavController().popBackStack()
//            }
//            .handleOutsideClick { viewModel.setIsOpenedDeleteModal(false) }
//            .handleCancelClick { viewModel.setIsOpenedDeleteModal(false) }
//    }


    private fun openRecordBottomSheet() {
//        val recordSheetDialog = RecordAudioBottomSheet()
//        recordSheetDialog.arguments = Bundle().apply {
//            putString(
//                RecordAudioBottomSheet.MODIFIED_FILE_NAME,
//                viewModel.getAudioFileName()
//            )
//            putString(
//                RecordAudioBottomSheet.WORD,
//                binding.inputTranslatedWord.englishWordInput.text.toString()
//            )
//        }
//        recordSheetDialog.show(requireActivity().supportFragmentManager, RecordAudioBottomSheet.TAG)
//        setRecordAudioBottomSheetClick(recordSheetDialog)
    }

//
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