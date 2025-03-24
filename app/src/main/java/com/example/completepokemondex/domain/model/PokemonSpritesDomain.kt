package com.example.completepokemondex.domain.model

/**
 * Modelo de dominio que representa los sprites (imágenes) de un Pokémon.
 * 
 * Esta clase contiene las URLs de las diferentes imágenes de un Pokémon
 * y sirve como modelo para la capa de presentación.
 *
 * @property backDefault URL de la vista trasera predeterminada.
 * @property backFemale URL de la vista trasera femenina (puede ser nula).
 * @property backShiny URL de la vista trasera brillante.
 * @property backShinyFemale URL de la vista trasera brillante femenina (puede ser nula).
 * @property frontDefault URL de la vista frontal predeterminada.
 * @property frontFemale URL de la vista frontal femenina (puede ser nula).
 * @property frontShiny URL de la vista frontal brillante.
 * @property frontShinyFemale URL de la vista frontal brillante femenina (puede ser nula).
 */
data class PokemonSpritesDomain(
    val backDefault: String?,
    val backFemale: String?,
    val backShiny: String?,
    val backShinyFemale: String?,
    val frontDefault: String?,
    val frontFemale: String?,
    val frontShiny: String?,
    val frontShinyFemale: String?
)
