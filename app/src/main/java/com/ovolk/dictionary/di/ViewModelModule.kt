package com.ovolk.dictionary.di

import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.presentation.exam.ExamKnowledgeWordsViewModel
import com.ovolk.dictionary.presentation.modify_word.ModifyWordViewModel
import com.ovolk.dictionary.presentation.settings.SettingsViewModel
import com.ovolk.dictionary.presentation.word_list.WordListViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.multibindings.IntoMap

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(ModifyWordViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindModifyWordViewModel(impl: ModifyWordViewModel): ViewModel

    @IntoMap
    @ViewModelKey(WordListViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindWordListViewModel(impl: WordListViewModel): ViewModel


    @IntoMap
    @ViewModelKey(ExamKnowledgeWordsViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindExamKnowledgeWordsViewModel(impl: ExamKnowledgeWordsViewModel): ViewModel


    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindSettingsViewModel(impl: SettingsViewModel): ViewModel
}