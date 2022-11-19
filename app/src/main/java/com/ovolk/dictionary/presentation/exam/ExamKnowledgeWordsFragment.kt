package com.ovolk.dictionary.presentation.exam

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentExamKnowledgeWordsBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.modify_word.ModifyWordModes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamKnowledgeWordsFragment : BaseFragment<FragmentExamKnowledgeWordsBinding>() {

    override val bindingInflater: BindingInflater<FragmentExamKnowledgeWordsBinding>
        get() = FragmentExamKnowledgeWordsBinding::inflate

    private val args by navArgs<ExamKnowledgeWordsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<ExamKnowledgeWordsViewModel>()
            val state = viewModel.composeState
            val onAction = viewModel::onAction


            // TODO temporary solution for updating exam list after create first word
            if (state.examWordList.isEmpty() && state.shouldLoadWordListAgain) {
                onAction(ExamAction.LoadExamList(listId = args.listId, listName = args.listName))
            }

            if (viewModel.listener == null) {
                viewModel.listener = object : ExamKnowledgeWordsViewModel.Listener {
                    override fun onNavigateToCreateFirstWord() {
                        findNavController().navigate(
                            ExamKnowledgeWordsFragmentDirections.actionExamKnowledgeWordsFragmentToModifyWordFragment(
                                mode = ModifyWordModes.MODE_ADD
                            )
                        )
                    }
                }
            }

            AppCompatTheme {
                ExamPresenter(state = state, onAction = onAction, )
            }
        }
    }
}
