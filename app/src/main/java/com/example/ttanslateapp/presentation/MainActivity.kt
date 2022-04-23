package com.example.ttanslateapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.modify_word.ModifyWordFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchModifyFragment()
    }

    private fun launchModifyFragment() {
        val fragment = ModifyWordFragment.newInstanceAdd()

        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_modify_word_container, fragment)
            .addToBackStack(null)
            .commit()
    }


}