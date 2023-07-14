package com.ovolk.dictionary.data.mapper

import com.ovolk.dictionary.data.model.FullListItem
import com.ovolk.dictionary.data.model.ListItemDb
import com.ovolk.dictionary.domain.model.lists.ListItem
import com.ovolk.dictionary.domain.model.lists.ModifyWordListItem
import javax.inject.Inject

class ListMapper @Inject constructor() {
    fun fullListToLocal(listDb: List<FullListItem>): List<ListItem> =
        listDb.map { fullListItemToLocal(it) }

    fun fullListToModifyWordListItem(fullListItem: List<FullListItem>): List<ModifyWordListItem> =
        fullListItem.map { fullListItemToModifyWordListItem(it) }

    fun listItemToModifyWordListItem(listItem: ListItem): ModifyWordListItem = ModifyWordListItem(
        id = listItem.id,
        title = listItem.title,
        count = listItem.count,
        isSelected = listItem.isSelected,
        dictionaryId = listItem.dictionaryId
    )


    fun fullListItemToModifyWordListItem(fullListItem: FullListItem): ModifyWordListItem =
        ModifyWordListItem(
            id = fullListItem.listInfo.id,
            title = fullListItem.listInfo.title,
            count = fullListItem.count,
            isSelected = false,
            dictionaryId = fullListItem.dictionary.id
        )

    fun fullListItemToLocal(listItemDb: FullListItem): ListItem = ListItem(
        id = listItemDb.listInfo.id,
        title = listItemDb.listInfo.title,
        count = listItemDb.count,
        createdAt = listItemDb.listInfo.createdAt,
        updatedAt = listItemDb.listInfo.updatedAt,
        isSelected = false,
        dictionaryId = listItemDb.dictionary.id
    )

    fun listItemLocalToDb(listItem: ListItem): ListItemDb = ListItemDb(
        id = listItem.id,
        title = listItem.title,
        createdAt = listItem.createdAt,
        updatedAt = listItem.updatedAt,
        dictionaryId = listItem.dictionaryId
    )

}