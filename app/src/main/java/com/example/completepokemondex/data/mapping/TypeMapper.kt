package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.remote.models.TypeDTO
import com.example.completepokemondex.data.domain.model.TypeDomain
import com.example.completepokemondex.data.local.entities.TypeEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Mapper de DTO a Domain
fun TypeDTO.toDomain(): TypeDomain {
    return TypeDomain(
        damageRelations = TypeDomain.DamageRelations(
            doubleDamageFrom = damageRelations.doubleDamageFrom.map { TypeDomain.DamageRelations.DoubleDamageFrom(it.name, it.url) },
            doubleDamageTo = damageRelations.doubleDamageTo.map { TypeDomain.DamageRelations.DoubleDamageTo(it.name, it.url) },
            halfDamageFrom = damageRelations.halfDamageFrom.map { TypeDomain.DamageRelations.HalfDamageFrom(it.name, it.url) },
            halfDamageTo = damageRelations.halfDamageTo.map { TypeDomain.DamageRelations.HalfDamageTo(it.name, it.url) },
            noDamageFrom = damageRelations.noDamageFrom.map { TypeDomain.DamageRelations.NoDamageFrom(it.name, it.url) },
            noDamageTo = damageRelations.noDamageTo
        ),
        gameIndices = gameIndices.map {
            TypeDomain.GameIndice(
                gameIndex = it.gameIndex,
                generation = TypeDomain.GameIndice.Generation(
                    name = it.generation.name,
                    url = it.generation.url
                )
            )
        },
        generation = TypeDomain.Generation(generation.name, generation.url),
        id = id,
        moveDamageClass = TypeDomain.MoveDamageClass(moveDamageClass.name, moveDamageClass.url),
        moves = moves.map { TypeDomain.Move(it.name, it.url) },
        name = name,
        names = names.map {
            TypeDomain.Name(
                language = TypeDomain.Name.Language(it.language.name, it.language.url),
                name = it.name
            )
        },
        pastDamageRelations = pastDamageRelations,
        pokemon = pokemon.map {
            TypeDomain.Pokemon(
                pokemon = TypeDomain.Pokemon.Pokemon(it.pokemon.name, it.pokemon.url),
                slot = it.slot
            )
        },
        sprites = TypeDomain.Sprites(
            generationIii = with(sprites.generationIii) {
                TypeDomain.Sprites.GenerationIii(
                    colosseum = TypeDomain.Sprites.GenerationIii.Colosseum(colosseum.nameIcon),
                    emerald = TypeDomain.Sprites.GenerationIii.Emerald(emerald.nameIcon),
                    fireredLeafgreen = TypeDomain.Sprites.GenerationIii.FireredLeafgreen(fireredLeafgreen.nameIcon),
                    rubySaphire = TypeDomain.Sprites.GenerationIii.RubySaphire(rubySaphire.nameIcon),
                    xd = TypeDomain.Sprites.GenerationIii.Xd(xd.nameIcon)
                )
            },
            generationIv = with(sprites.generationIv) {
                TypeDomain.Sprites.GenerationIv(
                    diamondPearl = TypeDomain.Sprites.GenerationIv.DiamondPearl(diamondPearl.nameIcon),
                    heartgoldSoulsilver = TypeDomain.Sprites.GenerationIv.HeartgoldSoulsilver(heartgoldSoulsilver.nameIcon),
                    platinum = TypeDomain.Sprites.GenerationIv.Platinum(platinum.nameIcon)
                )
            },
            generationIx = with(sprites.generationIx) {
                TypeDomain.Sprites.GenerationIx(
                    scarletViolet = TypeDomain.Sprites.GenerationIx.ScarletViolet(scarletViolet.nameIcon)
                )
            },
            generationV = with(sprites.generationV) {
                TypeDomain.Sprites.GenerationV(
                    black2White2 = TypeDomain.Sprites.GenerationV.Black2White2(black2White2.nameIcon),
                    blackWhite = TypeDomain.Sprites.GenerationV.BlackWhite(blackWhite.nameIcon)
                )
            },
            generationVi = with(sprites.generationVi) {
                TypeDomain.Sprites.GenerationVi(
                    omegaRubyAlphaSapphire = TypeDomain.Sprites.GenerationVi.OmegaRubyAlphaSapphire(omegaRubyAlphaSapphire.nameIcon),
                    xY = TypeDomain.Sprites.GenerationVi.XY(xY.nameIcon)
                )
            },
            generationVii = with(sprites.generationVii) {
                TypeDomain.Sprites.GenerationVii(
                    letsGoPikachuLetsGoEevee = TypeDomain.Sprites.GenerationVii.LetsGoPikachuLetsGoEevee(letsGoPikachuLetsGoEevee.nameIcon),
                    sunMoon = TypeDomain.Sprites.GenerationVii.SunMoon(sunMoon.nameIcon),
                    ultraSunUltraMoon = TypeDomain.Sprites.GenerationVii.UltraSunUltraMoon(ultraSunUltraMoon.nameIcon)
                )
            },
            generationViii = with(sprites.generationViii) {
                TypeDomain.Sprites.GenerationViii(
                    brilliantDiamondAndShiningPearl = TypeDomain.Sprites.GenerationViii.BrilliantDiamondAndShiningPearl(brilliantDiamondAndShiningPearl.nameIcon),
                    legendsArceus = TypeDomain.Sprites.GenerationViii.LegendsArceus(legendsArceus.nameIcon),
                    swordShield = TypeDomain.Sprites.GenerationViii.SwordShield(swordShield.nameIcon)
                )
            }
        )
    )
}

// Mapper de DTO a Entity
fun TypeDTO.toEntity(): TypeEntity {
    val gson = Gson()
    return TypeEntity(
        id = id,
        damageRelations = gson.toJson(damageRelations),
        gameIndices = gson.toJson(gameIndices),
        generation = gson.toJson(generation),
        moveDamageClass = gson.toJson(moveDamageClass),
        moves = gson.toJson(moves),
        name = name,
        names = gson.toJson(names),
        pastDamageRelations = gson.toJson(pastDamageRelations),
        pokemon = gson.toJson(pokemon),
        sprites = gson.toJson(sprites)
    )
}

// Mapper de Entity a Domain
fun TypeEntity.toDomain(): TypeDomain {
    val gson = Gson()
    val damageRelationsType = object : TypeToken<TypeDTO.DamageRelations>() {}.type
    val gameIndicesType = object : TypeToken<List<TypeDTO.GameIndice>>() {}.type
    val generationType = object : TypeToken<TypeDTO.Generation>() {}.type
    val moveDamageClassType = object : TypeToken<TypeDTO.MoveDamageClass>() {}.type
    val movesType = object : TypeToken<List<TypeDTO.Move>>() {}.type
    val namesType = object : TypeToken<List<TypeDTO.Name>>() {}.type
    val pastDamageRelationsType = object : TypeToken<List<Any?>>() {}.type
    val pokemonType = object : TypeToken<List<TypeDTO.Pokemon>>() {}.type
    val spritesType = object : TypeToken<TypeDTO.Sprites>() {}.type

    val damageRelationsDTO: TypeDTO.DamageRelations = gson.fromJson(damageRelations, damageRelationsType)
    val gameIndicesDTO: List<TypeDTO.GameIndice> = gson.fromJson(gameIndices, gameIndicesType)
    val generationDTO: TypeDTO.Generation = gson.fromJson(generation, generationType)
    val moveDamageClassDTO: TypeDTO.MoveDamageClass = gson.fromJson(moveDamageClass, moveDamageClassType)
    val movesDTO: List<TypeDTO.Move> = gson.fromJson(moves, movesType)
    val namesDTO: List<TypeDTO.Name> = gson.fromJson(names, namesType)
    val pastDamageRelationsDTO: List<Any?> = gson.fromJson(pastDamageRelations, pastDamageRelationsType)
    val pokemonDTO: List<TypeDTO.Pokemon> = gson.fromJson(pokemon, pokemonType)
    val spritesDTO: TypeDTO.Sprites = gson.fromJson(sprites, spritesType)

    return TypeDTO(
        damageRelations = damageRelationsDTO,
        gameIndices = gameIndicesDTO,
        generation = generationDTO,
        id = id,
        moveDamageClass = moveDamageClassDTO,
        moves = movesDTO,
        name = name,
        names = namesDTO,
        pastDamageRelations = pastDamageRelationsDTO,
        pokemon = pokemonDTO,
        sprites = spritesDTO
    ).toDomain()
}
