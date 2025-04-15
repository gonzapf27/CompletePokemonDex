package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ability_table")
data class AbilityEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "effect_changes")
    val effectChanges: String?, // JSON serializado

    @ColumnInfo(name = "effect_entries")
    val effectEntries: String?, // JSON serializado

    @ColumnInfo(name = "flavor_text_entries")
    val flavorTextEntries: String?, // JSON serializado

    @ColumnInfo(name = "generation")
    val generation: String?, // JSON serializado

    @ColumnInfo(name = "is_main_series")
    val isMainSeries: Boolean?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "names")
    val names: String?, // JSON serializado

    @ColumnInfo(name = "pokemon")
    val pokemon: String? // JSON serializado
)
