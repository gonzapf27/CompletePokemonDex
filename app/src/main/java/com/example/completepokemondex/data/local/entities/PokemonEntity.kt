package com.example.completepokemondex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una entidad de Pokémon en la base de datos local.
 *
 * @property id Identificador único del Pokémon.
 * @property name Nombre del Pokémon.
 * @property base_experience Experiencia base del Pokémon.
 * @property height Altura del Pokémon en decímetros.
 * @property weight Peso del Pokémon en hectogramos.
 * @property sprites Sprites del Pokémon.
 */
@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSpritesEntity
)