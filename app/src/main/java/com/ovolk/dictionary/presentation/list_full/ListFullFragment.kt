package com.ovolk.dictionary.presentation.list_full

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentListFullBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFullFragment :
    BaseFragment<FragmentListFullBinding>() {
    private val args by navArgs<ListFullFragmentArgs>()
    override val bindingInflater: BindingInflater<FragmentListFullBinding>
        get() = FragmentListFullBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listFull.setContent {
            val viewModel = hiltViewModel<ListsFullViewModel>()
            if (viewModel.listener == null) {
                viewModel.listener = listener()
                viewModel.onAction(ListFullAction.InitialLoadData(args.listId, args.listName))
            }

            val state = viewModel.state

            AppCompatTheme {
                ListFullScreen(state = state, onAction = viewModel::onAction)
            }
        }
    }

    fun listener() = object : ListsFullViewModel.Listener {
        override fun goBack() {
            findNavController().popBackStack()
        }

        override fun navigateToExam(listId: Long, listName: String) {
            findNavController().navigate(
                ListFullFragmentDirections.actionListFullFragmentToExamKnowledgeWordsFragment(
                    listId = listId,
                    listName = listName
                )
            )
        }

        override fun navigateToEditWord(wordId: Long) {
            findNavController().navigate(
                ListFullFragmentDirections.actionListFullFragmentToModifyWordFragment(
                    mode = ModifyWordModes.MODE_EDIT,
                    wordId = wordId
                )
            )
        }

        override fun navigateToAddWord(listId: Long) {
            findNavController().navigate(
                ListFullFragmentDirections.actionListFullFragmentToModifyWordFragment(
                    mode = ModifyWordModes.MODE_ADD,
                    listId = listId
                )
            )
        }
    }

}