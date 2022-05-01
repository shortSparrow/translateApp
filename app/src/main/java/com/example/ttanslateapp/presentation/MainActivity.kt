package com.example.ttanslateapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ttanslateapp.R
import com.example.ttanslateapp.presentation.modify_word.ModifyWordFragment
import com.example.ttanslateapp.presentation.word_list.WordListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            launchModifyFragment()
        }
    }

    private fun launchModifyFragment() {
//        val fragment = ModifyWordFragment.newInstanceAdd()
        val fragment = WordListFragment.newInstance()

        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_container, fragment)
            .commit()
    }


}