package com.example.ttanslateapp.presentation.list_full

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttanslateapp.databinding.FragmentListBinding
import com.example.ttanslateapp.databinding.FragmentListFullBinding
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.lists.ListsScreen
import com.example.ttanslateapp.presentation.lists.ListsViewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFullFragment :
    BaseFragment<FragmentListFullBinding>() {

    override val bindingInflater: BindingInflater<FragmentListFullBinding>
        get() = FragmentListFullBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listFull.setContent {
            val viewModel = hiltViewModel<ListsViewModel>()
            val state = viewModel.state

            AppCompatTheme {
//                ListsScreen(
//                    state = state,
//                    onAction = viewModel::onAction
//                )
            }
        }
    }

}