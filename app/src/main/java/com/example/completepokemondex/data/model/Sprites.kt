package com.example.completepokemondex.data.model


/**
 * Representa los diferentes sprites de un Pokémon.
 *
 * @property back_default URL del sprite trasero por defecto.
 * @property back_female URL del sprite trasero para la versión femenina.
 * @property back_shiny URL del sprite trasero en su versión shiny.
 * @property back_shiny_female URL del sprite trasero shiny para la versión femenina.
 * @property front_default URL del sprite delantero por defecto.
 * @property front_female URL del sprite delantero para la versión femenina.
 * @property front_shiny URL del sprite delantero en su versión shiny.
 * @property front_shiny_female URL del sprite delantero shiny para la versión femenina.
 */
data class Sprites(
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)