package com.tcardly.core.database.entity

import androidx.room.*
import com.tcardly.core.common.model.ActivityType

@Entity(
    tableName = "activity_logs",
    foreignKeys = [
        ForeignKey(entity = BusinessCardEntity::class, parentColumns = ["id"], childColumns = ["cardId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["cardId"]), Index(value = ["timestamp"])]
)
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "cardId") val cardId: Long,
    @ColumnInfo(name = "type") val type: ActivityType,
    @ColumnInfo(name = "content") val content: String? = null,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)
