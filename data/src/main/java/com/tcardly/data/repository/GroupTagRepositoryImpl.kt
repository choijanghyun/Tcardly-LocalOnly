package com.tcardly.data.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.database.dao.GroupDao
import com.tcardly.core.database.dao.TagDao
import com.tcardly.core.database.entity.CardTagEntity
import com.tcardly.core.database.entity.GroupEntity
import com.tcardly.core.database.entity.TagEntity
import com.tcardly.domain.model.Tag
import com.tcardly.domain.repository.Group
import com.tcardly.domain.repository.GroupTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupTagRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
    private val tagDao: TagDao
) : GroupTagRepository {

    override fun getAllGroups(): Flow<List<Group>> {
        return groupDao.getAllGroups().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTagsForCard(cardId: Long): Flow<List<Tag>> {
        return tagDao.getTagsForCard(cardId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createGroup(group: Group): ResultWrapper<Long> {
        return try {
            val entity = GroupEntity(
                name = group.name, color = group.color,
                isDefault = group.isDefault, sortOrder = group.sortOrder
            )
            val id = groupDao.insert(entity)
            Timber.d("그룹 생성 성공: id=$id, name=${group.name}")
            ResultWrapper.Success(id)
        } catch (e: Exception) {
            Timber.e(e, "그룹 생성 실패: name=${group.name}")
            ResultWrapper.Error("그룹 생성에 실패했습니다.", e)
        }
    }

    override suspend fun updateGroup(group: Group): ResultWrapper<Unit> {
        return try {
            val entity = GroupEntity(
                id = group.id, name = group.name, color = group.color,
                isDefault = group.isDefault, sortOrder = group.sortOrder
            )
            groupDao.update(entity)
            Timber.d("그룹 수정 성공: id=${group.id}")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "그룹 수정 실패: id=${group.id}")
            ResultWrapper.Error("그룹 수정에 실패했습니다.", e)
        }
    }

    override suspend fun deleteGroup(groupId: Long): ResultWrapper<Unit> {
        return try {
            groupDao.delete(groupId)
            Timber.d("그룹 삭제 성공: id=$groupId")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "그룹 삭제 실패: id=$groupId")
            ResultWrapper.Error("그룹 삭제에 실패했습니다.", e)
        }
    }

    override suspend fun createTag(tag: Tag): ResultWrapper<Long> {
        return try {
            val entity = TagEntity(
                name = tag.name, bgColor = tag.bgColor,
                textColor = tag.textColor, isDefault = tag.isDefault
            )
            val id = tagDao.insertTag(entity)
            Timber.d("태그 생성 성공: id=$id, name=${tag.name}")
            ResultWrapper.Success(id)
        } catch (e: Exception) {
            Timber.e(e, "태그 생성 실패: name=${tag.name}")
            ResultWrapper.Error("태그 생성에 실패했습니다.", e)
        }
    }

    override suspend fun deleteTag(tagId: Long): ResultWrapper<Unit> {
        return try {
            tagDao.deleteTag(tagId)
            Timber.d("태그 삭제 성공: id=$tagId")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "태그 삭제 실패: id=$tagId")
            ResultWrapper.Error("태그 삭제에 실패했습니다.", e)
        }
    }

    override suspend fun addTagToCard(cardId: Long, tagId: Long): ResultWrapper<Unit> {
        return try {
            tagDao.insertCardTag(CardTagEntity(cardId, tagId))
            Timber.d("태그 추가 성공: cardId=$cardId, tagId=$tagId")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "태그 추가 실패: cardId=$cardId, tagId=$tagId")
            ResultWrapper.Error("태그 추가에 실패했습니다.", e)
        }
    }

    override suspend fun removeTagFromCard(cardId: Long, tagId: Long): ResultWrapper<Unit> {
        return try {
            tagDao.removeCardTag(cardId, tagId)
            Timber.d("태그 제거 성공: cardId=$cardId, tagId=$tagId")
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "태그 제거 실패: cardId=$cardId, tagId=$tagId")
            ResultWrapper.Error("태그 제거에 실패했습니다.", e)
        }
    }

    private fun GroupEntity.toDomain() = Group(
        id = id, name = name, color = color,
        isDefault = isDefault, sortOrder = sortOrder
    )

    private fun TagEntity.toDomain() = Tag(
        id = id, name = name, bgColor = bgColor,
        textColor = textColor, isDefault = isDefault
    )
}
