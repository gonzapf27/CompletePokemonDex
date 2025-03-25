package com.example.completepokemondex.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) que representa los sprites (imágenes) de un Pokémon obtenidos de la API.
 *
 * Esta clase encapsula las diferentes URLs de imágenes para un Pokémon que son devueltas
 * por la PokeAPI cuando se solicita información específica de un Pokémon.
 *
 * @property backDefault URL de la vista trasera predeterminada.
 * @property backFemale URL de la vista trasera femenina (puede ser nula).
 * @property backShiny URL de la vista trasera brillante.
 * @property backShinyFemale URL de la vista trasera brillante femenina (puede ser nula).
 * @property frontDefault URL de la vista frontal predeterminada.
 * @property frontFemale URL de la vista frontal femenina (puede ser nula).
 * @property frontShiny URL de la vista frontal brillante.
 * @property frontShinyFemale URL de la vista frontal brillante femenina (puede ser nula).
 * @property other Contiene colecciones adicionales de sprites como official-artwork.
 */
data class PokemonSpritesDTO(
    @SerializedName("back_default")
    val backDefault: String?,

    @SerializedName("back_female")
    val backFemale: String?,

    @SerializedName("back_shiny")
    val backShiny: String?,

    @SerializedName("back_shiny_female")
    val backShinyFemale: String?,

    @SerializedName("front_default")
    val frontDefault: String?,

    @SerializedName("front_female")
    val frontFemale: String?,

    @SerializedName("front_shiny")
    val frontShiny: String?,

    @SerializedName("front_shiny_female")
    val frontShinyFemale: String?,

    @SerializedName("other")
    val other: OtherSpritesDTO?
)

/**
 * Representa las colecciones adicionales de sprites del Pokémon
 *
 * @property dreamWorld Sprites del estilo Dream World
 * @property home Sprites del estilo Home
 * @property officialArtwork Sprites oficiales artísticos
 * @property showdown Sprites animados del estilo Showdown
 */
data class OtherSpritesDTO(
    @SerializedName("dream_world")
    val dreamWorld: DreamWorldDTO?,

    @SerializedName("home")
    val home: HomeDTO?,

    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkDTO?,

    @SerializedName("showdown")
    val showdown: ShowdownDTO?
)

/**
 * Representa los sprites del estilo Dream World
 */
data class DreamWorldDTO(
    @SerializedName("front_default")
    val frontDefault: String?,

    @SerializedName("front_female")
    val frontFemale: String?
)

/**
 * Representa los sprites del estilo Home
 */
data class HomeDTO(
    @SerializedName("front_default")
    val frontDefault: String?,

    @SerializedName("front_female")
    val frontFemale: String?,

    @SerializedName("front_shiny")
    val frontShiny: String?,

    @SerializedName("front_shiny_female")
    val frontShinyFemale: String?
)

/**
 * Representa los sprites oficiales artísticos
 */
data class OfficialArtworkDTO(
    @SerializedName("front_default")
    val frontDefault: String?,

    @SerializedName("front_shiny")
    val frontShiny: String?
)

/**
 * Representa los sprites animados del estilo Showdown
 */
data class ShowdownDTO(
    @SerializedName("back_default")
    val backDefault: String?,

    @SerializedName("back_female")
    val backFemale: String?,

    @SerializedName("back_shiny")
    val backShiny: String?,

    @SerializedName("back_shiny_female")
    val backShinyFemale: String?,

    @SerializedName("front_default")
    val frontDefault: String?,

    @SerializedName("front_female")
    val frontFemale: String?,

    @SerializedName("front_shiny")
    val frontShiny: String?,

    @SerializedName("front_shiny_female")
    val frontShinyFemale: String?
)