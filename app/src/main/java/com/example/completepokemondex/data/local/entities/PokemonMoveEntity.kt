package com.example.completepokemondex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Entidad que representa un movimiento de Pokémon en la base de datos local.
 *
 * @property id Identificador único del movimiento.
 * @property name Nombre del movimiento.
 * @property accuracy Precisión del movimiento.
 * @property power Poder del movimiento.
 * @property pp Puntos de poder del movimiento.
 * @property priority Prioridad del movimiento.
 * @property contestCombos Combinaciones de concurso (almacenadas como JSON).
 * @property contestEffect Efecto de concurso (almacenado como JSON).
 * @property contestType Tipo de concurso (almacenado como JSON).
 * @property damageClass Clase de daño (almacenado como JSON).
 * @property effectChance Probabilidad de efecto (almacenado como JSON).
 * @property effectChanges Cambios de efecto (almacenado como JSON).
 * @property effectEntries Entradas de efecto (almacenado como JSON).
 * @property flavorTextEntries Entradas de texto descriptivo (almacenado como JSON).
 * @property generation Generación (almacenado como JSON).
 * @property learnedByPokemon Lista de Pokémon que pueden aprender este movimiento (almacenado como JSON).
 * @property machines Máquinas relacionadas (almacenado como JSON).
 * @property meta Metadatos (almacenado como JSON).
 * @property names Nombres en diferentes idiomas (almacenado como JSON).
 * @property pastValues Valores anteriores (almacenado como JSON).
 * @property statChanges Cambios de estadísticas (almacenado como JSON).
 * @property superContestEffect Efecto de súper concurso (almacenado como JSON).
 * @property target Objetivo del movimiento (almacenado como JSON).
 * @property type Tipo del movimiento (almacenado como JSON).
 */
@Entity(tableName = "pokemon_move_table")
data class PokemonMoveEntity(
    @PrimaryKey
    @ColumnInfo(name = "move_id")
    val id: Int,
    
    @ColumnInfo(name = "move_name")
    val name: String?,
    
    @ColumnInfo(name = "accuracy")
    val accuracy: Int?,
    
    @ColumnInfo(name = "power")
    val power: Int?,
    
    @ColumnInfo(name = "pp")
    val pp: Int?,
    
    @ColumnInfo(name = "priority")
    val priority: Int?,
    
    @ColumnInfo(name = "contest_combos")
    val contestCombos: String?,
    
    @ColumnInfo(name = "contest_effect")
    val contestEffect: String?,
    
    @ColumnInfo(name = "contest_type")
    val contestType: String?,
    
    @ColumnInfo(name = "damage_class")
    val damageClass: String?,
    
    @ColumnInfo(name = "effect_chance")
    val effectChance: String?,
    
    @ColumnInfo(name = "effect_changes")
    val effectChanges: String?,
    
    @ColumnInfo(name = "effect_entries")
    val effectEntries: String?,
    
    @ColumnInfo(name = "flavor_text_entries")
    val flavorTextEntries: String?,
    
    @ColumnInfo(name = "generation")
    val generation: String?,
    
    @ColumnInfo(name = "learned_by_pokemon")
    val learnedByPokemon: String?,
    
    @ColumnInfo(name = "machines")
    val machines: String?,
    
    @ColumnInfo(name = "meta")
    val meta: String?,
    
    @ColumnInfo(name = "names")
    val names: String?,
    
    @ColumnInfo(name = "past_values")
    val pastValues: String?,
    
    @ColumnInfo(name = "stat_changes")
    val statChanges: String?,
    
    @ColumnInfo(name = "super_contest_effect")
    val superContestEffect: String?,
    
    @ColumnInfo(name = "target")
    val target: String?,
    
    @ColumnInfo(name = "type")
    val type: String?
)
