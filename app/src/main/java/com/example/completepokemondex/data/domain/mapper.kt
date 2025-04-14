package com.example.completepokemondex.data.domain

import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity
import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.local.entities.PokemonSpritesEmbedded
import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.remote.models.PokemonSpritesDTO
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.example.completepokemondex.data.domain.model.PokemonSpritesDomain

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
fun PokemonDTO.pokemonDTOToDomain(): PokemonDomain {
    return PokemonDomain(
        id = extractId(),
        name =
            name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            },
    )
}

/**
 * Convierte un objeto PokemonEntity (capa de datos local) en un objeto Pokemon (capa de dominio).
 *
 * @return Un objeto Pokemon con los datos mapeados desde la entidad.
 */
fun PokemonEntity.pokemonEntityToDomain(): PokemonDomain {
    return PokemonDomain(
        id = id,
        name =
            name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            },
    )
}

/**
 * Convierte un objeto PokemonDTO (capa de datos remota) en un objeto PokemonEntity (capa de datos
 * local).
 *
 * @return Un objeto PokemonEntity con los datos mapeados desde el DTO.
 */
fun PokemonDTO.pokemonDTOToEntity(): PokemonEntity {
    return PokemonEntity(id = extractId(), name = name, url = url)
}

/**
 * Convierte una lista de PokemonDTO en una lista de entidades PokemonEntity.
 *
 * @return Una lista de entidades PokemonEntity.
 */
fun List<PokemonDTO>.PokemonDTOToEntityList(): List<PokemonEntity> {
    return this.map { it.pokemonDTOToEntity() }
}

/**
 * Convierte una lista de PokemonDTO en una lista de objetos Pokemon de dominio.
 *
 * @return Una lista de objetos Pokemon de dominio.
 */
fun List<PokemonDTO>.pokemonDTOToDomainList(): List<PokemonDomain> {
    return map { it.pokemonDTOToDomain() }
}


/**
 * Convierte una lista de PokemonEntity en una lista de objetos Pokemon de dominio.
 *
 * @return Una lista de objetos Pokemon de dominio.
 */
fun List<PokemonEntity>.pokemonEntityToDomainList(): List<PokemonDomain> {
    return map { it.pokemonEntityToDomain() }
}

/**
 * Convierte un objeto PokemonDetailsDTO (capa de datos) en un objeto PokemonDetailsDomain (capa de dominio).
 *
 * @return Un objeto PokemonDetailsDomain con los datos mapeados desde el DTO.
 */
fun PokemonDetailsDTO.pokemonDetailsDTOToDomain(): PokemonDetailsDomain {
    return PokemonDetailsDomain(
        id = id,
        name = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
        height = height,
        weight = weight,
        sprites = sprites.pokemonSpritesDTOToDomain()
    )
}

/**
 * Convierte un objeto PokemonDetailsEntity (capa de datos local) en un objeto PokemonDetailsDomain (capa de dominio).
 *
 * @return Un objeto PokemonDetailsDomain con los datos mapeados desde la entidad.
 */
fun PokemonDetailsEntity.pokemonDetailsEntityToDomain(): PokemonDetailsDomain {
    return PokemonDetailsDomain(
        id = id,
        name = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
        height = height,
        weight = weight,
        sprites = sprites.pokemonSpritesEmbeddedToDomain()
    )
}

/**
 * Convierte un objeto PokemonDetailsDTO (capa de datos remota) en un objeto PokemonDetailsEntity (capa de datos local).
 *
 * @return Un objeto PokemonDetailsEntity con los datos mapeados desde el DTO.
 */
fun PokemonDetailsDTO.pokemonDetailsDTOToEntity(): PokemonDetailsEntity {
    return PokemonDetailsEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        sprites = sprites.pokemonSpritesDTOToEmbedded()
    )
}

/**
 * Convierte un objeto PokemonSpritesDTO (capa de datos) en un objeto PokemonSpritesDomain (capa de dominio).
 *
 * @return Un objeto PokemonSpritesDomain con los datos mapeados desde el DTO.
 */
fun PokemonSpritesDTO.pokemonSpritesDTOToDomain(): PokemonSpritesDomain {
    return PokemonSpritesDomain(
        backDefault = backDefault,
        backFemale = backFemale,
        backShiny = backShiny,
        backShinyFemale = backShinyFemale,
        frontDefault = frontDefault,
        frontFemale = frontFemale,
        frontShiny = frontShiny,
        frontShinyFemale = frontShinyFemale,
        officialArtworkDefault = other?.officialArtwork?.frontDefault,
        officialArtworkShiny = other?.officialArtwork?.frontShiny,
        dreamWorldFrontDefault = other?.dreamWorld?.frontDefault,
        dreamWorldFrontFemale = other?.dreamWorld?.frontFemale,
        homeFrontDefault = other?.home?.frontDefault,
        homeFrontFemale = other?.home?.frontFemale,
        homeFrontShiny = other?.home?.frontShiny,
        homeFrontShinyFemale = other?.home?.frontShinyFemale,
        showdownBackDefault = other?.showdown?.backDefault,
        showdownBackFemale = other?.showdown?.backFemale,
        showdownBackShiny = other?.showdown?.backShiny,
        showdownBackShinyFemale = other?.showdown?.backShinyFemale,
        showdownFrontDefault = other?.showdown?.frontDefault,
        showdownFrontFemale = other?.showdown?.frontFemale,
        showdownFrontShiny = other?.showdown?.frontShiny,
        showdownFrontShinyFemale = other?.showdown?.frontShinyFemale
    )
}

/**
 * Convierte un objeto PokemonSpritesEmbedded (capa de datos local) en un objeto PokemonSpritesDomain (capa de dominio).
 *
 * @return Un objeto PokemonSpritesDomain con los datos mapeados desde la entidad.
 */
fun PokemonSpritesEmbedded.pokemonSpritesEmbeddedToDomain(): PokemonSpritesDomain {
    return PokemonSpritesDomain(
        backDefault = backDefault,
        backFemale = backFemale,
        backShiny = backShiny,
        backShinyFemale = backShinyFemale,
        frontDefault = frontDefault,
        frontFemale = frontFemale,
        frontShiny = frontShiny,
        frontShinyFemale = frontShinyFemale,
        officialArtworkDefault = officialArtworkDefault,
        officialArtworkShiny = officialArtworkShiny,
        dreamWorldFrontDefault = dreamWorldFrontDefault,
        dreamWorldFrontFemale = dreamWorldFrontFemale,
        homeFrontDefault = homeFrontDefault,
        homeFrontFemale = homeFrontFemale,
        homeFrontShiny = homeFrontShiny,
        homeFrontShinyFemale = homeFrontShinyFemale,
        showdownBackDefault = showdownBackDefault,
        showdownBackFemale = showdownBackFemale,
        showdownBackShiny = showdownBackShiny,
        showdownBackShinyFemale = showdownBackShinyFemale,
        showdownFrontDefault = showdownFrontDefault,
        showdownFrontFemale = showdownFrontFemale,
        showdownFrontShiny = showdownFrontShiny,
        showdownFrontShinyFemale = showdownFrontShinyFemale
    )
}

/**
 * Convierte un objeto PokemonSpritesDTO (capa de datos remota) en un objeto PokemonSpritesEmbedded (capa de datos local).
 *
 * @return Un objeto PokemonSpritesEmbedded con los datos mapeados desde el DTO.
 */
fun PokemonSpritesDTO.pokemonSpritesDTOToEmbedded(): PokemonSpritesEmbedded {
    return PokemonSpritesEmbedded(
        backDefault = backDefault,
        backFemale = backFemale,
        backShiny = backShiny,
        backShinyFemale = backShinyFemale,
        frontDefault = frontDefault,
        frontFemale = frontFemale,
        frontShiny = frontShiny,
        frontShinyFemale = frontShinyFemale,
        officialArtworkDefault = other?.officialArtwork?.frontDefault,
        officialArtworkShiny = other?.officialArtwork?.frontShiny,
        dreamWorldFrontDefault = other?.dreamWorld?.frontDefault,
        dreamWorldFrontFemale = other?.dreamWorld?.frontFemale,
        homeFrontDefault = other?.home?.frontDefault,
        homeFrontFemale = other?.home?.frontFemale,
        homeFrontShiny = other?.home?.frontShiny,
        homeFrontShinyFemale = other?.home?.frontShinyFemale,
        showdownBackDefault = other?.showdown?.backDefault,
        showdownBackFemale = other?.showdown?.backFemale,
        showdownBackShiny = other?.showdown?.backShiny,
        showdownBackShinyFemale = other?.showdown?.backShinyFemale,
        showdownFrontDefault = other?.showdown?.frontDefault,
        showdownFrontFemale = other?.showdown?.frontFemale,
        showdownFrontShiny = other?.showdown?.frontShiny,
        showdownFrontShinyFemale = other?.showdown?.frontShinyFemale
    )
}

