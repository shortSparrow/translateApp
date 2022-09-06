package com.example.ttanslateapp.presentation.list_full

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.navArgs
import com.example.ttanslateapp.databinding.FragmentListBinding
import com.example.ttanslateapp.databinding.FragmentListFullBinding
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.lists.ListsScreen
import com.example.ttanslateapp.presentation.lists.ListsViewModel
import com.example.ttanslateapp.presentation.modify_word.ModifyWordFragmentArgs
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
            val state = viewModel.state
            viewModel.onAction(ListFullAction.InitialLoadData(args.listId))

            AppCompatTheme {
                ListFullScreen(state = state, onAction = viewModel::onAction)
            }
        }
    }

}