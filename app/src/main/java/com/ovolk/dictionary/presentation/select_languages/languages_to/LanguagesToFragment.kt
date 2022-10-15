package com.ovolk.dictionary.presentation.select_languages.languages_to

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentLanguageToBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.select_languages.languages_to.components.SelectLanguagesTo
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LanguagesToFragment : BaseFragment<FragmentLanguageToBinding>() {

    override val bindingInflater: BindingInflater<FragmentLanguageToBinding>
        get() = FragmentLanguageToBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<LanguagesToViewModel>()
            val state = viewModel.state
            if (!viewModel.initialMount) {
                viewModel.setNavController(findNavController())
                viewModel.initialMount = true
            }

            AppCompatTheme {
                SelectLanguagesTo(state=state, onAction=viewModel::onAction)
            }
        }
    }


}