package com.example.ttanslateapp.presentation.word_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ttanslateapp.R
import com.example.ttanslateapp.TranslateApp
import com.example.ttanslateapp.databinding.FragmentWordListBinding
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