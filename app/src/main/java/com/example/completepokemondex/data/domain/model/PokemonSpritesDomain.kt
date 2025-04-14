package com.example.completepokemondex.data.domain.model

/**
 * Modelo de dominio que representa los sprites (imágenes) de un Pokémon.
 * 
 * Esta clase contiene las URLs de las diferentes imágenes de un Pokémon
 * incluyendo sprites básicos, Dream World, Home, Official Artwork y Showdown.
 *
 * @property backDefault URL de la vista trasera predeterminada
 * @property backFemale URL de la vista trasera femenina (puede ser nula)
 * @property backShiny URL de la vista trasera brillante
 * @property backShinyFemale URL de la vista trasera brillante femenina (puede ser nula)
 * @property frontDefault URL de la vista frontal predeterminada
 * @property frontFemale URL de la vista frontal femenina (puede ser nula)
 * @property frontShiny URL de la vista frontal brillante
 * @property frontShinyFemale URL de la vista frontal brillante femenina (puede ser nula)
 * @property dreamWorldFrontDefault URL de la vista frontal Dream World
 * @property dreamWorldFrontFemale URL de la vista frontal femenina Dream World (puede ser nula)
 * @property homeFrontDefault URL de la vista frontal Home
 * @property homeFrontFemale URL de la vista frontal femenina Home (puede ser nula)
 * @property homeFrontShiny URL de la vista frontal brillante Home
 * @property homeFrontShinyFemale URL de la vista frontal brillante femenina Home (puede ser nula)
 * @property officialArtworkDefault URL del artwork oficial predeterminado
 * @property officialArtworkShiny URL del artwork oficial brillante
 * @property showdownBackDefault URL de la vista trasera animada Showdown
 * @property showdownBackFemale URL de la vista trasera femenina animada Showdown (puede ser nula)
 * @property showdownBackShiny URL de la vista trasera brillante animada Showdown
 * @property showdownBackShinyFemale URL de la vista trasera brillante femenina animada Showdown (puede ser nula)
 * @property showdownFrontDefault URL de la vista frontal animada Showdown
 * @property showdownFrontFemale URL de la vista frontal femenina animada Showdown (puede ser nula)
 * @property showdownFrontShiny URL de la vista frontal brillante animada Showdown
 * @property showdownFrontShinyFemale URL de la vista frontal brillante femenina animada Showdown (puede ser nula)
 */
data class PokemonSpritesDomain(
    val backDefault: String?,
    val backFemale: String?,
    val backShiny: String?,
    val backShinyFemale: String?,
    val frontDefault: String?,
    val frontFemale: String?,
    val frontShiny: String?,
    val frontShinyFemale: String?,
    val dreamWorldFrontDefault: String?,
    val dreamWorldFrontFemale: String?,
    val homeFrontDefault: String?,
    val homeFrontFemale: String?,
    val homeFrontShiny: String?,
    val homeFrontShinyFemale: String?,
    val officialArtworkDefault: String?,
    val officialArtworkShiny: String?,
    val showdownBackDefault: String?,
    val showdownBackFemale: String?,
    val showdownBackShiny: String?,
    val showdownBackShinyFemale: String?,
    val showdownFrontDefault: String?,
    val showdownFrontFemale: String?,
    val showdownFrontShiny: String?,
    val showdownFrontShinyFemale: String?
)