package com.tcardly.domain.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.Tag
import kotlinx.coroutines.flow.Flow

data class Group(
    val id: Long = 0,
    val name: String,
    val color: String = "#0D9488",
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)

interface GroupTagRepository {
    fun getAllGroups(): Flow<List<Group>>
    fun getAllTags(): Flow<List<Tag>>
    fun getTagsForCard(cardId: Long): Flow<List<Tag>>
    suspend fun createGroup(group: Group): ResultWrapper<Long>
    suspend fun updateGroup(group: Group): ResultWrapper<Unit>
    suspend fun deleteGroup(groupId: Long): ResultWrapper<Unit>
    suspend fun createTag(tag: Tag): ResultWrapper<Long>
    suspend fun deleteTag(tagId: Long): ResultWrapper<Unit>
    suspend fun addTagToCard(cardId: Long, tagId: Long): ResultWrapper<Unit>
    suspend fun removeTagFromCard(cardId: Long, tagId: Long): ResultWrapper<Unit>
}
