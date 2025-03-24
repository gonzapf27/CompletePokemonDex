package com.example.completepokemondex.domain.model

/**
 * Modelo de dominio que representa un Pokémon.
 * 
 * Esta clase contiene la información esencial de un Pokémon
 * y sirve como modelo para la capa de presentación.
 *
 * @property id Identificador único del Pokémon.
 * @property name Nombre del Pokémon.
 * @property url URL para obtener detalles adicionales del Pokémon.
 */
data class PokemonDomain(
    val id: Int,
    val name: String,
    val url: String
)
