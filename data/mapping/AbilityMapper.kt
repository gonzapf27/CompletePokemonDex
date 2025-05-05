package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.remote.models.AbilityDTO
import com.example.completepokemondex.data.domain.model.AbilityDomain
import com.example.completepokemondex.data.local.entities.AbilityEntity

class AbilityMapper {
}

// Mapper de DTO a Domain
// ...existing code...

// Mapper de DTO a Entity
fun AbilityDTO.toEntity(): AbilityEntity {
    return AbilityEntity(
        id = id ?: 0,
        effectChanges = effectChanges?.map { effectChangeDTO ->
            effectChangeDTO?.let {
                AbilityEntity.EffectChangeEntity(
                    effectEntries = it.effectEntries?.map { entryDTO ->
                        entryDTO?.let { entry ->
                            AbilityEntity.EffectChangeEntity.EffectEntryEntity(
                                effect = entry.effect,
                                language = entry.language?.let { lang ->
                                    AbilityEntity.EffectChangeEntity.EffectEntryEntity.LanguageEntity(
                                        name = lang.name,
                                        url = lang.url
                                    )
                                }
                            )
                        }
                    },
                    versionGroup = it.versionGroup?.let { vg ->
                        AbilityEntity.EffectChangeEntity.VersionGroupEntity(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        effectEntries = effectEntries?.map { entryDTO ->
            entryDTO?.let {
                AbilityEntity.EffectEntryEntity(
                    effect = it.effect,
                    language = it.language?.let { lang ->
                        AbilityEntity.EffectEntryEntity.LanguageEntity(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    shortEffect = it.shortEffect
                )
            }
        },
        flavorTextEntries = flavorTextEntries?.map { entryDTO ->
            entryDTO?.let {
                AbilityEntity.FlavorTextEntryEntity(
                    flavorText = it.flavorText,
                    language = it.language?.let { lang ->
                        AbilityEntity.FlavorTextEntryEntity.LanguageEntity(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    versionGroup = it.versionGroup?.let { vg ->
                        AbilityEntity.FlavorTextEntryEntity.VersionGroupEntity(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        generation = generation?.let {
            AbilityEntity.GenerationEntity(
                name = it.name,
                url = it.url
            )
        },
        isMainSeries = isMainSeries,
        name = name,
        names = names?.map { nameDTO ->
            nameDTO?.let {
                AbilityEntity.NameEntity(
                    language = it.language?.let { lang ->
                        AbilityEntity.NameEntity.LanguageEntity(
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
                AbilityEntity.PokemonEntity(
                    isHidden = it.isHidden,
                    pokemon = it.pokemon?.let { poke ->
                        AbilityEntity.PokemonEntity.PokemonDataEntity(
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

// Mapper de Entity a Domain
fun AbilityEntity.toDomain(): AbilityDomain {
    return AbilityDomain(
        effect_changes = effectChanges?.map { effectChangeEntity ->
            effectChangeEntity?.let {
                AbilityDomain.EffectChange(
                    effect_entries = it.effectEntries?.map { entryEntity ->
                        entryEntity?.let { entry ->
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
        effect_entries = effectEntries?.map { entryEntity ->
            entryEntity?.let {
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
        flavor_text_entries = flavorTextEntries?.map { entryEntity ->
            entryEntity?.let {
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
        names = names?.map { nameEntity ->
            nameEntity?.let {
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
        pokemon = pokemon?.map { pokeEntity ->
            pokeEntity?.let {
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
