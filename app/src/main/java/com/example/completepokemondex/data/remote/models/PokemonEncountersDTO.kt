package com.example.completepokemondex.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) que representa los encuentros de un Pokémon en diferentes ubicaciones.
 *
 * Esta clase encapsula la información sobre donde puede ser encontrado un Pokémon
 * en diferentes áreas de los juegos Pokémon, tal como es devuelta por la PokeAPI.
 */
data class PokemonEncountersDTO(
    @SerializedName("location_area")
    val locationArea: LocationArea?,
    @SerializedName("version_details")
    val versionDetails: List<VersionDetail?>?
) {
    data class LocationArea(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class VersionDetail(
        @SerializedName("encounter_details")
        val encounterDetails: List<EncounterDetail?>?,
        @SerializedName("max_chance")
        val maxChance: Int?,
        @SerializedName("version")
        val version: Version?
    ) {
        data class EncounterDetail(
            @SerializedName("chance")
            val chance: Int?,
            @SerializedName("condition_values")
            val conditionValues: List<ConditionValue?>?,
            @SerializedName("max_level")
            val maxLevel: Int?,
            @SerializedName("method")
            val method: Method?,
            @SerializedName("min_level")
            val minLevel: Int?
        ) {
            data class ConditionValue(
                @SerializedName("name")
                val name: String?,
                @SerializedName("url")
                val url: String?
            )

            data class Method(
                @SerializedName("name")
                val name: String?,
                @SerializedName("url")
                val url: String?
            )
        }

        data class Version(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }
}
