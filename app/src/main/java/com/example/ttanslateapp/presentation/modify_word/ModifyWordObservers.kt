package com.example.ttanslateapp.presentation.modify_word

import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.ttanslateapp.databinding.FragmentModifyWordBinding
import timber.log.Timber

class ModifyWordObservers(
    val viewModel: ModifyWordViewModel,
    val binding: FragmentModifyWordBinding,
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) = with(viewModel) {
        editableHint.observe(owner) {
            if (it != null) {
                binding.addHints.button.text = "Edit"
                binding.addHints.cancelEditHint.visibility = android.view.View.VISIBLE
            } else {
                binding.addHints.button.text = "Add"
                binding.addHints.cancelEditHint.visibility = android.view.View.INVISIBLE
            }
        }
        editableTranslate.observe(owner) {
            if (it != null) {
                binding.addTranslate.button.text = "Edit"
                binding.addTranslate.cancelEditTranslate.visibility = android.view.View.VISIBLE
            } else {
                binding.addTranslate.button.text = "Add"
                binding.addTranslate.cancelEditTranslate.visibility =
                    android.view.View.INVISIBLE
            }
        }

        /* additional fields */
        isAdditionalFieldVisible.observe(owner) {
            if (it) {
                binding.addHints.root.visibility = android.view.View.VISIBLE
            } else {
                binding.addHints.root.visibility = android.view.View.GONE
            }
        }

        wordValueError.observe(owner) {
            if (it == true) {
                binding.inputTranslatedWord.englishWordContainer.error =
                    "This field is required"
            } else {
                binding.inputTranslatedWord.englishWordContainer.error = null
            }
        }

        translatesError.observe(owner) {
            if (it == true) {
                binding.addTranslate.englishWordContainer.error = "This field is required"
            } else {
                binding.addTranslate.englishWordContainer.error = null
            }
        }

        savedWordResult.observe(owner) {
            val message = if (it == true) {
                "Success"
            } else {
                "fail"
            }
            Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
        }
    }
}