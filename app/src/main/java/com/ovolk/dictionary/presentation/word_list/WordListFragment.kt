package com.ovolk.dictionary.presentation.word_list

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.LaunchedEffect
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

        binding.root.setContent {
            val viewModel = hiltViewModel<WordListViewModel>()
            val state = viewModel.state

            // prefilled search field and do search when open app from intent and pass searchWord
            LaunchedEffect(args) {
                args.searchedWord?.let {
                    viewModel.onAction(WordListAction.SearchWord(it))
                }
            }
            if (viewModel.listener == null) {
                viewModel.listener = listener()
            }

            AppCompatTheme {
                WordListScreen(state = state, onAction = viewModel::onAction)
            }
        }
    }


    private fun listener() = object : WordListViewModel.Listener {
        override fun navigateToCreateNewWord() {
            findNavController().navigate(
                WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(
                    ModifyWordModes.MODE_ADD
                )
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