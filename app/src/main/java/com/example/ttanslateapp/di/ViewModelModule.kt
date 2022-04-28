package com.example.ttanslateapp.di

import androidx.lifecycle.ViewModel
import com.example.ttanslateapp.presentation.modify_word.ModifyWordViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(ModifyWordViewModel::class)
    @Binds
    fun bindModifyWordViewModel(impl: ModifyWordViewModel): ViewModel
}