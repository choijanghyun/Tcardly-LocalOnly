package com.tcardly.core.database.dao

import androidx.room.*
import com.tcardly.core.database.entity.TagEntity
import com.tcardly.core.database.entity.CardTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tags ORDER BY isDefault DESC, name ASC")
    fun getAllTags(): Flow<List<TagEntity>>

    @Query("SELECT t.* FROM tags t INNER JOIN card_tags ct ON t.id = ct.tagId WHERE ct.cardId = :cardId")
    fun getTagsForCard(cardId: Long): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardTag(cardTag: CardTagEntity)

    @Query("DELETE FROM card_tags WHERE cardId = :cardId AND tagId = :tagId")
    suspend fun removeCardTag(cardId: Long, tagId: Long)

    @Query("DELETE FROM card_tags WHERE cardId = :cardId")
    suspend fun removeAllCardTags(cardId: Long)

    @Query("DELETE FROM tags WHERE id = :id AND isDefault = 0")
    suspend fun deleteTag(id: Long)
}
