package com.example.ttanslateapp.presentation.lists

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.ttanslateapp.databinding.FragmentListBinding
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment :
    BaseFragment<FragmentListBinding>() {

    override val bindingInflater: BindingInflater<FragmentListBinding>
        get() = FragmentListBinding::inflate

    private fun getNavController(): NavController {
        return findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lists.setContent {
            val viewModel = hiltViewModel<ListsViewModel>()
            val state = viewModel.state

            AppCompatTheme {
                ListsScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    getNavController = { getNavController() }
                )
            }
        }
    }

}