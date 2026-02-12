package com.tcardly.core.database.entity

import androidx.room.*

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "bgColor") val bgColor: String,
    @ColumnInfo(name = "textColor") val textColor: String,
    @ColumnInfo(name = "isDefault") val isDefault: Boolean = false
)

@Entity(
    tableName = "card_tags",
    primaryKeys = ["cardId", "tagId"],
    foreignKeys = [
        ForeignKey(entity = BusinessCardEntity::class, parentColumns = ["id"], childColumns = ["cardId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TagEntity::class, parentColumns = ["id"], childColumns = ["tagId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class CardTagEntity(
    val cardId: Long,
    val tagId: Long
)
