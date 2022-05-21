package com.example.ttanslateapp.presentation.modify_word

import android.Manifest.permission
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentModifyWordBinding
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.modify_word_chip.HintItem
import com.example.ttanslateapp.domain.model.modify_word_chip.TranslateWordItem
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.core.RecordAudioBottomSheet
import com.example.ttanslateapp.presentation.modify_word.adapter.ModifyWordAdapter
import com.example.ttanslateapp.presentation.modify_word.adapter.hints.HintAdapter
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import com.example.ttanslateapp.util.ScrollEditTextInsideScrollView
import com.example.ttanslateapp.util.getAppComponent
import com.example.ttanslateapp.util.setOnTextChange
import timber.log.Timber


enum class ModifyWordModes {
    MODE_ADD, MODE_EDIT
}

class ModifyWordFragment : BaseFragment<FragmentModifyWordBinding>() {
    override val bindingInflater: BindingInflater<FragmentModifyWordBinding>
        get() = FragmentModifyWordBinding::inflate

    private val args by navArgs<ModifyWordFragmentArgs>()

    private val viewModel by viewModels {
        get(ModifyWordViewModel::class.java)
    }
    private val recordPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val audio = permissions[permission.RECORD_AUDIO]

            if (audio == true) {
                openRecordBottomSheet()
            } else {
                val showRationaleRecord =
                    shouldShowRequestPermissionRationale(permission.RECORD_AUDIO)

                if (!showRationaleRecord) {
                    Toast.makeText(context, "enable RECORD_AUDIO", Toast.LENGTH_SHORT).show()
                }
            }
        }


    private val translateAdapter = TranslateAdapter()
    private val hintAdapter = HintAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        launchRightMode()
        setupClickListener()
        editTextScrollListener()
        setAdaptersClickListener()
        setupView()
        setObservers()
    }

    private fun launchRightMode() {
        when (args.mode) {
            ModifyWordModes.MODE_EDIT -> launchEditMode()
            else -> {}
        }
    }

    private fun launchEditMode() {
        val id = if (args.wordId == -1L) {
            error("wordId is null")
        } else {
            args.wordId
        }

        viewModel.successLoadWordById = object : ModifyWordViewModel.SuccessLoadWordById {
            override fun onLoaded(word: ModifyWord) {
                with(binding) {
                    inputTranslatedWord.englishWordInput.setText(word.value)
                    inputTranslatedWord.englishTranscriptionInput.setText(word.transcription)
                    translateWordDescription.descriptionInput.setText(word.description)

                    if (word.sound != null) {
                        recordEnglishPronunciation.setImageResource(R.drawable.mic_successful)
                        isRecordAdded.visibility = View.VISIBLE
                    }

                    val langList = mutableMapOf<String, Int>()

                    val adapter = inputTranslatedWord.selectLanguageSpinner.adapter
                    for (i in 0 until adapter.count) {
                        langList[adapter.getItem(i).toString()] = i
                    }

                    // FIXME change spinner for text view on edit mode
                    val spinnerValue = langList[word.langFrom] ?: 0
                    inputTranslatedWord.selectLanguageSpinner.setSelection(spinnerValue)
                    inputTranslatedWord.selectLanguageSpinner.isEnabled = false
                }
            }
        }
        viewModel.getWordById(id)
    }

    private fun setupView() = with(binding) {
        inputTranslatedWord.englishWordInput.setOnTextChange { viewModel.setWordValueError(false) }

        addTranslate.translateChipsRv.adapter = translateAdapter
        addTranslate.translateChipsRv.itemAnimator = null
        addTranslate.translateInput.setOnTextChange { viewModel.setTranslatesError(false) }

        addHints.hintChipsRv.adapter = hintAdapter
        addHints.hintChipsRv.itemAnimator = null

        recordEnglishPronunciation.setOnClickListener {
            requestRecordPermission()
        }
    }

    private fun requestRecordPermission() {
        recordPermission.launch(
            arrayOf(
                permission.RECORD_AUDIO,
                permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    private fun openRecordBottomSheet() {
        val recordSheetDialog =
            RecordAudioBottomSheet()

        recordSheetDialog.arguments = Bundle().apply {
            putString(RecordAudioBottomSheet.MODIFIED_FILE_NAME, viewModel.soundFileName)
            putString(
                RecordAudioBottomSheet.WORD,
                binding.inputTranslatedWord.englishWordInput.text.toString()
            )
        }

        recordSheetDialog.show(requireActivity().supportFragmentManager, RecordAudioBottomSheet.TAG)
        recordSheetDialog.callbackListener = object : RecordAudioBottomSheet.CallbackListener {
            override fun saveAudio(fileName: String?) {
                if (fileName != null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.add_audio_scuccess),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    viewModel.soundFileName = fileName
                    binding.recordEnglishPronunciation.setImageResource(R.drawable.mic_successful)
                    binding.isRecordAdded.visibility = View.VISIBLE
                } else {
                    viewModel.soundFileName = fileName
                    binding.recordEnglishPronunciation.setImageResource(R.drawable.mic_active)
                    binding.isRecordAdded.visibility = View.INVISIBLE
                    viewModel.saveAudio(null)
                }
            }
        }
    }

    private fun setObservers() = with(viewModel) {
        translates.observe(viewLifecycleOwner) { translateAdapter.submitList(it) }
        hints.observe(viewLifecycleOwner) { hintAdapter.submitList(it) }
        savedWordResult.observe(viewLifecycleOwner) {
            val message = if (it == true) {
                findNavController().popBackStack()
                getString(R.string.modify_word_success_save_word)
            } else {
                getString(R.string.error_happened)
            }
            Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdaptersClickListener() {
        translateAdapter.clickListener =
            object : ModifyWordAdapter.OnItemClickListener<TranslateWordItem> {
                override fun onItemClick(it: View, item: TranslateWordItem) {
                    createTranslatePopupMenu(it, item)
                }
            }

        hintAdapter.clickListener =
            object : ModifyWordAdapter.OnItemMultiClickListener<HintItem> {
                override fun onItemClick(it: View, item: HintItem) {
                    createHintPopupMenu(it, item)
                }

                override fun onItemDeleteClick(item: HintItem) {
                    viewModel.deleteHint(item.id)
                }
            }
    }

    private fun setupClickListener() = with(binding) {
        addTranslate.button.setOnClickListener {
            viewModel.addTranslate(addTranslate.translateInput.text.toString())
            addTranslate.translateInput.setText("")
        }

        addTranslate.cancelEditTranslate.setOnClickListener {
            viewModel.setEditableTranslate(null)
            addTranslate.translateInput.setText("")
        }

        addHints.button.setOnClickListener {
            viewModel.addHint(addHints.inputHint.text.toString())
            addHints.inputHint.setText("")
        }

        addHints.cancelEditHint.setOnClickListener {
            viewModel.setEditableHint(null)
            addHints.inputHint.setText("")
        }

        toggleAdditionalField.setOnClickListener { viewModel.toggleVisibleAdditionalField() }
        saveTranslatedWord.setOnClickListener {
            viewModel.saveWord(
                value = inputTranslatedWord.englishWordInput.text.toString(),
                description = translateWordDescription.descriptionInput.text.toString(),
                transcription = inputTranslatedWord.englishTranscriptionInput.text.toString(),
                langFrom = inputTranslatedWord.selectLanguageSpinner.selectedItem.toString(),
                langTo = "UA"
            )
        }
    }

    private fun createHintPopupMenu(v: View, hintChip: HintItem) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.menuInflater.inflate(R.menu.hint_item_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    with(binding.addHints.inputHint) {
                        setText(hintChip.value)
                        requestFocus()
                        setSelection(length())
                    }
                    viewModel.setEditableHint(hintChip)
                }
            }
            false
        }
        popupMenu.show()
    }

    private fun createTranslatePopupMenu(v: View, translateChip: TranslateWordItem) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.menuInflater.inflate(R.menu.trasnlate_item_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    with(binding.addTranslate.translateInput) {
                        setText(translateChip.value)
                        requestFocus()
                        setSelection(length())
                    }
                    viewModel.setEditableTranslate(translateChip)
                }

                R.id.menu_delete -> {
                    viewModel.deleteTranslate(translateChip.id)
                }
            }
            false
        }

        popupMenu.show()
    }

    private fun editTextScrollListener() {
        ScrollEditTextInsideScrollView.allowScroll(binding.translateWordDescription.descriptionInput)
    }

}