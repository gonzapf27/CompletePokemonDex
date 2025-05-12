package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.remote.models.AbilityDTO
import com.example.completepokemondex.data.domain.model.AbilityDomain
import com.example.completepokemondex.data.local.entities.AbilityEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



// Mapper de DTO a Domain
fun AbilityDTO.toDomain(): AbilityDomain {
    return AbilityDomain(
        effect_changes = effectChanges?.map { effectChangeDTO ->
            effectChangeDTO?.let {
                AbilityDomain.EffectChange(
                    effect_entries = it.effectEntries?.map { entryDTO ->
                        entryDTO?.let { entry ->
                            AbilityDomain.EffectChange.EffectEntry(
                                effect = entry.effect,
                                language = entry.language?.let { lang ->
                                    AbilityDomain.EffectChange.EffectEntry.Language(
                                        name = lang.name,
                                        url = lang.url
                                    )
                                }
                            )
                        }
                    },
                    version_group = it.versionGroup?.let { vg ->
                        AbilityDomain.EffectChange.VersionGroup(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        effect_entries = effectEntries?.map { entryDTO ->
            entryDTO?.let {
                AbilityDomain.EffectEntry(
                    effect = it.effect,
                    language = it.language?.let { lang ->
                        AbilityDomain.EffectEntry.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    short_effect = it.shortEffect
                )
            }
        },
        flavor_text_entries = flavorTextEntries?.map { entryDTO ->
            entryDTO?.let {
                AbilityDomain.FlavorTextEntry(
                    flavor_text = it.flavorText,
                    language = it.language?.let { lang ->
                        AbilityDomain.FlavorTextEntry.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    version_group = it.versionGroup?.let { vg ->
                        AbilityDomain.FlavorTextEntry.VersionGroup(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        generation = generation?.let {
            AbilityDomain.Generation(
                name = it.name,
                url = it.url
            )
        },
        id = id,
        is_main_series = isMainSeries,
        name = name,
        names = names?.map { nameDTO ->
            nameDTO?.let {
                AbilityDomain.Name(
                    language = it.language?.let { lang ->
                        AbilityDomain.Name.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    name = it.name
                )
            }
        },
        pokemon = pokemon?.map { pokeDTO ->
            pokeDTO?.let {
                AbilityDomain.Pokemon(
                    is_hidden = it.isHidden,
                    pokemon = it.pokemon?.let { poke ->
                        AbilityDomain.Pokemon.Pokemon(
                            name = poke.name,
                            url = poke.url
                        )
                    },
                    slot = it.slot
                )
            }
        }
    )
}

// Mapper de DTO a Entity
fun AbilityDTO.toEntity(): AbilityEntity {
    val gson = Gson()
    return AbilityEntity(
        id = id ?: 0,
        effectChanges = gson.toJson(effectChanges),
        effectEntries = gson.toJson(effectEntries),
        flavorTextEntries = gson.toJson(flavorTextEntries),
        generation = gson.toJson(generation),
        isMainSeries = isMainSeries,
        name = name,
        names = gson.toJson(names),
        pokemon = gson.toJson(pokemon)
    )
}

// Mapper de Entity a Domain
fun AbilityEntity.toDomain(): AbilityDomain {
    val gson = Gson()
    val effectChangesType = object : TypeToken<List<AbilityDTO.EffectChange?>?>() {}.type
    val effectEntriesType = object : TypeToken<List<AbilityDTO.EffectEntry?>?>() {}.type
    val flavorTextEntriesType = object : TypeToken<List<AbilityDTO.FlavorTextEntry?>?>() {}.type
    val generationType = object : TypeToken<AbilityDTO.Generation?>() {}.type
    val namesType = object : TypeToken<List<AbilityDTO.Name?>?>() {}.type
    val pokemonType = object : TypeToken<List<AbilityDTO.Pokemon?>?>() {}.type

    val effectChangesDTO: List<AbilityDTO.EffectChange?>? = gson.fromJson(effectChanges, effectChangesType)
    val effectEntriesDTO: List<AbilityDTO.EffectEntry?>? = gson.fromJson(effectEntries, effectEntriesType)
    val flavorTextEntriesDTO: List<AbilityDTO.FlavorTextEntry?>? = gson.fromJson(flavorTextEntries, flavorTextEntriesType)
    val generationDTO: AbilityDTO.Generation? = gson.fromJson(generation, generationType)
    val namesDTO: List<AbilityDTO.Name?>? = gson.fromJson(names, namesType)
    val pokemonDTO: List<AbilityDTO.Pokemon?>? = gson.fromJson(pokemon, pokemonType)

    return AbilityDomain(
        effect_changes = effectChangesDTO?.map { effectChangeDTO ->
            effectChangeDTO?.let {
                AbilityDomain.EffectChange(
                    effect_entries = it.effectEntries?.map { entryDTO ->
                        entryDTO?.let { entry ->
                            AbilityDomain.EffectChange.EffectEntry(
                                effect = entry.effect,
                                language = entry.language?.let { lang ->
                                    AbilityDomain.EffectChange.EffectEntry.Language(
                                        name = lang.name,
                                        url = lang.url
                                    )
                                }
                            )
                        }
                    },
                    version_group = it.versionGroup?.let { vg ->
                        AbilityDomain.EffectChange.VersionGroup(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        effect_entries = effectEntriesDTO?.map { entryDTO ->
            entryDTO?.let {
                AbilityDomain.EffectEntry(
                    effect = it.effect,
                    language = it.language?.let { lang ->
                        AbilityDomain.EffectEntry.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    short_effect = it.shortEffect
                )
            }
        },
        flavor_text_entries = flavorTextEntriesDTO?.map { entryDTO ->
            entryDTO?.let {
                AbilityDomain.FlavorTextEntry(
                    flavor_text = it.flavorText,
                    language = it.language?.let { lang ->
                        AbilityDomain.FlavorTextEntry.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    version_group = it.versionGroup?.let { vg ->
                        AbilityDomain.FlavorTextEntry.VersionGroup(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        generation = generationDTO?.let {
            AbilityDomain.Generation(
                name = it.name,
                url = it.url
            )
        },
        id = id,
        is_main_series = isMainSeries,
        name = name,
        names = namesDTO?.map { nameDTO ->
            nameDTO?.let {
                AbilityDomain.Name(
                    language = it.language?.let { lang ->
                        AbilityDomain.Name.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    name = it.name
                )
            }
        },
        pokemon = pokemonDTO?.map { pokeDTO ->
            pokeDTO?.let {
                AbilityDomain.Pokemon(
                    is_hidden = it.isHidden,
                    pokemon = it.pokemon?.let { poke ->
                        AbilityDomain.Pokemon.Pokemon(
                            name = poke.name,
                            url = poke.url
                        )
                    },
                    slot = it.slot
                )
            }
        }
    )
}