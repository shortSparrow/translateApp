package com.ovolk.dictionary.data.in_memory_storage

import kotlinx.coroutines.flow.MutableStateFlow

interface InMemoryStorage {
    val data: MutableStateFlow<String>

    fun setData(value: String)
}