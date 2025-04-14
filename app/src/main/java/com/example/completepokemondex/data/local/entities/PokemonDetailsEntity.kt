package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_details_table")
data class PokemonDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "pokemon_id")
    val id: Int,

    @ColumnInfo(name = "pokemon_name")
    val name: String,

    @ColumnInfo(name = "pokemon_height")
    val height: Int,

    @ColumnInfo(name = "pokemon_weight")
    val weight: Int,

    @ColumnInfo(name = "base_experience")
    val baseExperience: Int,

    @ColumnInfo(name = "is_default")
    val isDefault: Boolean,

    @ColumnInfo(name = "location_area_encounters")
    val locationAreaEncounters: String,

    @ColumnInfo(name = "order")
    val order: Int,

    @ColumnInfo(name = "abilities")
    val abilities: String, // JSON serializado

    @ColumnInfo(name = "cries")
    val cries: String, // JSON serializado

    @ColumnInfo(name = "forms")
    val forms: String, // JSON serializado

    @ColumnInfo(name = "game_indices")
    val gameIndices: String, // JSON serializado

    @ColumnInfo(name = "held_items")
    val heldItems: String, // JSON serializado

    @ColumnInfo(name = "moves")
    val moves: String, // JSON serializado

    @ColumnInfo(name = "past_abilities")
    val pastAbilities: String, // JSON serializado

    @ColumnInfo(name = "past_types")
    val pastTypes: String, // JSON serializado

    @ColumnInfo(name = "species")
    val species: String, // JSON serializado

    @ColumnInfo(name = "sprites")
    val sprites: String, // JSON serializado

    @ColumnInfo(name = "stats")
    val stats: String, // JSON serializado

    @ColumnInfo(name = "types")
    val types: String // JSON serializado
)