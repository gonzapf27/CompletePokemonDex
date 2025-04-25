package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un movimiento Pokémon en la base de datos local.
 *
 * Todos los campos complejos se almacenan como JSON serializado.
 */
@Entity(tableName = "pokemon_move_table")
data class PokemonMoveEntity(
    @PrimaryKey
    @ColumnInfo(name = "move_id")
    val id: Int,

    @ColumnInfo(name = "accuracy")
    val accuracy: Int?,

    @ColumnInfo(name = "contest_combos")
    val contestCombos: String, // JSON serializado

    @ColumnInfo(name = "contest_effect")
    val contestEffect: String, // JSON serializado

    @ColumnInfo(name = "contest_type")
    val contestType: String, // JSON serializado

    @ColumnInfo(name = "damage_class")
    val damageClass: String, // JSON serializado

    @ColumnInfo(name = "effect_chance")
    val effectChance: String, // JSON serializado 

    @ColumnInfo(name = "effect_changes")
    val effectChanges: String, // JSON serializado

    @ColumnInfo(name = "effect_entries")
    val effectEntries: String, // JSON serializado

    @ColumnInfo(name = "flavor_text_entries")
    val flavorTextEntries: String, // JSON serializado

    @ColumnInfo(name = "generation")
    val generation: String, // JSON serializado

    @ColumnInfo(name = "learned_by_pokemon")
    val learnedByPokemon: String, // JSON serializado

    @ColumnInfo(name = "machines")
    val machines: String, // JSON serializado

    @ColumnInfo(name = "meta")
    val meta: String, // JSON serializado

    @ColumnInfo(name = "move_name")
    val name: String?,

    @ColumnInfo(name = "names")
    val names: String, // JSON serializado

    @ColumnInfo(name = "past_values")
    val pastValues: String, // JSON serializado

    @ColumnInfo(name = "power")
    val power: Int?,

    @ColumnInfo(name = "pp")
    val pp: Int?,

    @ColumnInfo(name = "priority")
    val priority: Int?,

    @ColumnInfo(name = "stat_changes")
    val statChanges: String, // JSON serializado

    @ColumnInfo(name = "super_contest_effect")
    val superContestEffect: String, // JSON serializado

    @ColumnInfo(name = "target")
    val target: String, // JSON serializado

    @ColumnInfo(name = "type")
    val type: String  // JSON serializado
)
