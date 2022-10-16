package com.ovolk.dictionary.presentation.settings_reminder_exam

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.ovolk.dictionary.databinding.FragmentExamReminderBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import com.ovolk.dictionary.presentation.settings_reminder_exam.components.ExamReminderScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamReminderFragment : BaseFragment<FragmentExamReminderBinding>() {

    override val bindingInflater: BindingInflater<FragmentExamReminderBinding>
        get() = FragmentExamReminderBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentRoot.setContent {
            val viewModel = hiltViewModel<ExamReminderViewModel>()
            val state = viewModel.state

            AppCompatTheme {
                ExamReminderScreen(state, onAction=viewModel::onAction)
            }
        }
    }

}