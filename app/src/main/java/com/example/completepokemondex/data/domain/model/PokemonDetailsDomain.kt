package com.example.completepokemondex.data.domain.model

/**
 * Modelo de dominio que representa los detalles de un Pokémon.
 * 
 * Esta clase proporciona una representación limpia y específica de dominio para los detalles
 * de un Pokémon, abstrayendo los detalles de implementación de la capa de datos.
 * 
 * @property id Identificador único del Pokémon.
 * @property name Nombre del Pokémon con primera letra en mayúscula.
 * @property height Altura del Pokémon (en decímetros).
 * @property weight Peso del Pokémon (en hectogramos).
 * @property sprites Sprites (imágenes) del Pokémon.
 */
data class PokemonDetailsDomain(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSpritesDomain
)

