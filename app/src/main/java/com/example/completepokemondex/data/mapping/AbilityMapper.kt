package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.remote.models.AbilityDTO
import com.example.completepokemondex.data.domain.model.AbilityDomain
import com.example.completepokemondex.data.local.entities.AbilityEntity



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
    return AbilityEntity(
        id = id ?: 0,
        effect_changes = effectChanges?.map { effectChangeDTO ->
            effectChangeDTO?.let {
                AbilityEntity.EffectChange(
                    effect_entries = it.effectEntries?.map { entryDTO ->
                        entryDTO?.let { entry ->
                            AbilityEntity.EffectChange.EffectEntry(
                                effect = entry.effect,
                                language = entry.language?.let { lang ->
                                    AbilityEntity.EffectChange.EffectEntry.Language(
                                        name = lang.name,
                                        url = lang.url
                                    )
                                }
                            )
                        }
                    },
                    version_group = it.versionGroup?.let { vg ->
                        AbilityEntity.EffectChange.VersionGroup(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        effect_entries = effectEntries?.map { entryDTO ->
            entryDTO?.let {
                AbilityEntity.EffectEntry(
                    effect = it.effect,
                    language = it.language?.let { lang ->
                        AbilityEntity.EffectEntry.Language(
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
                AbilityEntity.FlavorTextEntry(
                    flavor_text = it.flavorText,
                    language = it.language?.let { lang ->
                        AbilityEntity.FlavorTextEntry.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    version_group = it.versionGroup?.let { vg ->
                        AbilityEntity.FlavorTextEntry.VersionGroup(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        generation = generation?.let {
            AbilityEntity.Generation(
                name = it.name,
                url = it.url
            )
        },
        is_main_series = isMainSeries,
        name = name,
        names = names?.map { nameDTO ->
            nameDTO?.let {
                AbilityEntity.Name(
                    language = it.language?.let { lang ->
                        AbilityEntity.Name.Language(
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
                AbilityEntity.Pokemon(
                    is_hidden = it.isHidden,
                    pokemon = it.pokemon?.let { poke ->
                        AbilityEntity.Pokemon.Pokemon(
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
        effect_changes = effect_changes?.map { effectChangeEntity ->
            effectChangeEntity?.let {
                AbilityDomain.EffectChange(
                    effect_entries = it.effect_entries?.map { entryEntity ->
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
                    version_group = it.version_group?.let { vg ->
                        AbilityDomain.EffectChange.VersionGroup(
                            name = vg.name,
                            url = vg.url
                        )
                    }
                )
            }
        },
        effect_entries = effect_entries?.map { entryEntity ->
            entryEntity?.let {
                AbilityDomain.EffectEntry(
                    effect = it.effect,
                    language = it.language?.let { lang ->
                        AbilityDomain.EffectEntry.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    short_effect = it.short_effect
                )
            }
        },
        flavor_text_entries = flavor_text_entries?.map { entryEntity ->
            entryEntity?.let {
                AbilityDomain.FlavorTextEntry(
                    flavor_text = it.flavor_text,
                    language = it.language?.let { lang ->
                        AbilityDomain.FlavorTextEntry.Language(
                            name = lang.name,
                            url = lang.url
                        )
                    },
                    version_group = it.version_group?.let { vg ->
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
        is_main_series = is_main_series,
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
                    is_hidden = it.is_hidden,
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
