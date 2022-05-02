package com.example.ttanslateapp.presentation.word_list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.SearchView
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

        viewModel.loadWordList()
        setAdapter()
        makeSearchBarClickable()
        viewModel.wordList.observe(viewLifecycleOwner) { wordListAdapter.submitList(it) }

        wordListAdapter.onClickListener = object : WordListAdapter.OnClickListener {
            override fun onRootClickListener(wordId: Long) {
                launchEditWordScreen(wordId)
            }
        }

        binding.searchWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("CCC", query.toString())

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d("CCC", "SEARCH")
                viewModel.searchDebounced(query.toString())
                return true
            }

        })
    }

    private fun makeSearchBarClickable() = with(binding) {
        searchWord.setOnClickListener { searchWord.isIconified = false }
        addNewWord.setOnClickListener { launchAddWordScreen() }
    }

    private fun launchAddWordScreen() {
        val fragment = ModifyWordFragment.newInstanceAdd()
        launchModifyScreen(fragment)
    }

    private fun launchEditWordScreen(wordId: Long) {
        val fragment = ModifyWordFragment.newInstanceEdit(wordId)
        launchModifyScreen(fragment)
    }

    private fun launchModifyScreen(fragment: ModifyWordFragment) {
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