package com.example.completepokemondex.domain

import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity
import com.example.completepokemondex.data.local.entities.PokemonSpritesEntity
import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.remote.models.PokemonSpritesDTO
import com.example.completepokemondex.domain.model.PokemonDomain
import com.example.completepokemondex.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.domain.model.PokemonSpritesDomain

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

/**
 * Convierte un objeto PokemonDetailsDTO (capa de datos remota) en un objeto PokemonDetailsEntity (capa de datos local).
 *
 * @return Un objeto PokemonDetailsEntity con los datos mapeados desde el DTO.
 */
fun PokemonDetailsDTO.toEntity(): PokemonDetailsEntity {
    return PokemonDetailsEntity(
        id = id,
        name = name,
        height = height,
        weight = weight
    )
}

/**
 * Convierte un objeto PokemonDetailsDTO (capa de datos) en un objeto PokemonDetailsDomain (capa de dominio).
 *
 * @return Un objeto PokemonDetailsDomain con los datos mapeados desde el DTO.
 */
fun PokemonDetailsDTO.toDomainModel(): PokemonDetailsDomain {
    return PokemonDetailsDomain(
        id = id,
        name = name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        },
        height = height,
        weight = weight
    )
}

/**
 * Convierte un objeto PokemonDetailsEntity (capa de datos local) en un objeto PokemonDetailsDomain (capa de dominio).
 *
 * @return Un objeto PokemonDetailsDomain con los datos mapeados desde la entidad.
 */
fun PokemonDetailsEntity.toDomainModel(): PokemonDetailsDomain {
    return PokemonDetailsDomain(
        id = id,
        name = name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        },
        height = height,
        weight = weight
    )
}

/**
 * Convierte un objeto PokemonSpritesDTO (capa de datos remota) en un objeto PokemonSpritesEntity (capa de datos local).
 *
 * @param pokemonId El ID del Pokémon al que pertenecen los sprites.
 * @return Un objeto PokemonSpritesEntity con los datos mapeados desde el DTO.
 */
fun PokemonSpritesDTO.toEntity(pokemonId: Int): PokemonSpritesEntity {
    return PokemonSpritesEntity(
        pokemonId = pokemonId,
        backDefault = backDefault,
        backFemale = backFemale,
        backShiny = backShiny,
        backShinyFemale = backShinyFemale,
        frontDefault = frontDefault,
        frontFemale = frontFemale,
        frontShiny = frontShiny,
        frontShinyFemale = frontShinyFemale
    )
}

/**
 * Convierte un objeto PokemonSpritesDTO (capa de datos) en un objeto PokemonSpritesDomain (capa de dominio).
 *
 * @return Un objeto PokemonSpritesDomain con los datos mapeados desde el DTO.
 */
fun PokemonSpritesDTO.toDomainModel(): PokemonSpritesDomain {
    return PokemonSpritesDomain(
        backDefault = backDefault,
        backFemale = backFemale,
        backShiny = backShiny,
        backShinyFemale = backShinyFemale,
        frontDefault = frontDefault,
        frontFemale = frontFemale,
        frontShiny = frontShiny,
        frontShinyFemale = frontShinyFemale
    )
}

/**
 * Convierte un objeto PokemonSpritesEntity (capa de datos local) en un objeto PokemonSpritesDomain (capa de dominio).
 *
 * @return Un objeto PokemonSpritesDomain con los datos mapeados desde la entidad.
 */
fun PokemonSpritesEntity.toDomainModel(): PokemonSpritesDomain {
    return PokemonSpritesDomain(
        backDefault = backDefault,
        backFemale = backFemale,
        backShiny = backShiny,
        backShinyFemale = backShinyFemale,
        frontDefault = frontDefault,
        frontFemale = frontFemale,
        frontShiny = frontShiny,
        frontShinyFemale = frontShinyFemale
    )
}
