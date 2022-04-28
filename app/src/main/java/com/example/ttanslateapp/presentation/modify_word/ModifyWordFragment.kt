package com.example.ttanslateapp.presentation.modify_word

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ttanslateapp.R
import com.example.ttanslateapp.TranslateApp
import com.example.ttanslateapp.databinding.FragmentModifyWordBinding
import com.example.ttanslateapp.presentation.ViewModelFactory
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

        setupClickListener()
        editTextScrollListener()
        liveDataListeners()
    }

    private fun liveDataListeners() {
        additionalFieldListener()
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
            save.setOnClickListener {
                val word = inputTranslatedWord.englishWordInput.text.toString()
                val description = translateWordDescription.descriptionInput.text.toString()

                model.saveWord(word = word, description = description)
            }

            getWords.setOnClickListener {
                val word = model.getWordById(100)
                Log.d("ModifyWordFragment", word.toString())
            }

            addHints.translateHintItem.hintItem.setOnClickListener {
                showMenu(it, R.menu.translate_hint_menu)
            }


            // FIXME Зробити через RV
            addTranslate.chipItemContainer.allViews.forEach {
                it.setOnClickListener {
                    showMenu(it, R.menu.translate_hint_menu)
                }
            }

            toggleAdditionalField.setOnClickListener {
                model.toggleVisibleAdditionalField()
            }

        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            true
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

//    @SuppressLint("ClickableViewAccessibility")
    private fun editTextScrollListener() {
//        // scroll edit text inside scrollview
//        ScrollEditTextInsideScrollView()
//            .allowScroll(binding.translateWordDescription.descriptionInput)


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