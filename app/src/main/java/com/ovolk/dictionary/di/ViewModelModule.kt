package com.ovolk.dictionary.di

import androidx.lifecycle.ViewModel
import com.ovolk.dictionary.presentation.create_first_dictionary.CreateFirstDictionaryViewModel
import com.ovolk.dictionary.presentation.dictionary_words.DictionaryWordsViewModel
import com.ovolk.dictionary.presentation.exam.ExamKnowledgeWordsViewModel
import com.ovolk.dictionary.presentation.modify_word.ModifyWordViewModel
import com.ovolk.dictionary.presentation.settings.SettingsViewModel
import com.ovolk.dictionary.presentation.settings_exam_daily.SettingsExamDailyViewModel
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
    @ViewModelKey(SettingsExamDailyViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindSettingsExamDailyViewModel(impl: SettingsExamDailyViewModel): ViewModel

    @IntoMap
    @ViewModelKey(CreateFirstDictionaryViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindCreateFirstDictionaryViewModel(impl: CreateFirstDictionaryViewModel): ViewModel


    @IntoMap
    @ViewModelKey(DictionaryWordsViewModel::class)
    @Binds
    @ViewModelScoped
    fun bindDictionaryWordsViewModel(impl: DictionaryWordsViewModel): ViewModel

}