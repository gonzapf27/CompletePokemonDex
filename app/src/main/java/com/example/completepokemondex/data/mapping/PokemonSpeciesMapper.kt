package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.local.entities.PokemonSpeciesEntity
import com.example.completepokemondex.data.remote.models.PokemonSpeciesDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    val gson = Gson()
    
    return PokemonSpeciesEntity(
        id = id ?: 0,
        baseHappiness = baseHappiness,
        captureRate = captureRate,
        color = gson.toJson(color),
        eggGroups = gson.toJson(eggGroups),
        evolutionChain = gson.toJson(evolutionChain),
        evolvesFromSpecies = gson.toJson(evolvesFromSpecies),
        flavorTextEntries = gson.toJson(flavorTextEntries),
        formDescriptions = gson.toJson(formDescriptions),
        formsSwitchable = formsSwitchable,
        genderRate = genderRate,
        genera = gson.toJson(genera),
        generation = gson.toJson(generation),
        growthRate = gson.toJson(growthRate),
        habitat = gson.toJson(habitat),
        hasGenderDifferences = hasGenderDifferences,
        hatchCounter = hatchCounter,
        isBaby = isBaby,
        isLegendary = isLegendary,
        isMythical = isMythical,
        name = name,
        names = gson.toJson(names),
        order = order,
        palParkEncounters = gson.toJson(palParkEncounters),
        pokedexNumbers = gson.toJson(pokedexNumbers),
        shape = gson.toJson(shape),
        varieties = gson.toJson(varieties)
    )
}

/**
 * Convierte una entidad PokemonSpeciesEntity a un objeto de dominio PokemonSpeciesDomain.
 *
 * @return PokemonSpeciesDomain convertido desde PokemonSpeciesEntity.
 */
fun PokemonSpeciesEntity.pokemonSpeciesEntityToDomain(): PokemonSpeciesDomain {
    val gson = Gson()
    
    // Definir TypeTokens para cada campo JSON
    val colorType = object : TypeToken<PokemonSpeciesDTO.Color?>() {}.type
    val eggGroupsType = object : TypeToken<List<PokemonSpeciesDTO.EggGroup?>?>() {}.type
    val evolutionChainType = object : TypeToken<PokemonSpeciesDTO.EvolutionChain?>() {}.type
    val evolvesFromSpeciesType = object : TypeToken<PokemonSpeciesDTO.EvolvesFromSpecies?>() {}.type
    val flavorTextEntriesType = object : TypeToken<List<PokemonSpeciesDTO.FlavorTextEntry?>?>() {}.type
    val formDescriptionsType = object : TypeToken<List<Any?>?>() {}.type
    val generaType = object : TypeToken<List<PokemonSpeciesDTO.Genera?>?>() {}.type
    val generationType = object : TypeToken<PokemonSpeciesDTO.Generation?>() {}.type
    val growthRateType = object : TypeToken<PokemonSpeciesDTO.GrowthRate?>() {}.type
    val habitatType = object : TypeToken<PokemonSpeciesDTO.Habitat?>() {}.type
    val namesType = object : TypeToken<List<PokemonSpeciesDTO.Name?>?>() {}.type
    val palParkEncountersType = object : TypeToken<List<PokemonSpeciesDTO.PalParkEncounter?>?>() {}.type
    val pokedexNumbersType = object : TypeToken<List<PokemonSpeciesDTO.PokedexNumber?>?>() {}.type
    val shapeType = object : TypeToken<PokemonSpeciesDTO.Shape?>() {}.type
    val varietiesType = object : TypeToken<List<PokemonSpeciesDTO.Variety?>?>() {}.type
    
    // Deserializar los campos JSON
    val colorDTO: PokemonSpeciesDTO.Color? = gson.fromJson(color, colorType)
    val eggGroupsDTO: List<PokemonSpeciesDTO.EggGroup?>? = gson.fromJson(eggGroups, eggGroupsType)
    val evolutionChainDTO: PokemonSpeciesDTO.EvolutionChain? = gson.fromJson(evolutionChain, evolutionChainType)
    val evolvesFromSpeciesDTO: PokemonSpeciesDTO.EvolvesFromSpecies? = gson.fromJson(evolvesFromSpecies, evolvesFromSpeciesType)
    val flavorTextEntriesDTO: List<PokemonSpeciesDTO.FlavorTextEntry?>? = gson.fromJson(flavorTextEntries, flavorTextEntriesType)
    val formDescriptionsDTO: List<Any?>? = gson.fromJson(formDescriptions, formDescriptionsType)
    val generaDTO: List<PokemonSpeciesDTO.Genera?>? = gson.fromJson(genera, generaType)
    val generationDTO: PokemonSpeciesDTO.Generation? = gson.fromJson(generation, generationType)
    val growthRateDTO: PokemonSpeciesDTO.GrowthRate? = gson.fromJson(growthRate, growthRateType)
    val habitatDTO: PokemonSpeciesDTO.Habitat? = gson.fromJson(habitat, habitatType)
    val namesDTO: List<PokemonSpeciesDTO.Name?>? = gson.fromJson(names, namesType)
    val palParkEncountersDTO: List<PokemonSpeciesDTO.PalParkEncounter?>? = gson.fromJson(palParkEncounters, palParkEncountersType)
    val pokedexNumbersDTO: List<PokemonSpeciesDTO.PokedexNumber?>? = gson.fromJson(pokedexNumbers, pokedexNumbersType)
    val shapeDTO: PokemonSpeciesDTO.Shape? = gson.fromJson(shape, shapeType)
    val varietiesDTO: List<PokemonSpeciesDTO.Variety?>? = gson.fromJson(varieties, varietiesType)

    return PokemonSpeciesDomain(
        base_happiness = baseHappiness,
        capture_rate = captureRate,
        color = colorDTO?.let {
            PokemonSpeciesDomain.Color(
                name = it.name,
                url = it.url
            )
        },
        egg_groups = eggGroupsDTO?.map { eggGroupDTO ->
            eggGroupDTO?.let {
                PokemonSpeciesDomain.EggGroup(
                    name = it.name,
                    url = it.url
                )
            }
        },
        evolution_chain = evolutionChainDTO?.let {
            PokemonSpeciesDomain.EvolutionChain(
                url = it.url
            )
        },
        evolves_from_species = evolvesFromSpeciesDTO?.let {
            PokemonSpeciesDomain.EvolvesFromSpecies(
                name = it.name,
                url = it.url
            )
        },
        flavor_text_entries = flavorTextEntriesDTO?.map { entryDTO ->
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
        form_descriptions = formDescriptionsDTO,
        forms_switchable = formsSwitchable,
        gender_rate = genderRate,
        genera = generaDTO?.map { generaDTO ->
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
        generation = generationDTO?.let {
            PokemonSpeciesDomain.Generation(
                name = it.name,
                url = it.url
            )
        },
        growth_rate = growthRateDTO?.let {
            PokemonSpeciesDomain.GrowthRate(
                name = it.name,
                url = it.url
            )
        },
        habitat = habitatDTO?.let {
            PokemonSpeciesDomain.Habitat(
                name = it.name,
                url = it.url
            )
        },
        has_gender_differences = hasGenderDifferences,
        hatch_counter = hatchCounter,
        id = id,
        is_baby = isBaby,
        is_legendary = isLegendary,
        is_mythical = isMythical,
        name = name,
        names = namesDTO?.map { nameDTO ->
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
        pal_park_encounters = palParkEncountersDTO?.map { encounterDTO ->
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
        pokedex_numbers = pokedexNumbersDTO?.map { numberDTO ->
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
        shape = shapeDTO?.let {
            PokemonSpeciesDomain.Shape(
                name = it.name,
                url = it.url
            )
        },
        varieties = varietiesDTO?.map { varietyDTO ->
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
