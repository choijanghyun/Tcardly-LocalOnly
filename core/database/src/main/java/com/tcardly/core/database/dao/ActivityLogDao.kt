package com.tcardly.core.database.dao

import androidx.room.*
import com.tcardly.core.database.entity.ActivityLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs WHERE cardId = :cardId ORDER BY timestamp DESC")
    fun getLogsForCard(cardId: Long): Flow<List<ActivityLogEntity>>

    @Insert
    suspend fun insert(log: ActivityLogEntity): Long

    @Query("DELETE FROM activity_logs WHERE id = :id")
    suspend fun delete(id: Long)
}
