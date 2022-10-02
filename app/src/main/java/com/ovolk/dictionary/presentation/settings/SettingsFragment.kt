package com.ovolk.dictionary.presentation.settings

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.databinding.FragmentSettingsBinding
import com.ovolk.dictionary.presentation.core.BaseFragment
import com.ovolk.dictionary.presentation.core.BindingInflater
import dagger.hilt.android.AndroidEntryPoint

val reminderFrequencyList = listOf("disable", "once a day", "once every 3 days", "once every 6 days")

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: BindingInflater<FragmentSettingsBinding>
        get() = FragmentSettingsBinding::inflate

    private val viewModel by viewModels<SettingsViewModel>()

    private lateinit var timePickerDialog: TimePickerDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setupData(savedInstanceState == null)
        setupView()
        observeData()
        onClickListeners()
    }

    private fun observeData() = with(binding) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is SettingsUiState.SetInitial -> {
                    setAdapter(uiState.frequency)
                    timePicker.text = "${uiState.timeHours}:${uiState.timeMinutes}"
                    save.isEnabled = !uiState.isSettingsTheSame
                }
                is SettingsUiState.SetReminderFrequency -> {
                    setAdapter(uiState.frequency)
                }
                is SettingsUiState.UpdateTime -> {
                    timePicker.text = "${uiState.timeHours}:${uiState.timeMinutes}"
                }
                is SettingsUiState.IsSuccessUpdateSettings -> {
                    val message =
                        if (uiState.isSuccess) getString(R.string.settings_update_word_success) else getString(
                            R.string.settings_update_word_failed
                        )
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is SettingsUiState.SettingsHasBeenChanged -> {
                    save.isEnabled = !uiState.isSame
                }
                is SettingsUiState.TimeBeforePush -> {
                    leftTimeBeforeNextPushLabel.visibility = View.VISIBLE
                    leftTimeBeforeNextPush.text = uiState.time
                }
            }
        }
    }


    private fun onClickListeners() = with(binding) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_menu, reminderFrequencyList)
        val menu = (binding.menuText as? AutoCompleteTextView)
        menu?.setAdapter(adapter)
        menu?.setOnItemClickListener { parent, view, position, id ->
            viewModel.updateUiFrequency(position)
        }

        timePicker.setOnClickListener { timePickerDialog.show() }
        save.setOnClickListener {
            viewModel.updateSettingsPreferences()
        }
    }

    private fun setupView() = with(binding) {
        setAdapter()
        timePickerDialog = TimePickerDialog(requireActivity(), 0, { view, hourOfDay, minute ->
            viewModel.updateTime(hours = hourOfDay, minutes = minute)
        }, viewModel.state.timeHours.toInt(), viewModel.state.timeMinutes.toInt(), true)
    }

    private fun setAdapter(frequency: String? = null) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.item_menu, reminderFrequencyList)
        val menu = (binding.menuText as? AutoCompleteTextView)
        menu?.setAdapter(adapter)
        if (frequency == null) return
        menu?.setText(frequency, false)
    }
}