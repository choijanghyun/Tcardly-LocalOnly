package com.tcardly.core.database.entity

import androidx.room.*

@Entity(
    tableName = "meetings",
    foreignKeys = [
        ForeignKey(entity = BusinessCardEntity::class, parentColumns = ["id"], childColumns = ["cardId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["cardId"]), Index(value = ["dateTime"])]
)
data class MeetingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "cardId") val cardId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "dateTime") val dateTime: Long,
    @ColumnInfo(name = "location") val location: String? = null,
    @ColumnInfo(name = "memo") val memo: String? = null
)
