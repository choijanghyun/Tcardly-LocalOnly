package com.tcardly.core.database.entity

import androidx.room.*

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: String = "#0D9488",
    @ColumnInfo(name = "isDefault") val isDefault: Boolean = false,
    @ColumnInfo(name = "sortOrder") val sortOrder: Int = 0
)
