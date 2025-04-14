package com.example.completepokemondex.data

import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity
import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

fun PokemonDetailsDTO.toDomain(): PokemonDetailsDomain {
    return PokemonDetailsDomain(
        abilities = abilities.map { abilityDTO ->
            PokemonDetailsDomain.Ability(
                ability = PokemonDetailsDomain.Ability.Ability(
                    name = abilityDTO.ability.name,
                    url = abilityDTO.ability.url
                ),
                is_hidden = abilityDTO.isHidden,
                slot = abilityDTO.slot
            )
        },
        base_experience = baseExperience,
        cries = PokemonDetailsDomain.Cries(
            latest = cries.latest,
            legacy = cries.legacy
        ),
        forms = forms.map { formDTO ->
            PokemonDetailsDomain.Form(
                name = formDTO.name,
                url = formDTO.url
            )
        },
        game_indices = gameIndices.map { gameIndexDTO ->
            PokemonDetailsDomain.GameIndice(
                game_index = gameIndexDTO.gameIndex,
                version = PokemonDetailsDomain.GameIndice.Version(
                    name = gameIndexDTO.version.name,
                    url = gameIndexDTO.version.url
                )
            )
        },
        height = height,
        held_items = heldItems.map { heldItemDTO ->
            PokemonDetailsDomain.HeldItem(
                item = PokemonDetailsDomain.HeldItem.Item(
                    name = heldItemDTO.item.name,
                    url = heldItemDTO.item.url
                ),
                version_details = heldItemDTO.versionDetails.map { versionDetailDTO ->
                    PokemonDetailsDomain.HeldItem.VersionDetail(
                        rarity = versionDetailDTO.rarity,
                        version = PokemonDetailsDomain.HeldItem.VersionDetail.Version(
                            name = versionDetailDTO.version.name,
                            url = versionDetailDTO.version.url
                        )
                    )
                }
            )
        },
        id = id,
        is_default = isDefault,
        location_area_encounters = locationAreaEncounters,
        moves = moves.map { moveDTO ->
            PokemonDetailsDomain.Move(
                move = PokemonDetailsDomain.Move.Move(
                    name = moveDTO.move.name,
                    url = moveDTO.move.url
                ),
                version_group_details = moveDTO.versionGroupDetails.map { versionGroupDetailDTO ->
                    PokemonDetailsDomain.Move.VersionGroupDetail(
                        level_learned_at = versionGroupDetailDTO.levelLearnedAt,
                        move_learn_method = PokemonDetailsDomain.Move.VersionGroupDetail.MoveLearnMethod(
                            name = versionGroupDetailDTO.moveLearnMethod.name,
                            url = versionGroupDetailDTO.moveLearnMethod.url
                        ),
                        order = versionGroupDetailDTO.order,
                        version_group = PokemonDetailsDomain.Move.VersionGroupDetail.VersionGroup(
                            name = versionGroupDetailDTO.versionGroup.name,
                            url = versionGroupDetailDTO.versionGroup.url
                        )
                    )
                }
            )
        },
        name = name,
        order = order,
        past_abilities = pastAbilities.map { pastAbilityDTO ->
            PokemonDetailsDomain.PastAbility(
                abilities = pastAbilityDTO.abilities.map { abilityDTO ->
                    PokemonDetailsDomain.PastAbility.Ability(
                        ability = abilityDTO.ability,
                        is_hidden = abilityDTO.isHidden,
                        slot = abilityDTO.slot
                    )
                },
                generation = PokemonDetailsDomain.PastAbility.Generation(
                    name = pastAbilityDTO.generation.name,
                    url = pastAbilityDTO.generation.url
                )
            )
        },
        past_types = pastTypes,
        species = PokemonDetailsDomain.Species(
            name = species.name,
            url = species.url
        ),
        sprites = PokemonDetailsDomain.Sprites(
            back_default = sprites.backDefault,
            back_female = sprites.backFemale,
            back_shiny = sprites.backShiny,
            back_shiny_female = sprites.backShinyFemale,
            front_default = sprites.frontDefault,
            front_female = sprites.frontFemale,
            front_shiny = sprites.frontShiny,
            front_shiny_female = sprites.frontShinyFemale,
            other = PokemonDetailsDomain.Sprites.Other(
                dream_world = PokemonDetailsDomain.Sprites.Other.DreamWorld(
                    front_default = sprites.other.dreamWorld.frontDefault,
                    front_female = sprites.other.dreamWorld.frontFemale
                ),
                home = PokemonDetailsDomain.Sprites.Other.Home(
                    front_default = sprites.other.home.frontDefault,
                    front_female = sprites.other.home.frontFemale,
                    front_shiny = sprites.other.home.frontShiny,
                    front_shiny_female = sprites.other.home.frontShinyFemale
                ),
                `official-artwork` = PokemonDetailsDomain.Sprites.Other.OfficialArtwork(
                    front_default = sprites.other.officialArtwork.frontDefault,
                    front_shiny = sprites.other.officialArtwork.frontShiny
                ),
                showdown = PokemonDetailsDomain.Sprites.Other.Showdown(
                    back_default = sprites.other.showdown.backDefault,
                    back_female = sprites.other.showdown.backFemale,
                    back_shiny = sprites.other.showdown.backShiny,
                    back_shiny_female = sprites.other.showdown.backShinyFemale,
                    front_default = sprites.other.showdown.frontDefault,
                    front_female = sprites.other.showdown.frontFemale,
                    front_shiny = sprites.other.showdown.frontShiny,
                    front_shiny_female = sprites.other.showdown.frontShinyFemale
                )
            ),
            versions = PokemonDetailsDomain.Sprites.Versions(
                `generation-i` = sprites.versions.generationI.toDomain(),
                `generation-ii` = sprites.versions.generationIi.toDomain(),
                `generation-iii` = sprites.versions.generationIii.toDomain(),
                `generation-iv` = sprites.versions.generationIv.toDomain(),
                `generation-v` = sprites.versions.generationV.toDomain(),
                `generation-vi` = sprites.versions.generationVi.toDomain(),
                `generation-vii` = sprites.versions.generationVii.toDomain(),
                `generation-viii` = sprites.versions.generationViii.toDomain()
            )
        ),
        stats = stats.map { statDTO ->
            PokemonDetailsDomain.Stat(
                base_stat = statDTO.baseStat,
                effort = statDTO.effort,
                stat = PokemonDetailsDomain.Stat.Stat(
                    name = statDTO.stat.name,
                    url = statDTO.stat.url
                )
            )
        },
        types = types.map { typeDTO ->
            PokemonDetailsDomain.Type(
                slot = typeDTO.slot,
                type = PokemonDetailsDomain.Type.Type(
                    name = typeDTO.type.name,
                    url = typeDTO.type.url
                )
            )
        },
        weight = weight
    )
}

/**
 * Alias para el método toDomain() para mantener la compatibilidad con el código existente
 */
fun PokemonDetailsDTO.pokemonDetailsDTOToDomain(): PokemonDetailsDomain = this.toDomain()

private fun PokemonDetailsDTO.Sprites.Versions.GenerationI.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationI(
        `red-blue` = PokemonDetailsDomain.Sprites.Versions.GenerationI.RedBlue(
            back_default = redBlue.backDefault,
            back_gray = redBlue.backGray,
            back_transparent = redBlue.backTransparent,
            front_default = redBlue.frontDefault,
            front_gray = redBlue.frontGray,
            front_transparent = redBlue.frontTransparent
        ),
        yellow = PokemonDetailsDomain.Sprites.Versions.GenerationI.Yellow(
            back_default = yellow.backDefault,
            back_gray = yellow.backGray,
            back_transparent = yellow.backTransparent,
            front_default = yellow.frontDefault,
            front_gray = yellow.frontGray,
            front_transparent = yellow.frontTransparent
        )
    )

private fun PokemonDetailsDTO.Sprites.Versions.GenerationIi.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationIi(
        crystal = PokemonDetailsDomain.Sprites.Versions.GenerationIi.Crystal(
            back_default = crystal.backDefault,
            back_shiny = crystal.backShiny,
            back_shiny_transparent = crystal.backShinyTransparent,
            back_transparent = crystal.backTransparent,
            front_default = crystal.frontDefault,
            front_shiny = crystal.frontShiny,
            front_shiny_transparent = crystal.frontShinyTransparent,
            front_transparent = crystal.frontTransparent
        ),
        gold = PokemonDetailsDomain.Sprites.Versions.GenerationIi.Gold(
            back_default = gold.backDefault,
            back_shiny = gold.backShiny,
            front_default = gold.frontDefault,
            front_shiny = gold.frontShiny,
            front_transparent = gold.frontTransparent
        ),
        silver = PokemonDetailsDomain.Sprites.Versions.GenerationIi.Silver(
            back_default = silver.backDefault,
            back_shiny = silver.backShiny,
            front_default = silver.frontDefault,
            front_shiny = silver.frontShiny,
            front_transparent = silver.frontTransparent
        )
    )

private fun PokemonDetailsDTO.Sprites.Versions.GenerationIii.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationIii(
        emerald = PokemonDetailsDomain.Sprites.Versions.GenerationIii.Emerald(
            front_default = emerald.frontDefault,
            front_shiny = emerald.frontShiny
        ),
        `firered-leafgreen` = PokemonDetailsDomain.Sprites.Versions.GenerationIii.FireredLeafgreen(
            back_default = fireredLeafgreen.backDefault,
            back_shiny = fireredLeafgreen.backShiny,
            front_default = fireredLeafgreen.frontDefault,
            front_shiny = fireredLeafgreen.frontShiny
        ),
        `ruby-sapphire` = PokemonDetailsDomain.Sprites.Versions.GenerationIii.RubySapphire(
            back_default = rubySapphire.backDefault,
            back_shiny = rubySapphire.backShiny,
            front_default = rubySapphire.frontDefault,
            front_shiny = rubySapphire.frontShiny
        )
    )

private fun PokemonDetailsDTO.Sprites.Versions.GenerationIv.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationIv(
        `diamond-pearl` = PokemonDetailsDomain.Sprites.Versions.GenerationIv.DiamondPearl(
            back_default = diamondPearl.backDefault,
            back_female = diamondPearl.backFemale,
            back_shiny = diamondPearl.backShiny,
            back_shiny_female = diamondPearl.backShinyFemale,
            front_default = diamondPearl.frontDefault,
            front_female = diamondPearl.frontFemale,
            front_shiny = diamondPearl.frontShiny,
            front_shiny_female = diamondPearl.frontShinyFemale
        ),
        `heartgold-soulsilver` = PokemonDetailsDomain.Sprites.Versions.GenerationIv.HeartgoldSoulsilver(
            back_default = heartgoldSoulsilver.backDefault,
            back_female = heartgoldSoulsilver.backFemale,
            back_shiny = heartgoldSoulsilver.backShiny,
            back_shiny_female = heartgoldSoulsilver.backShinyFemale,
            front_default = heartgoldSoulsilver.frontDefault,
            front_female = heartgoldSoulsilver.frontFemale,
            front_shiny = heartgoldSoulsilver.frontShiny,
            front_shiny_female = heartgoldSoulsilver.frontShinyFemale
        ),
        platinum = PokemonDetailsDomain.Sprites.Versions.GenerationIv.Platinum(
            back_default = platinum.backDefault,
            back_female = platinum.backFemale,
            back_shiny = platinum.backShiny,
            back_shiny_female = platinum.backShinyFemale,
            front_default = platinum.frontDefault,
            front_female = platinum.frontFemale,
            front_shiny = platinum.frontShiny,
            front_shiny_female = platinum.frontShinyFemale
        )
    )

private fun PokemonDetailsDTO.Sprites.Versions.GenerationV.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationV(
        `black-white` = PokemonDetailsDomain.Sprites.Versions.GenerationV.BlackWhite(
            animated = PokemonDetailsDomain.Sprites.Versions.GenerationV.BlackWhite.Animated(
                back_default = blackWhite.animated.backDefault,
                back_female = blackWhite.animated.backFemale,
                back_shiny = blackWhite.animated.backShiny,
                back_shiny_female = blackWhite.animated.backShinyFemale,
                front_default = blackWhite.animated.frontDefault,
                front_female = blackWhite.animated.frontFemale,
                front_shiny = blackWhite.animated.frontShiny,
                front_shiny_female = blackWhite.animated.frontShinyFemale
            ),
            back_default = blackWhite.backDefault,
            back_female = blackWhite.backFemale,
            back_shiny = blackWhite.backShiny,
            back_shiny_female = blackWhite.backShinyFemale,
            front_default = blackWhite.frontDefault,
            front_female = blackWhite.frontFemale,
            front_shiny = blackWhite.frontShiny,
            front_shiny_female = blackWhite.frontShinyFemale
        )
    )

private fun PokemonDetailsDTO.Sprites.Versions.GenerationVi.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationVi(
        `omegaruby-alphasapphire` = PokemonDetailsDomain.Sprites.Versions.GenerationVi.OmegarubyAlphasapphire(
            front_default = omegarubyAlphasapphire.frontDefault,
            front_female = omegarubyAlphasapphire.frontFemale,
            front_shiny = omegarubyAlphasapphire.frontShiny,
            front_shiny_female = omegarubyAlphasapphire.frontShinyFemale
        ),
        `x-y` = PokemonDetailsDomain.Sprites.Versions.GenerationVi.XY(
            front_default = xY.frontDefault,
            front_female = xY.frontFemale,
            front_shiny = xY.frontShiny,
            front_shiny_female = xY.frontShinyFemale
        )
    )

private fun PokemonDetailsDTO.Sprites.Versions.GenerationVii.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationVii(
        icons = PokemonDetailsDomain.Sprites.Versions.GenerationVii.Icons(
            front_default = icons.frontDefault,
            front_female = icons.frontFemale
        ),
        `ultra-sun-ultra-moon` = PokemonDetailsDomain.Sprites.Versions.GenerationVii.UltraSunUltraMoon(
            front_default = ultraSunUltraMoon.frontDefault,
            front_female = ultraSunUltraMoon.frontFemale,
            front_shiny = ultraSunUltraMoon.frontShiny,
            front_shiny_female = ultraSunUltraMoon.frontShinyFemale
        )
    )

private fun PokemonDetailsDTO.Sprites.Versions.GenerationViii.toDomain() =
    PokemonDetailsDomain.Sprites.Versions.GenerationViii(
        icons = PokemonDetailsDomain.Sprites.Versions.GenerationViii.Icons(
            front_default = icons.frontDefault,
            front_female = icons.frontFemale
        )
    )

/**
 * Convierte un PokemonDetailsEntity de la base de datos a un PokemonDetailsDomain
 */
fun PokemonDetailsEntity.pokemonDetailsEntityToDomain(): PokemonDetailsDomain {
    val gson = Gson()
    
    // Deserializar los campos JSON
    val abilitiesType = object : TypeToken<List<PokemonDetailsDomain.Ability>>() {}.type
    val criesType = object : TypeToken<PokemonDetailsDomain.Cries>() {}.type
    val formsType = object : TypeToken<List<PokemonDetailsDomain.Form>>() {}.type
    val gameIndicesType = object : TypeToken<List<PokemonDetailsDomain.GameIndice>>() {}.type
    val heldItemsType = object : TypeToken<List<PokemonDetailsDomain.HeldItem>>() {}.type
    val movesType = object : TypeToken<List<PokemonDetailsDomain.Move>>() {}.type
    val pastAbilitiesType = object : TypeToken<List<PokemonDetailsDomain.PastAbility>>() {}.type
    val pastTypesType = object : TypeToken<List<Any?>>() {}.type
    val speciesType = object : TypeToken<PokemonDetailsDomain.Species>() {}.type
    val spritesType = object : TypeToken<PokemonDetailsDomain.Sprites>() {}.type
    val statsType = object : TypeToken<List<PokemonDetailsDomain.Stat>>() {}.type
    val typesType = object : TypeToken<List<PokemonDetailsDomain.Type>>() {}.type
    
    return PokemonDetailsDomain(
        abilities = gson.fromJson(abilities, abilitiesType),
        base_experience = baseExperience,
        cries = gson.fromJson(cries, criesType),
        forms = gson.fromJson(forms, formsType),
        game_indices = gson.fromJson(gameIndices, gameIndicesType),
        height = height,
        held_items = gson.fromJson(heldItems, heldItemsType),
        id = id,
        is_default = isDefault,
        location_area_encounters = locationAreaEncounters,
        moves = gson.fromJson(moves, movesType),
        name = name,
        order = order,
        past_abilities = gson.fromJson(pastAbilities, pastAbilitiesType),
        past_types = gson.fromJson(pastTypes, pastTypesType),
        species = gson.fromJson(species, speciesType),
        sprites = gson.fromJson(sprites, spritesType),
        stats = gson.fromJson(stats, statsType),
        types = gson.fromJson(types, typesType),
        weight = weight
    )
}

/**
 * Convierte un PokemonDetailsDTO de la API a un PokemonDetailsEntity para almacenar en la base de datos
 */
fun PokemonDetailsDTO.pokemonDetailsDTOToEntity(): PokemonDetailsEntity {
    val gson = Gson()
    
    return PokemonDetailsEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        isDefault = isDefault,
        locationAreaEncounters = locationAreaEncounters,
        order = order,
        abilities = gson.toJson(abilities),
        cries = gson.toJson(cries),
        forms = gson.toJson(forms),
        gameIndices = gson.toJson(gameIndices),
        heldItems = gson.toJson(heldItems),
        moves = gson.toJson(moves),
        pastAbilities = gson.toJson(pastAbilities),
        pastTypes = gson.toJson(pastTypes),
        species = gson.toJson(species),
        sprites = gson.toJson(sprites),
        stats = gson.toJson(stats),
        types = gson.toJson(types)
    )
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







