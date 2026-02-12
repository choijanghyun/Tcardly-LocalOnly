package com.tcardly.core.database.dao

import androidx.room.*
import com.tcardly.core.database.entity.MeetingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meetings WHERE dateTime >= :startOfDay AND dateTime < :endOfDay ORDER BY dateTime ASC")
    fun getTodayMeetings(startOfDay: Long, endOfDay: Long): Flow<List<MeetingEntity>>

    @Query("SELECT * FROM meetings WHERE cardId = :cardId ORDER BY dateTime DESC")
    fun getMeetingsForCard(cardId: Long): Flow<List<MeetingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meeting: MeetingEntity): Long

    @Update
    suspend fun update(meeting: MeetingEntity)

    @Query("DELETE FROM meetings WHERE id = :id")
    suspend fun delete(id: Long)
}
