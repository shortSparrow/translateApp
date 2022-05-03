package com.example.ttanslateapp.presentation.layout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.AddTranslateBinding

class AddTranslate @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _binding: AddTranslateBinding? = null
    val binding get() = _binding!!

    init {
        _binding = AddTranslateBinding.inflate(LayoutInflater.from(context), this, true)
    }

 // TODO


}

