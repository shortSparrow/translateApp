package com.example.ttanslateapp.presentation.modify_word

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ttanslateapp.R
import com.example.ttanslateapp.TranslateApp
import com.example.ttanslateapp.databinding.FragmentModifyWordBinding
import com.example.ttanslateapp.presentation.ViewModelFactory
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
        descriptionScrollListener()
    }

    private fun setupClickListener() {
        with(binding) {
            saveTranslatedWord.setOnClickListener {
//                val word = englishWord.text.toString()
//                val description = description.text.toString()
//
//                model.addTranslatedWord(word = word, description = description)
            }

            getWords.setOnClickListener {
                model.getWordById(100)
            }

            addHints.translateHintItem.hintItem.setOnClickListener {
                showMenu(it, R.menu.translate_hint_menu)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun descriptionScrollListener() {
        // scroll edit text inside scrollview

        binding.translateWordDescription.descriptionInput.setOnTouchListener(OnTouchListener { v, event ->
            if (binding.translateWordDescription.descriptionInput.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })
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