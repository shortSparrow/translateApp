package com.example.ttanslateapp.presentation.modify_word

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentModifyWordBinding
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.modify_word.adapter.ModifyWordAdapter
import com.example.ttanslateapp.presentation.modify_word.adapter.hints.HintAdapter
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import com.example.ttanslateapp.util.ScrollEditTextInsideScrollView
import com.example.ttanslateapp.util.getAppComponent
import com.example.ttanslateapp.util.lazySimple
import com.example.ttanslateapp.util.setOnTextChange


private const val MODE_ADD = "mode-add"
private const val MODE_EDIT = "mode-edit"
private const val MODE = "mode"
private const val WORD_ID = "word_id"

class ModifyWordFragment : BaseFragment<FragmentModifyWordBinding>() {

    override val bindingInflater: BindingInflater<FragmentModifyWordBinding>
        get() = FragmentModifyWordBinding::inflate

    private val viewModel by viewModels { get(ModifyWordViewModel::class.java) }

    private var mode: String? = null
    private var wordId: Long? = null
    private val translateAdapter = TranslateAdapter()
    private val hintAdapter = HintAdapter()

    private val modifyWordObservers by lazySimple {
        ModifyWordObservers(viewModel,binding)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE)
            wordId = it.getLong(WORD_ID)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        launchRightMode()
        setupClickListener()
        editTextScrollListener()
        setAdaptersClickListener()
        setupView()
        setObservers()

        viewLifecycleOwner.lifecycle.addObserver(modifyWordObservers)
    }

    private fun launchRightMode() {
        when (mode) {
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() {
        val id = wordId ?: error("wordId is null")
        viewModel.getWordById(id)
        viewModel.successLoadWordById = object : ModifyWordViewModel.SuccessLoadWordById {
            override fun onLoaded(word: ModifyWord) {
                with(binding) {
                    inputTranslatedWord.englishWordInput.setText(word.value);
                    translateWordDescription.descriptionInput.setText(word.description)

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
    }


    private fun setupView() = with(binding) {
        inputTranslatedWord.englishWordInput.setOnTextChange { viewModel.setWordValueError(false) }

        addTranslate.translateChipsRv.adapter = translateAdapter
        addTranslate.translateChipsRv.itemAnimator = null
        addTranslate.translateInput.setOnTextChange { viewModel.setTranslatesError(false) }

        addHints.hintChipsRv.adapter = hintAdapter
        addHints.hintChipsRv.itemAnimator = null
    }

    private fun setObservers() = with(viewModel) {
        translates.observe(viewLifecycleOwner) { translateAdapter.submitList(it) }
        hints.observe(viewLifecycleOwner) { hintAdapter.submitList(it) }
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
                    binding.addHints.inputHint.setText(hintChip.value)
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
                    binding.addTranslate.translateInput.setText(translateChip.value)
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

    companion object {
        @JvmStatic
        fun newInstanceAdd() =
            ModifyWordFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        fun newInstanceEdit(wordId: Long) =
            ModifyWordFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_EDIT)
                    putLong(WORD_ID, wordId)
                }
            }
    }

}