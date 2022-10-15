package com.ovolk.dictionary.presentation.select_languages.languages_from

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentLanguageFromBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.select_languages.languages_from.componenets.SelectLanguagesFrom
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LanguagesFromFragment : BaseFragment<FragmentLanguageFromBinding>() {

    override val bindingInflater: BindingInflater<FragmentLanguageFromBinding>
        get() = FragmentLanguageFromBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<LanguagesFromViewModel>()
            val state = viewModel.state
            if (!viewModel.initialMount) {
                viewModel.setNavController(findNavController())
                viewModel.initialMount = true
            }

            AppCompatTheme {
                SelectLanguagesFrom(state = state, onAction = viewModel::onAction)
            }
        }
    }

}