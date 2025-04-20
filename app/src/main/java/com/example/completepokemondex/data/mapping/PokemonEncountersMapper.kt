package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.domain.model.PokemonEncountersDomain
import com.example.completepokemondex.data.local.entities.PokemonEncountersEntity
import com.example.completepokemondex.data.remote.models.PokemonEncountersDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Convierte una lista de PokemonEncountersDTO a un objeto de dominio PokemonEncountersDomain.
 *
 * @return PokemonEncountersDomain convertido desde una lista de PokemonEncountersDTO.
 */
fun List<PokemonEncountersDTO>.toDomain(): PokemonEncountersDomain {
    return PokemonEncountersDomain(
        items = this.map { dto ->
            PokemonEncountersDomain.LocationAreaEncounter(
                location_area = dto.locationArea?.let { locationAreaDTO ->
                    PokemonEncountersDomain.LocationAreaEncounter.LocationArea(
                        name = locationAreaDTO.name,
                        url = locationAreaDTO.url
                    )
                },
                version_details = dto.versionDetails?.map { versionDetailDTO ->
                    versionDetailDTO?.let {
                        PokemonEncountersDomain.LocationAreaEncounter.VersionDetail(
                            encounter_details = it.encounterDetails?.map { encounterDetailDTO ->
                                encounterDetailDTO?.let { encDetail ->
                                    PokemonEncountersDomain.LocationAreaEncounter.VersionDetail.EncounterDetail(
                                        chance = encDetail.chance,
                                        condition_values = encDetail.conditionValues?.map { conditionValueDTO ->
                                            conditionValueDTO?.let { condValue ->
                                                PokemonEncountersDomain.LocationAreaEncounter.VersionDetail.EncounterDetail.ConditionValue(
                                                    name = condValue.name,
                                                    url = condValue.url
                                                )
                                            }
                                        },
                                        max_level = encDetail.maxLevel,
                                        method = encDetail.method?.let { methodDTO ->
                                            PokemonEncountersDomain.LocationAreaEncounter.VersionDetail.EncounterDetail.Method(
                                                name = methodDTO.name,
                                                url = methodDTO.url
                                            )
                                        },
                                        min_level = encDetail.minLevel
                                    )
                                }
                            },
                            max_chance = it.maxChance,
                            version = it.version?.let { versionDTO ->
                                PokemonEncountersDomain.LocationAreaEncounter.VersionDetail.Version(
                                    name = versionDTO.name,
                                    url = versionDTO.url
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}

/**
 * Convierte una lista de PokemonEncountersDTO a una entidad PokemonEncountersEntity para la base de datos.
 *
 * @param pokemonId El ID del Pokémon al que pertenecen estos encuentros.
 * @return PokemonEncountersEntity convertida desde una lista de PokemonEncountersDTO.
 */
fun List<PokemonEncountersDTO>.toEntity(pokemonId: Int): PokemonEncountersEntity {
    val gson = Gson()
    return PokemonEncountersEntity(
        pokemonId = pokemonId,
        encounters = gson.toJson(this)
    )
}

/**
 * Convierte una entidad PokemonEncountersEntity a un objeto de dominio PokemonEncountersDomain.
 *
 * @return PokemonEncountersDomain convertido desde PokemonEncountersEntity.
 */
fun PokemonEncountersEntity.toDomain(): PokemonEncountersDomain {
    val gson = Gson()
    val encountersType = object : TypeToken<List<PokemonEncountersDTO>>() {}.type
    val encountersDTO: List<PokemonEncountersDTO> = gson.fromJson(encounters, encountersType)
    
    return encountersDTO.toDomain()
}
