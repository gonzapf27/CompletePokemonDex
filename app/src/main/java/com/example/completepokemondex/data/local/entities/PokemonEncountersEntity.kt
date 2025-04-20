package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa los encuentros de un Pokémon en diferentes ubicaciones.
 *
 * Esta clase se utiliza para almacenar información sobre dónde puede encontrarse un Pokémon
 * en los diferentes juegos de la saga.
 */
@Entity(tableName = "pokemon_encounters_table")
data class PokemonEncountersEntity(
    @PrimaryKey
    @ColumnInfo(name = "pokemon_id")
    val pokemonId: Int,
    
    @ColumnInfo(name = "encounters")
    val encounters: String // JSON serializado para almacenar la lista de LocationAreaEncounter
)
