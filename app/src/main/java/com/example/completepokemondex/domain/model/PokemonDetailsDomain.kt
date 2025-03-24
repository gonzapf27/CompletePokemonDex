package com.example.completepokemondex.domain.model

/**
 * Modelo de dominio que representa los detalles específicos de un Pokémon.
 * 
 * Esta clase contiene información detallada de un Pokémon
 * y sirve como modelo para la capa de presentación.
 *
 * @property id Identificador único del Pokémon.
 * @property name Nombre del Pokémon.
 * @property height Altura del Pokémon (en decímetros).
 * @property weight Peso del Pokémon (en hectogramos).
 *@property sprites Sprites del Pokémon.
 */
data class PokemonDetailsDomain(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSpritesDomain
)
