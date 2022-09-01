package com.example.ttanslateapp.di

import androidx.lifecycle.ViewModel
import com.example.ttanslateapp.presentation.exam.ExamKnowledgeWordsViewModel
import com.example.ttanslateapp.presentation.modify_word.ModifyWordViewModel
import com.example.ttanslateapp.presentation.settings.SettingsViewModel
import com.example.ttanslateapp.presentation.word_list.WordListViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(ModifyWordViewModel::class)
    @Binds
    fun bindModifyWordViewModel(impl: ModifyWordViewModel): ViewModel

    @IntoMap
    @ViewModelKey(WordListViewModel::class)
    @Binds
    fun bindWordListViewModel(impl: WordListViewModel): ViewModel


    @IntoMap
    @ViewModelKey(ExamKnowledgeWordsViewModel::class)
    @Binds
    fun bindExamKnowledgeWordsViewModel(impl: ExamKnowledgeWordsViewModel): ViewModel


    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    @Binds
    fun bindSettingsViewModel(impl: SettingsViewModel): ViewModel
}