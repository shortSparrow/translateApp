package com.example.ttanslateapp.presentation.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentListBinding
import com.example.ttanslateapp.databinding.FragmentWordListBinding
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment :
    BaseFragment<FragmentListBinding>() {

    override val bindingInflater: BindingInflater<FragmentListBinding>
        get() = FragmentListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.greeting.setContent {
            val viewModel = hiltViewModel<ListsViewModel>()
            val state = viewModel.state

            AppCompatTheme {
                Greeting(
                    state = state,
                    onAction = viewModel::onAction
                )
            }
        }
    }

}