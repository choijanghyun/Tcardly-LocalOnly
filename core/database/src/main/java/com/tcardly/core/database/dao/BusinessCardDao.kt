package com.tcardly.core.database.dao

import androidx.room.*
import com.tcardly.core.database.entity.BusinessCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessCardDao {

    @Query("SELECT * FROM business_cards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<BusinessCardEntity>>

    @Query("SELECT * FROM business_cards ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentCards(limit: Int = 5): Flow<List<BusinessCardEntity>>

    @Query("SELECT * FROM business_cards WHERE id = :id")
    suspend fun getCardById(id: Long): BusinessCardEntity?

    @Query("SELECT * FROM business_cards WHERE id = :id")
    fun observeCardById(id: Long): Flow<BusinessCardEntity?>

    @Query("""
        SELECT * FROM business_cards 
        WHERE name LIKE '%' || :query || '%' 
        OR company LIKE '%' || :query || '%'
        OR position LIKE '%' || :query || '%'
        OR mobilePhone LIKE '%' || :query || '%'
        OR email LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN name = :query THEN 0
                 WHEN name LIKE :query || '%' THEN 1
                 WHEN name LIKE '%' || :query || '%' THEN 2
                 ELSE 3 END,
            createdAt DESC
    """)
    fun searchCards(query: String): Flow<List<BusinessCardEntity>>

    @Query("SELECT * FROM business_cards WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun getCardsByGroup(groupId: Long): Flow<List<BusinessCardEntity>>

    @Query("SELECT * FROM business_cards WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteCards(): Flow<List<BusinessCardEntity>>

    @Query("""
        SELECT * FROM business_cards 
        WHERE name = :name AND mobilePhone = :phone
        LIMIT 1
    """)
    suspend fun findDuplicate(name: String, phone: String): BusinessCardEntity?

    @Query("SELECT COUNT(*) FROM business_cards")
    fun getCardCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: BusinessCardEntity): Long

    @Update
    suspend fun updateCard(card: BusinessCardEntity)

    @Query("UPDATE business_cards SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :cardId")
    suspend fun toggleFavorite(cardId: Long, isFavorite: Boolean, updatedAt: Long)

    @Query("DELETE FROM business_cards WHERE id = :id")
    suspend fun deleteCard(id: Long)

    @Query("SELECT * FROM business_cards ORDER BY name ASC")
    fun getAllCardsSortedByName(): Flow<List<BusinessCardEntity>>

    @Query("SELECT * FROM business_cards ORDER BY company ASC, name ASC")
    fun getAllCardsSortedByCompany(): Flow<List<BusinessCardEntity>>
}
