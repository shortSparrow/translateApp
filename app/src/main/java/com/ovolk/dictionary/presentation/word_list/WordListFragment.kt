package com.ovolk.dictionary.presentation.word_list

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ovolk.dictionary.databinding.FragmentWordListBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import com.ovolk.dictionary.presentation.word_list.adapter.WordListAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class WordListFragment : BaseFragment<FragmentWordListBinding>() {

    override val bindingInflater: BindingInflater<FragmentWordListBinding>
        get() = FragmentWordListBinding::inflate

    private val viewModel: WordListViewModel by viewModels()


    @Inject
    lateinit var  wordListAdapter: WordListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        But we want restore ui on rotate and go over from fragment
        viewModel.restoreUI()

        setAdapter()
        clickListeners()
        observeData()

        binding.searchWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(query: String?): Boolean {
                if (viewModel.searchInputValue != query.toString()) {
                    viewModel.searchInputValue = query.toString()
                    Timber.d("onQueryTextChange")
                    viewModel.searchDebounced(query.toString())
                }
                return true
            }
        })
    }

    private fun observeData() = with(binding) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is WordListViewModelState.IsLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is WordListViewModelState.LoadSuccess -> {
                    progressBar.visibility = View.GONE
                    if (uiState.wordList.isEmpty()) {
                        nothingFoundContainer.root.visibility = View.VISIBLE
                        wordListRv.visibility = View.GONE
                    } else {
                        nothingFoundContainer.root.visibility = View.GONE
                        wordListRv.visibility = View.VISIBLE
                    }

                    if (uiState.dictionaryIsEmpty) {
                        emptyListContainer.visibility = View.VISIBLE
                        wordListContainer.visibility = View.GONE
                    } else {
                        emptyListContainer.visibility = View.GONE
                        wordListContainer.visibility = View.VISIBLE
                    }

                    wordListAdapter.submitList(uiState.wordList) {
                        if (uiState.shouldScrollToStart) {
                            binding.wordListRv.scrollToPosition(0)
                        }
                    }
                    viewModel.previousSize = uiState.wordList.size
                }
            }
        }
    }

    private fun clickListeners() = with(binding) {
        searchWord.setOnClickListener { searchWord.isIconified = false }
        addNewWord.setOnClickListener { launchAddWordScreen() }
        emptyListLayout.addFirstWord.setOnClickListener { launchAddWordScreen() }

        wordListAdapter.onClickListener = object : WordListAdapter.OnClickListener {
            override fun onRootClickListener(wordId: Long) {
                launchEditWordScreen(wordId)
            }
        }
    }

    private fun launchAddWordScreen() {
        findNavController().navigate(
            WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(ModifyWordModes.MODE_ADD)
        )
    }

    private fun launchEditWordScreen(wordId: Long) {
        findNavController().navigate(
            WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(
                mode = ModifyWordModes.MODE_EDIT,
                wordId = wordId
            )
        )
    }

    private fun setAdapter() {
        binding.wordListRv.itemAnimator = null

        wordListAdapter
            .apply { binding.wordListRv.adapter = this }

    }
}