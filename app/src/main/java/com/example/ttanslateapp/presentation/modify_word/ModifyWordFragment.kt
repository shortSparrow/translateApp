package com.example.ttanslateapp.presentation.modify_word

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentModifyWordBinding
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.modify_word.adapter.ModifyWordAdapter
import com.example.ttanslateapp.presentation.modify_word.adapter.hints.HintAdapter
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import com.example.ttanslateapp.setOnTextChange
import com.example.ttanslateapp.util.ScrollEditTextInsideScrollView
import com.example.ttanslateapp.util.getAppComponent

private const val MODE_ADD = "mode-add"
private const val MODE_EDIT = "mode-edit"
private const val MODE = "mode"

class ModifyWordFragment : BaseFragment<FragmentModifyWordBinding>() {

    override val bindingInflater: BindingInflater<FragmentModifyWordBinding>
        get() = FragmentModifyWordBinding::inflate

    private val viewModel by viewModels { get(ModifyWordViewModel::class.java) }

    private var mode: String? = null
    private val translateAdapter = TranslateAdapter()
    private val hintAdapter = HintAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        setupClickListener()
        editTextScrollListener()
        prepareAdapters()
        setupView()
        setObservers()
//        binding.inputTranslatedWord.test()
    }

    private fun setupView() = with(binding) {
        addTranslate.translateChipsRv.adapter = translateAdapter
        addTranslate.translateChipsRv.itemAnimator = null
        addTranslate.translateInput.setOnTextChange { viewModel.setTranslatesError(false) }

        addHints.hintChipsRv.adapter = hintAdapter
        addHints.hintChipsRv.itemAnimator = null

        inputTranslatedWord.englishWordInput.setOnTextChange { viewModel.setWordValueError(false) }
    }

    private fun setObservers() = with(viewModel) {
        translates.observe(viewLifecycleOwner) { translateAdapter.submitList(it) }
        hints.observe(viewLifecycleOwner) { hintAdapter.submitList(it) }

        editableHint.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.addHints.button.text = "Edit"
                binding.addHints.cancelEditHint.visibility = View.VISIBLE
            } else {
                binding.addHints.button.text = "Add"
                binding.addHints.cancelEditHint.visibility = View.INVISIBLE
            }
        }
        editableTranslate.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.addTranslate.button.text = "Edit"
                binding.addTranslate.cancelEditTranslate.visibility = View.VISIBLE
            } else {
                binding.addTranslate.button.text = "Add"
                binding.addTranslate.cancelEditTranslate.visibility = View.INVISIBLE
            }
        }

        /* additional fields */
        isAdditionalFieldVisible.observe(viewLifecycleOwner) {
            if (it) {
                binding.addHints.root.visibility = View.VISIBLE
            } else {
                binding.addHints.root.visibility = View.GONE
            }
        }

        wordValueError.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputTranslatedWord.englishWordContainer.error = "This field is required"
            } else {
                binding.inputTranslatedWord.englishWordContainer.error = null
            }
        }
        translatesError.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.addTranslate.englishWordContainer.error = "This field is required"
            } else {
                binding.addTranslate.englishWordContainer.error = null
            }
        }
    }

    private fun prepareAdapters() {
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
            binding.addTranslate.translateInput.setText("")
        }

        addHints.button.setOnClickListener {
            viewModel.addHint(addHints.inputHint.text.toString())
            addHints.inputHint.setText("")
        }

        binding.addHints.cancelEditHint.setOnClickListener {
            viewModel.setEditableHint(null)
            binding.addHints.inputHint.setText("")
        }

        toggleAdditionalField.setOnClickListener { viewModel.toggleVisibleAdditionalField() }
        saveTranslatedWord.setOnClickListener {
            with(binding) {
                viewModel.saveWord(
                    value = inputTranslatedWord.englishWordInput.text.toString(),
                    description = translateWordDescription.descriptionInput.text.toString(),
                    langFrom = inputTranslatedWord.selectLanguageSpinner.selectedItem.toString(),
                    langTo = "UA"
                )
            }

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


    //    @SuppressLint("ClickableViewAccessibility")
    private fun editTextScrollListener() {
//        // scroll edit text inside scrollview
        ScrollEditTextInsideScrollView.allowScroll(binding.translateWordDescription.descriptionInput)


//        binding.rootScrollView.setOnTouchListener { v, event ->
//            binding.translateWordDescription.descriptionInput.parent.requestDisallowInterceptTouchEvent(false)
//            false
//        }
//
//        binding.translateWordDescription.descriptionInput.setOnTouchListener { v, event ->
//            binding.translateWordDescription.descriptionInput.parent
//                .requestDisallowInterceptTouchEvent(true)
//            false
//        }
//        binding.translateWordDescription.descriptionInput.movementMethod = ScrollingMovementMethod()


    }

    companion object {
        private const val MENU_HINT = "menu-hint"
        private const val MENU_TRANSLATE = "menu-translate"

        @JvmStatic
        fun newInstanceAdd() =
            ModifyWordFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        fun newInstanceEdit() =
            ModifyWordFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_EDIT)
                }
            }
    }

}