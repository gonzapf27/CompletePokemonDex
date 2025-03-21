package com.example.completepokemondex.data.model

/**
 * Clase de datos que representa un Pokémon.
 *
 * @property name El nombre del Pokémon.
 * @property height La altura del Pokémon en decímetros.
 * @property weight El peso del Pokémon en hectogramos.
 * @property sprites Los sprites del Pokémon.
 */
data class Pokemon(
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites
)
