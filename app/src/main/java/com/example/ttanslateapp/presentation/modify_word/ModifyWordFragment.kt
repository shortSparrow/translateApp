package com.example.ttanslateapp.presentation.modify_word

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ttanslateapp.R
import com.example.ttanslateapp.TranslateApp
import com.example.ttanslateapp.databinding.FragmentModifyWordBinding
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import com.example.ttanslateapp.presentation.ViewModelFactory
import com.example.ttanslateapp.presentation.modify_word.adapter.hints.HintAdapter
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import com.example.ttanslateapp.setOnTextChange
import com.example.ttanslateapp.util.ScrollEditTextInsideScrollView
import javax.inject.Inject


private const val MODE_ADD = "mode-add"
private const val MODE_EDIT = "mode-edit"
private const val MODE = "mode"

class ModifyWordFragment : Fragment() {
    private var _binding: FragmentModifyWordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val model by lazy {
        ViewModelProvider(this, viewModelFactory)[ModifyWordViewModel::class.java]
    }

    private var mode: String? = null

    private val component by lazy {
        (requireActivity().application as TranslateApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModifyWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)

        // TODO є 2 різних адаптера, і підписки до них, вони майже однакові
        //  див (setAdapters, observeAdapter, setAdaptersClickListener, setupClickListener,createHintPopupMenu,createTranslatePopupMenu)
        setupClickListener()
        editTextScrollListener()
        liveDataListeners()
        setAdapters()

        binding.inputTranslatedWord.englishWordInput.setOnTextChange {
            model.setWordValueError(false)
        }

        binding.addTranslate.translateInput.setOnTextChange {
            model.setTranslatesError(false)
        }

//        binding.inputTranslatedWord.test()
    }

    private fun setAdapters() {
        val translateAdapter = TranslateAdapter()
        binding.addTranslate.translateChipsRv.adapter = translateAdapter
        binding.addTranslate.translateChipsRv.itemAnimator = null

        val hintAdapter = HintAdapter()
        binding.addHints.hintChipsRv.adapter = hintAdapter
        binding.addHints.hintChipsRv.itemAnimator = null

        observeAdapter(translateAdapter, hintAdapter)
        setAdaptersClickListener(translateAdapter, hintAdapter)
    }


    private fun observeAdapter(adapter: TranslateAdapter, hintAdapter: HintAdapter) {
        model.translates.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        model.hints.observe(viewLifecycleOwner) {
            hintAdapter.submitList(it)
        }

    }

    private fun setAdaptersClickListener(
        translateAdapter: TranslateAdapter,
        hintAdapter: HintAdapter
    ) {
        translateAdapter.onChipClickListener = object : TranslateAdapter.OnChipClickListener {
            override fun onChipClick(it: View, translateWordItem: TranslateWordItem) {
                createTranslatePopupMenu(it, translateWordItem)
            }
        }

        hintAdapter.onChipClickListener = object : HintAdapter.OnChipClickListener {
            override fun onChipClick(it: View, hintItem: HintItem) {
                createHintPopupMenu(it, hintItem)
            }

            override fun onDeleteClick(hintItem: HintItem) {
                model.deleteHint(hintItem.id)
            }

        }
    }

    private fun liveDataListeners() {
        additionalFieldListener()

        model.editableHint.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.addHints.button.text = "Edit"
                binding.addHints.cancelEditHint.visibility = View.VISIBLE
            } else {
                binding.addHints.button.text = "Add"
                binding.addHints.cancelEditHint.visibility = View.INVISIBLE
            }
        }

        model.editableTranslate.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.addTranslate.button.text = "Edit"
                binding.addTranslate.cancelEditTranslate.visibility = View.VISIBLE
            } else {
                binding.addTranslate.button.text = "Add"
                binding.addTranslate.cancelEditTranslate.visibility = View.INVISIBLE
            }
        }

        model.wordValueError.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputTranslatedWord.englishWordContainer.error = "This field is required"
            } else {
                binding.inputTranslatedWord.englishWordContainer.error = null
            }
        }
        model.translatesError.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.addTranslate.englishWordContainer.error = "This field is required"
            } else {
                binding.addTranslate.englishWordContainer.error = null
            }
        }

    }

    private fun additionalFieldListener() {
        model.isAdditionalFieldVisible.observe(viewLifecycleOwner) {
            if (it) {
                binding.addHints.root.visibility = View.VISIBLE
            } else {
                binding.addHints.root.visibility = View.GONE
            }
        }
    }

    private fun setupClickListener() {
        with(binding) {
            addTranslate.button.setOnClickListener {
                model.addTranslate(addTranslate.translateInput.text.toString())
                addTranslate.translateInput.setText("")
            }

            binding.addTranslate.cancelEditTranslate.setOnClickListener {
                model.setEditableTranslate(null)
                binding.addTranslate.translateInput.setText("")
            }

            addHints.button.setOnClickListener {
                model.addHint(addHints.inputHint.text.toString())
                addHints.inputHint.setText("")
            }

            binding.addHints.cancelEditHint.setOnClickListener {
                model.setEditableHint(null)
                binding.addHints.inputHint.setText("")
            }

            toggleAdditionalField.setOnClickListener {
                model.toggleVisibleAdditionalField()
            }

            saveTranslatedWord.setOnClickListener {
                with(binding) {
                    model.saveWord(
                        value = inputTranslatedWord.englishWordInput.text.toString(),
                        description = translateWordDescription.descriptionInput.text.toString(),
                        langFrom = inputTranslatedWord.selectLanguageSpinner.selectedItem.toString(),
                        langTo = "UA"
                    )
                }

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
                    model.setEditableHint(hintChip)
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
                    model.setEditableTranslate(translateChip)
                }

                R.id.menu_delete -> {
                    model.deleteTranslate(translateChip.id)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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