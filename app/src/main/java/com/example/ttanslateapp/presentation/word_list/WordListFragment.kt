package com.example.ttanslateapp.presentation.word_list

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ttanslateapp.R
import com.example.ttanslateapp.TranslateApp
import com.example.ttanslateapp.databinding.FragmentWordListBinding
import com.example.ttanslateapp.domain.model.ModifyWord
import com.example.ttanslateapp.presentation.ViewModelFactory
import com.example.ttanslateapp.presentation.modify_word.ModifyWordFragment
import com.example.ttanslateapp.presentation.word_list.adapter.WordListAdapter
import javax.inject.Inject


class WordListFragment : Fragment() {
    private var _binding: FragmentWordListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val model by lazy {
        ViewModelProvider(this, viewModelFactory)[WordListViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as TranslateApp).component
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        setAdapter()

        makeSearchBarClickable()

//        model.isShowMoreOpen.observe(viewLifecycleOwner) {
//            if (it) {
//                // SHOW
//            } else {
//                // HIDE
//            }
//        }

    }

    private fun makeSearchBarClickable() {
        binding.searchWord.setOnClickListener {
            binding.searchWord.isIconified = false
        }

        binding.addNewWord.setOnClickListener {
            launchAddWordScreen()
        }
    }

    fun launchAddWordScreen() {
        val fragment = ModifyWordFragment.newInstanceAdd()
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun setAdapter() {
        val wordListAdapter = WordListAdapter(requireContext()) // FIXME delete requireContext
        binding.wordListRv.adapter = wordListAdapter

        wordListAdapter.onClickListener = object : WordListAdapter.OnClickListener {
            override fun onClickShowMore(view: CardView) {
                if (model.isShowMoreOpen.value == false) {
                    val params: ViewGroup.LayoutParams = view.layoutParams
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    view.layoutParams = params
                    view.findViewById<TextView>(R.id.show_more).text = "Приховати"
                } else {
                    val params: ViewGroup.LayoutParams = view.layoutParams
                    val height = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        140F, resources.displayMetrics
                    );
                    params.height = height.toInt()
                    view.layoutParams = params
                    view.findViewById<TextView>(R.id.show_more).text = "Розкрити"
                }
                model.toggleIsShowMoreOpen()
            }
        }

        model.wordList.observe(viewLifecycleOwner) {
            wordListAdapter.submitList(it)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            WordListFragment()
    }
}