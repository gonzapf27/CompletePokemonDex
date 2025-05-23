package com.example.completepokemondex.data.domain.model

/**
 * Modelo de dominio para un Pokémon básico en la lista
 */
data class PokemonDomain(
    val id: Int,
    val name: String,
    val url: String,
    val imageUrl: String? = null,  // URL de la imagen del Pokémon (frontDefault sprite)
    val favorite: Boolean = false  // Nuevo campo para favorito
)
