package com.ovolk.dictionary.di

import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.presentation.exam.ExamKnowledgeWordsViewModel
import com.ovolk.dictionary.presentation.modify_word.ModifyWordViewModel
import com.ovolk.dictionary.presentation.select_languages.LanguagesToFromViewModel
import com.ovolk.dictionary.presentation.settings.SettingsViewModel
import com.ovolk.dictionary.presentation.settings_languages_to_from.SettingsLanguagesToFromViewModel
import com.ovolk.dictionary.presentation.settings_reminder_exam.ExamReminderViewModel
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

    @IntoMap
    @ViewModelKey(ExamReminderViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindExamReminderViewModel(impl: ExamReminderViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SettingsLanguagesToFromViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindSettingsLanguagesToViewModel(impl: SettingsLanguagesToFromViewModel): ViewModel


    @IntoMap
    @ViewModelKey(LanguagesToFromViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindLanguagesToFromViewModel(impl: LanguagesToFromViewModel): ViewModel

}