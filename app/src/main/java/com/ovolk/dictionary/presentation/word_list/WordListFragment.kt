package com.ovolk.dictionary.presentation.word_list

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentWordListBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WordListFragment : BaseFragment<FragmentWordListBinding>() {

    override val bindingInflater: BindingInflater<FragmentWordListBinding>
        get() = FragmentWordListBinding::inflate

    private val args by navArgs<WordListFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
////        But we want restore ui on rotate and go over from fragment
//        viewModel.restoreUI()

//        setAdapter()
//        clickListeners()
//        observeData()

//        binding.searchWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean = true
//
//            override fun onQueryTextChange(query: String?): Boolean {
//                if (viewModel.searchInputValue != query.toString()) {
//                    viewModel.searchInputValue = query.toString()
//                    Timber.d("onQueryTextChange")
//                    viewModel.searchDebounced(query.toString())
//                }
//                return true
//            }
//        })

        // prefilled search field and do search when open app from intent and pass searchWord
//        if (args.searchedWord != null) {
//            binding.searchWord.onActionViewExpanded()
//            binding.searchWord.setQuery(args.searchedWord, false)
//        }

        binding.root.setContent {
            val viewModel = hiltViewModel<WordListViewModel>()
            val state = viewModel.state

            if (viewModel.listener == null) {
                viewModel.listener =  listener()
            }

            AppCompatTheme {
                WordListScreen(state = state, onAction = viewModel::onAction)
            }
        }
    }


    private fun listener() = object : WordListViewModel.Listener {
        override fun navigateToCreateNewWord() {
            findNavController().navigate(
                WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(ModifyWordModes.MODE_ADD)
            )
        }

        override fun navigateToExistingWord(wordId: Long) {
            findNavController().navigate(
                WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(
                    mode = ModifyWordModes.MODE_EDIT,
                    wordId = wordId
                )
            )
        }
    }
}