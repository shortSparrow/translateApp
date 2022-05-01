package com.example.ttanslateapp.presentation.word_list

import android.os.Bundle
import android.view.View
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentWordListBinding
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.modify_word.ModifyWordFragment
import com.example.ttanslateapp.presentation.word_list.adapter.WordListAdapter
import com.example.ttanslateapp.util.getAppComponent

class WordListFragment : BaseFragment<FragmentWordListBinding>() {

    override val bindingInflater: BindingInflater<FragmentWordListBinding>
        get() = FragmentWordListBinding::inflate

    private val viewModel by viewModels {
        get(WordListViewModel::class.java)
    }

    private val wordListAdapter = WordListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        setAdapter()
        makeSearchBarClickable()
        viewModel.wordList.observe(viewLifecycleOwner) { wordListAdapter.submitList(it) }
    }

    private fun makeSearchBarClickable() = with(binding) {
        searchWord.setOnClickListener { searchWord.isIconified = false }
        addNewWord.setOnClickListener { launchAddWordScreen() }
    }

    private fun launchAddWordScreen() {
        val fragment = ModifyWordFragment.newInstanceAdd()
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setAdapter() {
        wordListAdapter
            .apply { binding.wordListRv.adapter = this }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WordListFragment()
    }
}