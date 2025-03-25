package com.example.completepokemondex.domain.model

/**
 * Modelo de dominio para un Pokémon básico en la lista
 */
data class PokemonDomain(
    val id: Int,
    val name: String,
    val imageUrl: String? = null  // URL de la imagen del Pokémon (frontDefault sprite)
)
