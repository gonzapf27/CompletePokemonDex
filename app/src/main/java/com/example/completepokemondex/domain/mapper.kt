package com.example.completepokemondex.domain

import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.domain.model.PokemonDomain

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
 * Convierte un objeto PokemonDTO (capa de datos) en un objeto Pokemon (capa de dominio).
 *
 * @return Un objeto Pokemon con los datos mapeados desde el DTO.
 */
fun PokemonDTO.toDomainModel(): PokemonDomain {
    return PokemonDomain(
            id = extractId(),
            name =
                    name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    },
            url = url
    )
}

/**
 * Convierte un objeto PokemonEntity (capa de datos local) en un objeto Pokemon (capa de dominio).
 *
 * @return Un objeto Pokemon con los datos mapeados desde la entidad.
 */
fun PokemonEntity.toDomainModel(): PokemonDomain {
    return PokemonDomain(
            id = id,
            name =
                    name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    },
            url = url
    )
}

/**
 * Convierte un objeto PokemonDTO (capa de datos remota) en un objeto PokemonEntity (capa de datos
 * local).
 *
 * @return Un objeto PokemonEntity con los datos mapeados desde el DTO.
 */
fun PokemonDTO.toEntity(): PokemonEntity {
    return PokemonEntity(id = extractId(), name = name, url = url)
}

/**
 * Convierte una lista de PokemonDTO en una lista de entidades PokemonEntity.
 *
 * @return Una lista de entidades PokemonEntity.
 */
fun List<PokemonDTO>.toEntityList(): List<PokemonEntity> {
    return map { it.toEntity() }
}

/**
 * Convierte una lista de PokemonDTO en una lista de objetos Pokemon de dominio.
 *
 * @return Una lista de objetos Pokemon de dominio.
 */
fun List<PokemonDTO>.toDomainModelList(): List<PokemonDomain> {
    return map { it.toDomainModel() }
}

/**
 * Convierte una lista de PokemonEntity en una lista de objetos Pokemon de dominio.
 *
 * @return Una lista de objetos Pokemon de dominio.
 */
fun List<PokemonEntity>.toEntityDomainModelList(): List<PokemonDomain> {
    return map { it.toDomainModel() }
}
