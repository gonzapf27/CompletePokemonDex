package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un tipo Pokémon en la base de datos local.
 * Los campos complejos se almacenan como JSON serializado.
 */
@Entity(tableName = "type_table")
data class TypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "type_id")
    val id: Int,

    @ColumnInfo(name = "damage_relations")
    val damageRelations: String, // JSON serializado

    @ColumnInfo(name = "game_indices")
    val gameIndices: String, // JSON serializado

    @ColumnInfo(name = "generation")
    val generation: String, // JSON serializado

    @ColumnInfo(name = "move_damage_class")
    val moveDamageClass: String, // JSON serializado

    @ColumnInfo(name = "moves")
    val moves: String, // JSON serializado

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "names")
    val names: String, // JSON serializado

    @ColumnInfo(name = "past_damage_relations")
    val pastDamageRelations: String, // JSON serializado

    @ColumnInfo(name = "pokemon")
    val pokemon: String, // JSON serializado

    @ColumnInfo(name = "sprites")
    val sprites: String // JSON serializado
)
