package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un elemento individual en la lista de Pokémon.
 * Esta entidad almacena la información básica de un Pokémon desde la vista de lista.
 *
 * @property id Identificador único del Pokémon en la base de datos.
 * @property name Nombre del Pokémon.
 * @property url URL para obtener los detalles completos del Pokémon.
 */
@Entity(tableName = "pokemon_table")
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
)