package com.example.completepokemondex.data.domain.model

/**
 * Modelo de dominio que representa los encuentros de un Pokémon en diferentes ubicaciones.
 *
 * Esta clase proporciona una representación limpia y específica de dominio para los datos de encuentro
 * de un Pokémon en diferentes áreas de los juegos Pokémon, abstrayendo los detalles de implementación 
 * de la capa de datos.
 */
data class PokemonEncountersDomain(
    val items: List<LocationAreaEncounter>
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
