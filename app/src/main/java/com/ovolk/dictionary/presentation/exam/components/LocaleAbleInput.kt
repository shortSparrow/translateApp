package com.ovolk.dictionary.presentation.exam.components

import android.text.InputType
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.exam.ExamAction
import com.ovolk.dictionary.presentation.exam.NavigateButtons

@Composable
fun LocaleAbleInput(
    answerValue: String,
    onAction: (ExamAction) -> Unit,
    currentWordFreeze: Boolean = false,
    isAutoSuggestEnable: Boolean = true,
) {
    val focusManager = LocalFocusManager.current

    fun setUpEditText(editTextComponent: TextInputEditText) {
        editTextComponent.addTextChangedListener {
            onAction(ExamAction.OnInputTranslate(it.toString()))
        }

        editTextComponent.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (!currentWordFreeze) {
                    onAction(ExamAction.OnCheckAnswer)
                }
                onAction(ExamAction.OnPressNavigate(NavigateButtons.NEXT))
                focusManager.moveFocus(FocusDirection.Down)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        editTextComponent.setText("")
        onAction(ExamAction.SetEditText(editTextComponent))
    }


    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.exam_input, null, false)

            val editText = view.findViewById<TextInputEditText>(R.id.editText)
            if (!isAutoSuggestEnable) {
                editText.inputType =
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            setUpEditText(editText)


            view // return the view
        },
        update = { view ->
            val editTextLocal = view.findViewById<TextInputEditText>(R.id.editText)
            if (answerValue != editTextLocal?.text.toString()) {
                editTextLocal?.setText(answerValue)
                editTextLocal?.setSelection(answerValue.length)
            }
        }
    )
}