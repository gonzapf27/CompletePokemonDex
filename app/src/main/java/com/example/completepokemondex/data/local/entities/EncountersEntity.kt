package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "encounters_table")
data class EncountersEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "location_area")
    val locationArea: String?, // JSON serializado

    @ColumnInfo(name = "version_details")
    val versionDetails: String? // JSON serializado
)
