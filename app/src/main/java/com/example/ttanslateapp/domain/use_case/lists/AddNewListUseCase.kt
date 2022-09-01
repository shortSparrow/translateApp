package com.example.ttanslateapp.domain.use_case.lists

import android.app.Application
import com.example.ttanslateapp.domain.model.lists.ListItem
import javax.inject.Inject

class AddNewListUseCase @Inject constructor(
    private val repository: ListsRepository,
) {
    suspend fun addNewList(title: String) {
        repository.addNewList(
            ListItem(
                id=0,
                title = title,
                count = 0,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}