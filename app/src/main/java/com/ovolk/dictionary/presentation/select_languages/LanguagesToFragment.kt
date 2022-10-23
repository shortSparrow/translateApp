package com.ovolk.dictionary.presentation.select_languages

import android.os.Bundle
import android.view.View
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.R
import com.ovolk.dictionary.databinding.FragmentLanguageToBinding
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.select_languages.components.SelectLanguagesToFrom
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LanguagesToFragment : BaseFragment<FragmentLanguageToBinding>() {

    override val bindingInflater: BindingInflater<FragmentLanguageToBinding>
        get() = FragmentLanguageToBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<LanguagesToFromViewModel>()
            viewModel.setCurrentType(LanguagesType.LANG_TO)

            val state = viewModel.state
            if (!viewModel.initialMount) {
                viewModel.setNavController(findNavController())
                viewModel.initialMount = true
            }

            AppCompatTheme {
                SelectLanguagesToFrom(
                    title = stringResource(id = R.string.select_languages_translate_language_to),
                    state = state,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}