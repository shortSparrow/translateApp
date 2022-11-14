package com.ovolk.dictionary.presentation.exam

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ovolk.dictionary.databinding.FragmentExamKnowledgeWordsBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamKnowledgeWordsFragment : BaseFragment<FragmentExamKnowledgeWordsBinding>() {

    override val bindingInflater: BindingInflater<FragmentExamKnowledgeWordsBinding>
        get() = FragmentExamKnowledgeWordsBinding::inflate

    private val args by navArgs<ExamKnowledgeWordsFragmentArgs>()
    private val viewModel by viewModels<ExamKnowledgeWordsViewModel>()
    private var bottomBar: BottomNavigationView? = null


    private val modeDialog by lazy {
        ExamModeDialog(
            context = requireContext(),
            viewModel = viewModel
        )
    }

    private val examEndDialog by lazy {
        ExamEndDialog(
            context = requireContext(),
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<ExamKnowledgeWordsViewModel>()
            val state = viewModel.composeState
            val onAction = viewModel::onAction

//            // generateWordsList on every enter on screen. On rotation not invoked. Because we change configChanges in Manifest
//            viewModel.generateWordsList(listId = if (args.listId == -1L) null else args.listId, listName = args.listName)
//            bottomBar = requireActivity().findViewById(R.id.bottom_app_bar)
            AppCompatTheme {
                ExamScreen(state = state, onAction = onAction)
            }
        }
    }

}
