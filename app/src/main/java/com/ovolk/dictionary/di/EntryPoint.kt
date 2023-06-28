package com.ovolk.dictionary.di

import com.ovolk.dictionary.data.in_memory_storage.ExamLocalCache
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MyEntryPoint {
    fun getExamLocalCache(): ExamLocalCache
}