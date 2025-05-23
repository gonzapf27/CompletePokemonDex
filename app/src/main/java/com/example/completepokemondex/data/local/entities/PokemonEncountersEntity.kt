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
) {
    data class LocationAreaEncounter(
        val location_area: LocationArea?,
        val version_details: List<VersionDetail?>?
    ) {
        data class LocationArea(
            val name: String?,
            val url: String?
        )

        data class VersionDetail(
            val encounter_details: List<EncounterDetail?>?,
            val max_chance: Int?,
            val version: Version?
        ) {
            data class EncounterDetail(
                val chance: Int?,
                val condition_values: List<ConditionValue?>?,
                val max_level: Int?,
                val method: Method?,
                val min_level: Int?
            ) {
                data class ConditionValue(
                    val name: String?,
                    val url: String?
                )

                data class Method(
                    val name: String?,
                    val url: String?
                )
            }

            data class Version(
                val name: String?,
                val url: String?
            )
        }
    }
}
