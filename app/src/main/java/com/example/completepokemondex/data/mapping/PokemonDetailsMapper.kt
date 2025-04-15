package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun PokemonDetailsDTO.pokemonDetailsDTOToDomain(): PokemonDetailsDomain {
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
        sprites = sprites.toDomain(),
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

// --- SPRITES MAPPING ULTRA SIMPLIFICADO ---

private fun PokemonDetailsDTO.Sprites.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites(
        backDefault, backFemale, backShiny, backShinyFemale,
        frontDefault, frontFemale, frontShiny, frontShinyFemale,
        other.toDomain(), versions.toDomain()
    )
}

private fun PokemonDetailsDTO.Sprites.Other.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Other(
        dreamWorld.let { PokemonDetailsDomain.Sprites.Other.DreamWorld(it.frontDefault, it.frontFemale) },
        home.let { PokemonDetailsDomain.Sprites.Other.Home(it.frontDefault, it.frontFemale, it.frontShiny, it.frontShinyFemale) },
        officialArtwork.let { PokemonDetailsDomain.Sprites.Other.OfficialArtwork(it.frontDefault, it.frontShiny) },
        showdown.let { PokemonDetailsDomain.Sprites.Other.Showdown(it.backDefault, it.backFemale, it.backShiny, it.backShinyFemale, it.frontDefault, it.frontFemale, it.frontShiny, it.frontShinyFemale) }
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions(
        generationI.toDomain(),
        generationIi.toDomain(),
        generationIii.toDomain(),
        generationIv.toDomain(),
        generationV.toDomain(),
        generationVi.toDomain(),
        generationVii.toDomain(),
        generationViii.toDomain()
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationI.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationI(
        PokemonDetailsDomain.Sprites.Versions.GenerationI.RedBlue(
            redBlue.backDefault, redBlue.backGray, redBlue.backTransparent,
            redBlue.frontDefault, redBlue.frontGray, redBlue.frontTransparent
        ),
        PokemonDetailsDomain.Sprites.Versions.GenerationI.Yellow(
            yellow.backDefault, yellow.backGray, yellow.backTransparent,
            yellow.frontDefault, yellow.frontGray, yellow.frontTransparent
        )
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationIi.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationIi(
        PokemonDetailsDomain.Sprites.Versions.GenerationIi.Crystal(
            crystal.backDefault, crystal.backShiny, crystal.backShinyTransparent, crystal.backTransparent,
            crystal.frontDefault, crystal.frontShiny, crystal.frontShinyTransparent, crystal.frontTransparent
        ),
        PokemonDetailsDomain.Sprites.Versions.GenerationIi.Gold(
            gold.backDefault, gold.backShiny, gold.frontDefault, gold.frontShiny, gold.frontTransparent
        ),
        PokemonDetailsDomain.Sprites.Versions.GenerationIi.Silver(
            silver.backDefault, silver.backShiny, silver.frontDefault, silver.frontShiny, silver.frontTransparent
        )
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationIii.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationIii(
        PokemonDetailsDomain.Sprites.Versions.GenerationIii.Emerald(emerald.frontDefault, emerald.frontShiny),
        PokemonDetailsDomain.Sprites.Versions.GenerationIii.FireredLeafgreen(
            fireredLeafgreen.backDefault, fireredLeafgreen.backShiny, fireredLeafgreen.frontDefault, fireredLeafgreen.frontShiny
        ),
        PokemonDetailsDomain.Sprites.Versions.GenerationIii.RubySapphire(
            rubySapphire.backDefault, rubySapphire.backShiny, rubySapphire.frontDefault, rubySapphire.frontShiny
        )
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationIv.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationIv(
        PokemonDetailsDomain.Sprites.Versions.GenerationIv.DiamondPearl(
            diamondPearl.backDefault, diamondPearl.backFemale, diamondPearl.backShiny, diamondPearl.backShinyFemale,
            diamondPearl.frontDefault, diamondPearl.frontFemale, diamondPearl.frontShiny, diamondPearl.frontShinyFemale
        ),
        PokemonDetailsDomain.Sprites.Versions.GenerationIv.HeartgoldSoulsilver(
            heartgoldSoulsilver.backDefault, heartgoldSoulsilver.backFemale, heartgoldSoulsilver.backShiny, heartgoldSoulsilver.backShinyFemale,
            heartgoldSoulsilver.frontDefault, heartgoldSoulsilver.frontFemale, heartgoldSoulsilver.frontShiny, heartgoldSoulsilver.frontShinyFemale
        ),
        PokemonDetailsDomain.Sprites.Versions.GenerationIv.Platinum(
            platinum.backDefault, platinum.backFemale, platinum.backShiny, platinum.backShinyFemale,
            platinum.frontDefault, platinum.frontFemale, platinum.frontShiny, platinum.frontShinyFemale
        )
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationV.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationV(
        PokemonDetailsDomain.Sprites.Versions.GenerationV.BlackWhite(
            PokemonDetailsDomain.Sprites.Versions.GenerationV.BlackWhite.Animated(
                blackWhite.animated.backDefault, blackWhite.animated.backFemale, blackWhite.animated.backShiny, blackWhite.animated.backShinyFemale,
                blackWhite.animated.frontDefault, blackWhite.animated.frontFemale, blackWhite.animated.frontShiny, blackWhite.animated.frontShinyFemale
            ),
            blackWhite.backDefault, blackWhite.backFemale, blackWhite.backShiny, blackWhite.backShinyFemale,
            blackWhite.frontDefault, blackWhite.frontFemale, blackWhite.frontShiny, blackWhite.frontShinyFemale
        )
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationVi.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationVi(
        PokemonDetailsDomain.Sprites.Versions.GenerationVi.OmegarubyAlphasapphire(
            omegarubyAlphasapphire.frontDefault, omegarubyAlphasapphire.frontFemale, omegarubyAlphasapphire.frontShiny, omegarubyAlphasapphire.frontShinyFemale
        ),
        PokemonDetailsDomain.Sprites.Versions.GenerationVi.XY(
            xY.frontDefault, xY.frontFemale, xY.frontShiny, xY.frontShinyFemale
        )
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationVii.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationVii(
        PokemonDetailsDomain.Sprites.Versions.GenerationVii.Icons(icons.frontDefault, icons.frontFemale),
        PokemonDetailsDomain.Sprites.Versions.GenerationVii.UltraSunUltraMoon(
            ultraSunUltraMoon.frontDefault, ultraSunUltraMoon.frontFemale, ultraSunUltraMoon.frontShiny, ultraSunUltraMoon.frontShinyFemale
        )
    )
}

private fun PokemonDetailsDTO.Sprites.Versions.GenerationViii.toDomain() = with(this) {
    PokemonDetailsDomain.Sprites.Versions.GenerationViii(
        PokemonDetailsDomain.Sprites.Versions.GenerationViii.Icons(icons.frontDefault, icons.frontFemale)
    )
}

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







