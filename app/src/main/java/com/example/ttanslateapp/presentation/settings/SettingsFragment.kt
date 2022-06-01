package com.example.ttanslateapp.presentation.settings

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentSettingsBinding
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.util.getAppComponent
import timber.log.Timber

val reminderFrequencyList = listOf("вимкнути", "раз на день", "раз на 3 дні", "раз на 6 днів")


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: BindingInflater<FragmentSettingsBinding>
        get() = FragmentSettingsBinding::inflate

    private val viewModel by viewModels {
        get(SettingsViewModel::class.java)
    }

    lateinit var timePickerDialog: TimePickerDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        setupView()
        observeData()
        onClickListeners()
    }

    private fun observeData() = with(binding) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is SettingsUiState.SetInitial -> {
                    Timber.d("SSS ${uiState.frequency}")
                    setAdapter(uiState.frequency)
                    timePicker.text = "${uiState.timeHours}:${uiState.timeMinutes}"
                }
                is SettingsUiState.SetReminderFrequency -> {
                    setAdapter(uiState.frequency)
                }
                is SettingsUiState.UpdateTime -> {
                    timePicker.text = "${uiState.timeHours}:${uiState.timeMinutes}"
                }
            }
        }
    }


    private fun onClickListeners() = with(binding) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_menu, reminderFrequencyList)
        val menu = (binding.menuText as? AutoCompleteTextView)
        menu?.setAdapter(adapter)

        menu?.setOnItemClickListener { parent, view, position, id ->
            val item = reminderFrequencyList[position]
            Timber.d("item ${item}")
            Timber.d("position: ${position}")
            Timber.d("id: ${id}")
            viewModel.updateUiFrequency(position)
        }

        timePicker.setOnClickListener {
            timePickerDialog.show()
        }

        timeBeforeNextPush.setOnClickListener {
            val message = viewModel.getTimeBeforeNextPush()
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_LONG
            ).show()
        }

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