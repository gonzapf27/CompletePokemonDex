package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.local.entities.PokemonSpeciesEntity
import com.example.completepokemondex.data.remote.models.PokemonSpeciesDTO

/**
 * Convierte un PokemonSpeciesDTO de la API a un objeto de dominio PokemonSpeciesDomain.
 *
 * @return PokemonSpeciesDomain convertido desde PokemonSpeciesDTO.
 */
fun PokemonSpeciesDTO.pokemonSpeciesDTOToDomain(): PokemonSpeciesDomain {
    return PokemonSpeciesDomain(
        base_happiness = baseHappiness,
        capture_rate = captureRate,
        color = color?.let { colorDTO ->
            PokemonSpeciesDomain.Color(
                name = colorDTO.name,
                url = colorDTO.url
            )
        },
        egg_groups = eggGroups?.map { eggGroupDTO ->
            eggGroupDTO?.let {
                PokemonSpeciesDomain.EggGroup(
                    name = it.name,
                    url = it.url
                )
            }
        },
        evolution_chain = evolutionChain?.let { chainDTO ->
            PokemonSpeciesDomain.EvolutionChain(
                url = chainDTO.url
            )
        },
        evolves_from_species = evolvesFromSpecies?.let { speciesDTO ->
            PokemonSpeciesDomain.EvolvesFromSpecies(
                name = speciesDTO.name,
                url = speciesDTO.url
            )
        },
        flavor_text_entries = flavorTextEntries?.map { entryDTO ->
            entryDTO?.let {
                PokemonSpeciesDomain.FlavorTextEntry(
                    flavor_text = it.flavorText,
                    language = it.language?.let { langDTO ->
                        PokemonSpeciesDomain.FlavorTextEntry.Language(
                            name = langDTO.name,
                            url = langDTO.url
                        )
                    },
                    version = it.version?.let { versionDTO ->
                        PokemonSpeciesDomain.FlavorTextEntry.Version(
                            name = versionDTO.name,
                            url = versionDTO.url
                        )
                    }
                )
            }
        },
        form_descriptions = formDescriptions,
        forms_switchable = formsSwitchable,
        gender_rate = genderRate,
        genera = genera?.map { generaDTO ->
            generaDTO?.let {
                PokemonSpeciesDomain.Genera(
                    genus = it.genus,
                    language = it.language?.let { langDTO ->
                        PokemonSpeciesDomain.Genera.Language(
                            name = langDTO.name,
                            url = langDTO.url
                        )
                    }
                )
            }
        },
        generation = generation?.let { genDTO ->
            PokemonSpeciesDomain.Generation(
                name = genDTO.name,
                url = genDTO.url
            )
        },
        growth_rate = growthRate?.let { growthDTO ->
            PokemonSpeciesDomain.GrowthRate(
                name = growthDTO.name,
                url = growthDTO.url
            )
        },
        habitat = habitat?.let { habitatDTO ->
            PokemonSpeciesDomain.Habitat(
                name = habitatDTO.name,
                url = habitatDTO.url
            )
        },
        has_gender_differences = hasGenderDifferences,
        hatch_counter = hatchCounter,
        id = id,
        is_baby = isBaby,
        is_legendary = isLegendary,
        is_mythical = isMythical,
        name = name,
        names = names?.map { nameDTO ->
            nameDTO?.let {
                PokemonSpeciesDomain.Name(
                    name = it.name,
                    language = it.language?.let { langDTO ->
                        PokemonSpeciesDomain.Name.Language(
                            name = langDTO.name,
                            url = langDTO.url
                        )
                    }
                )
            }
        },
        order = order,
        pal_park_encounters = palParkEncounters?.map { encounterDTO ->
            encounterDTO?.let {
                PokemonSpeciesDomain.PalParkEncounter(
                    area = it.area?.let { areaDTO ->
                        PokemonSpeciesDomain.PalParkEncounter.Area(
                            name = areaDTO.name,
                            url = areaDTO.url
                        )
                    },
                    base_score = it.baseScore,
                    rate = it.rate
                )
            }
        },
        pokedex_numbers = pokedexNumbers?.map { numberDTO ->
            numberDTO?.let {
                PokemonSpeciesDomain.PokedexNumber(
                    entry_number = it.entryNumber,
                    pokedex = it.pokedex?.let { pokedexDTO ->
                        PokemonSpeciesDomain.PokedexNumber.Pokedex(
                            name = pokedexDTO.name,
                            url = pokedexDTO.url
                        )
                    }
                )
            }
        },
        shape = shape?.let { shapeDTO ->
            PokemonSpeciesDomain.Shape(
                name = shapeDTO.name,
                url = shapeDTO.url
            )
        },
        varieties = varieties?.map { varietyDTO ->
            varietyDTO?.let {
                PokemonSpeciesDomain.Variety(
                    is_default = it.isDefault,
                    pokemon = it.pokemon?.let { pokemonDTO ->
                        PokemonSpeciesDomain.Variety.Pokemon(
                            name = pokemonDTO.name,
                            url = pokemonDTO.url
                        )
                    }
                )
            }
        }
    )
}

/**
 * Convierte un PokemonSpeciesDTO de la API a una entidad PokemonSpeciesEntity para la base de datos.
 *
 * @return PokemonSpeciesEntity convertida desde PokemonSpeciesDTO.
 */
fun PokemonSpeciesDTO.pokemonSpeciesDTOToEntity(): PokemonSpeciesEntity {
    return PokemonSpeciesEntity(
        id = id ?: 0,
        base_happiness = baseHappiness,
        capture_rate = captureRate,
        color = color?.let { PokemonSpeciesEntity.Color(it.name, it.url) },
        egg_groups = eggGroups?.map { it?.let { eg -> PokemonSpeciesEntity.EggGroup(eg.name, eg.url) } },
        evolution_chain = evolutionChain?.let { PokemonSpeciesEntity.EvolutionChain(it.url) },
        evolves_from_species = evolvesFromSpecies?.let { PokemonSpeciesEntity.EvolvesFromSpecies(it.name, it.url) },
        flavor_text_entries = flavorTextEntries?.map { entry ->
            entry?.let {
                PokemonSpeciesEntity.FlavorTextEntry(
                    flavor_text = it.flavorText,
                    language = it.language?.let { lang -> PokemonSpeciesEntity.FlavorTextEntry.Language(lang.name, lang.url) },
                    version = it.version?.let { ver -> PokemonSpeciesEntity.FlavorTextEntry.Version(ver.name, ver.url) }
                )
            }
        },
        form_descriptions = formDescriptions,
        forms_switchable = formsSwitchable,
        gender_rate = genderRate,
        genera = genera?.map { g ->
            g?.let {
                PokemonSpeciesEntity.Genera(
                    genus = it.genus,
                    language = it.language?.let { lang -> PokemonSpeciesEntity.Genera.Language(lang.name, lang.url) }
                )
            }
        },
        generation = generation?.let { PokemonSpeciesEntity.Generation(it.name, it.url) },
        growth_rate = growthRate?.let { PokemonSpeciesEntity.GrowthRate(it.name, it.url) },
        habitat = habitat?.let { PokemonSpeciesEntity.Habitat(it.name, it.url) },
        has_gender_differences = hasGenderDifferences,
        hatch_counter = hatchCounter,
        is_baby = isBaby,
        is_legendary = isLegendary,
        is_mythical = isMythical,
        name = name,
        names = names?.map { n ->
            n?.let {
                PokemonSpeciesEntity.Name(
                    name = it.name,
                    language = it.language?.let { lang -> PokemonSpeciesEntity.Name.Language(lang.name, lang.url) }
                )
            }
        },
        order = order,
        pal_park_encounters = palParkEncounters?.map { p ->
            p?.let {
                PokemonSpeciesEntity.PalParkEncounter(
                    area = it.area?.let { a -> PokemonSpeciesEntity.PalParkEncounter.Area(a.name, a.url) },
                    base_score = it.baseScore,
                    rate = it.rate
                )
            }
        },
        pokedex_numbers = pokedexNumbers?.map { pn ->
            pn?.let {
                PokemonSpeciesEntity.PokedexNumber(
                    entry_number = it.entryNumber,
                    pokedex = it.pokedex?.let { pdx -> PokemonSpeciesEntity.PokedexNumber.Pokedex(pdx.name, pdx.url) }
                )
            }
        },
        shape = shape?.let { PokemonSpeciesEntity.Shape(it.name, it.url) },
        varieties = varieties?.map { v ->
            v?.let {
                PokemonSpeciesEntity.Variety(
                    is_default = it.isDefault,
                    pokemon = it.pokemon?.let { pkm -> PokemonSpeciesEntity.Variety.Pokemon(pkm.name, pkm.url) }
                )
            }
        }
    )
}

/**
 * Convierte una entidad PokemonSpeciesEntity a un objeto de dominio PokemonSpeciesDomain.
 *
 * @return PokemonSpeciesDomain convertido desde PokemonSpeciesEntity.
 */
fun PokemonSpeciesEntity.pokemonSpeciesEntityToDomain(): PokemonSpeciesDomain {
    return PokemonSpeciesDomain(
        base_happiness = base_happiness,
        capture_rate = capture_rate,
        color = color?.let { PokemonSpeciesDomain.Color(it.name, it.url) },
        egg_groups = egg_groups?.map { it?.let { eg -> PokemonSpeciesDomain.EggGroup(eg.name, eg.url) } },
        evolution_chain = evolution_chain?.let { PokemonSpeciesDomain.EvolutionChain(it.url) },
        evolves_from_species = evolves_from_species?.let { PokemonSpeciesDomain.EvolvesFromSpecies(it.name, it.url) },
        flavor_text_entries = flavor_text_entries?.map { entry ->
            entry?.let {
                PokemonSpeciesDomain.FlavorTextEntry(
                    flavor_text = it.flavor_text,
                    language = it.language?.let { lang -> PokemonSpeciesDomain.FlavorTextEntry.Language(lang.name, lang.url) },
                    version = it.version?.let { ver -> PokemonSpeciesDomain.FlavorTextEntry.Version(ver.name, ver.url) }
                )
            }
        },
        form_descriptions = form_descriptions,
        forms_switchable = forms_switchable,
        gender_rate = gender_rate,
        genera = genera?.map { g ->
            g?.let {
                PokemonSpeciesDomain.Genera(
                    genus = it.genus,
                    language = it.language?.let { lang -> PokemonSpeciesDomain.Genera.Language(lang.name, lang.url) }
                )
            }
        },
        generation = generation?.let { PokemonSpeciesDomain.Generation(it.name, it.url) },
        growth_rate = growth_rate?.let { PokemonSpeciesDomain.GrowthRate(it.name, it.url) },
        habitat = habitat?.let { PokemonSpeciesDomain.Habitat(it.name, it.url) },
        has_gender_differences = has_gender_differences,
        hatch_counter = hatch_counter,
        id = id,
        is_baby = is_baby,
        is_legendary = is_legendary,
        is_mythical = is_mythical,
        name = name,
        names = names?.map { n ->
            n?.let {
                PokemonSpeciesDomain.Name(
                    name = it.name,
                    language = it.language?.let { lang -> PokemonSpeciesDomain.Name.Language(lang.name, lang.url) }
                )
            }
        },
        order = order,
        pal_park_encounters = pal_park_encounters?.map { p ->
            p?.let {
                PokemonSpeciesDomain.PalParkEncounter(
                    area = it.area?.let { a -> PokemonSpeciesDomain.PalParkEncounter.Area(a.name, a.url) },
                    base_score = it.base_score,
                    rate = it.rate
                )
            }
        },
        pokedex_numbers = pokedex_numbers?.map { pn ->
            pn?.let {
                PokemonSpeciesDomain.PokedexNumber(
                    entry_number = it.entry_number,
                    pokedex = it.pokedex?.let { pdx -> PokemonSpeciesDomain.PokedexNumber.Pokedex(pdx.name, pdx.url) }
                )
            }
        },
        shape = shape?.let { PokemonSpeciesDomain.Shape(it.name, it.url) },
        varieties = varieties?.map { v ->
            v?.let {
                PokemonSpeciesDomain.Variety(
                    is_default = it.is_default,
                    pokemon = it.pokemon?.let { pkm -> PokemonSpeciesDomain.Variety.Pokemon(pkm.name, pkm.url) }
                )
            }
        }
    )
}

/**
 * Función de extensión para obtener la descripción de un Pokémon en un idioma específico.
 * 
 * @param language El código del idioma deseado (por ejemplo, "es", "en").
 * @return La primera descripción encontrada en el idioma especificado o null si no hay ninguna.
 */
fun PokemonSpeciesDomain.getDescriptionByLanguage(language: String): String? {
    return this.flavor_text_entries
        ?.filterNotNull()
        ?.find { it.language?.name == language }
        ?.flavor_text
        ?.replace("\n", " ")
        ?.replace("\u000c", " ")
        ?.trim()
}

/**
 * Función de extensión para obtener el género del Pokémon como texto descriptivo.
 * 
 * @return Descripción del género según el valor de gender_rate.
 */
fun PokemonSpeciesDomain.getGenderRateDescription(): String {
    return when (this.gender_rate) {
        -1 -> "Sin género"
        0 -> "100% masculino"
        8 -> "100% femenino"
        else -> "${((8 - (this.gender_rate ?: 0)) * 12.5).toInt()}% masculino, ${(this.gender_rate?.times(12.5))?.toInt()}% femenino"
    }
}
