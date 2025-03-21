package com.example.completepokemondex.data.model

/**
 * Clase de datos que representa un Pokémon para uso en la lógica de negocio y comunicación entre capas.
 * Esta clase está diseñada para ser independiente de frameworks como Room o Retrofit.
 *
 * @property id Identificador único del Pokémon.
 * @property name El nombre del Pokémon.
 * @property base_experience Experiencia base del Pokémon.
 * @property height La altura del Pokémon en decímetros.
 * @property weight El peso del Pokémon en hectogramos.
 * @property sprites Los sprites del Pokémon.
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val weight: Int,
    val sprites: Sprites
)
