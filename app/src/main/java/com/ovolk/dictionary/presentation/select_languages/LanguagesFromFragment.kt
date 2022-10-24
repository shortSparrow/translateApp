package com.ovolk.dictionary.presentation.select_languages

import android.os.Bundle
import android.view.View
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.R
import com.ovolk.dictionary.databinding.FragmentLanguageFromBinding
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.select_languages.components.SelectLanguagesToFrom
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LanguagesFromFragment : BaseFragment<FragmentLanguageFromBinding>() {

    override val bindingInflater: BindingInflater<FragmentLanguageFromBinding>
        get() = FragmentLanguageFromBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<LanguagesToFromViewModel>()
            viewModel.setCurrentType(LanguagesType.LANG_FROM)

            val state = viewModel.state
            if (viewModel.listener == null) {
                viewModel.listener = listener()
            }
            AppCompatTheme {
                SelectLanguagesToFrom(
                    title = stringResource(id = R.string.select_languages_translate_language_from),
                    state = state,
                    onAction = viewModel::onAction
                )
            }
        }
    }

    fun listener() = object : LanguagesToFromViewModel.Listener {
        override fun navigate(lang: LanguagesType?) {
            when (lang) {
                LanguagesType.LANG_TO -> {
                    findNavController().navigate(LanguagesToFragmentDirections.actionLanguagesToFragmentToWordListFragment())
                }
                LanguagesType.LANG_FROM -> {
                    findNavController().navigate(LanguagesFromFragmentDirections.actionLanguagesFromFragmentToLanguagesToFragment())
                }
                null -> {}
            }
        }
    }
}