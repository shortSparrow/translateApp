package com.ovolk.dictionary.presentation.list_full

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ovolk.dictionary.databinding.FragmentListFullBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.google.accompanist.appcompattheme.AppCompatTheme
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
            if (!viewModel.initialMount) {
                viewModel.setNavController(findNavController())
                viewModel.onAction(ListFullAction.InitialLoadData(args.listId, args.listName))
                viewModel.initialMount = true
            }

            val state = viewModel.state

            AppCompatTheme {
                ListFullScreen(state = state, onAction = viewModel::onAction)
            }
        }
    }

}