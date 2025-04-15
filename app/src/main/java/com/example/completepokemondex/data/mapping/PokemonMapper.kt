package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.remote.models.PokemonDTO

/**
 * Extrae el ID del Pokémon a partir de la URL proporcionada por la API.
 *
 * @return El ID del Pokémon extraído de la URL.
 */
fun PokemonDTO.extractId(): Int {
    // La URL suele tener un formato como: https://pokeapi.co/api/v2/pokemon/1/
    // Extraemos el número del final
    val segments = url.trim('/').split('/')
    return segments.last().toIntOrNull() ?: 0
}

/**
 * Convierte una lista de PokemonDTO de la API a una lista de PokemonEntity para la base de datos.
 *
 * @return Lista de PokemonEntity convertida desde PokemonDTO.
 */
fun List<PokemonDTO>.PokemonDTOToEntityList(): List<PokemonEntity> {
    return this.map { pokemonDTO ->
        PokemonEntity(
            id = pokemonDTO.extractId(),
            name = pokemonDTO.name,
            url = pokemonDTO.url
        )
    }
}

/**
 * Convierte una lista de entidades PokemonEntity a objetos de dominio PokemonDomain.
 *
 * @return Lista de PokemonDomain convertida desde PokemonEntity.
 */
fun List<PokemonEntity>.pokemonEntityToDomainList(): List<PokemonDomain> {
    return this.map { pokemonEntity ->
        PokemonDomain(
            id = pokemonEntity.id,
            name = pokemonEntity.name.replaceFirstChar { it.uppercase() }, // Capitalizar primera letra
            url = pokemonEntity.url,
            imageUrl = null // Se llenará después al cargar las imágenes
        )
    }
}

/**
 * Convierte una lista de PokemonDTO de la API directamente a objetos de dominio PokemonDomain.
 *
 * @return Lista de PokemonDomain convertida desde PokemonDTO.
 */
fun List<PokemonDTO>.pokemonDTOToDomainList(): List<PokemonDomain> {
    return this.map { pokemonDTO ->
        PokemonDomain(
            id = pokemonDTO.extractId(),
            name = pokemonDTO.name.replaceFirstChar { it.uppercase() }, // Capitalizar primera letra
            url = pokemonDTO.url,
            imageUrl = null // Se llenará después al cargar las imágenes
        )
    }
}