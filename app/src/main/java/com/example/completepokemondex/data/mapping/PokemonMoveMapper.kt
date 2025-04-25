package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.remote.models.PokemonMoveDTO
import com.example.completepokemondex.data.domain.model.PokemonMoveDomain
import com.example.completepokemondex.data.local.entities.PokemonMoveEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Mapper de DTO a Domain
fun PokemonMoveDTO.toDomain(): PokemonMoveDomain {
    return PokemonMoveDomain(
        accuracy = accuracy,
        contest_combos = contestCombos?.let {
            PokemonMoveDomain.ContestCombos(
                normal = it.normal?.let { normal ->
                    PokemonMoveDomain.ContestCombos.Normal(
                        use_after = normal.useAfter?.map { ua ->
                            ua?.let { PokemonMoveDomain.ContestCombos.Normal.UseAfter(name = it.name, url = it.url) }
                        },
                        use_before = normal.useBefore
                    )
                },
                `super` = it.superX?.let { superX ->
                    PokemonMoveDomain.ContestCombos.Super(
                        use_after = superX.useAfter,
                        use_before = superX.useBefore
                    )
                }
            )
        },
        contest_effect = contestEffect?.let { PokemonMoveDomain.ContestEffect(url = it.url) },
        contest_type = contestType?.let { PokemonMoveDomain.ContestType(name = it.name, url = it.url) },
        damage_class = damageClass?.let { PokemonMoveDomain.DamageClass(name = it.name, url = it.url) },
        effect_chance = effectChance,
        effect_changes = effectChanges,
        effect_entries = effectEntries?.map { entry ->
            entry?.let {
                PokemonMoveDomain.EffectEntry(
                    effect = it.effect,
                    language = it.language?.let { lang ->
                        PokemonMoveDomain.EffectEntry.Language(name = lang.name, url = lang.url)
                    },
                    short_effect = it.shortEffect
                )
            }
        },
        flavor_text_entries = flavorTextEntries?.map { entry ->
            entry?.let {
                PokemonMoveDomain.FlavorTextEntry(
                    flavor_text = it.flavorText,
                    language = it.language?.let { lang ->
                        PokemonMoveDomain.FlavorTextEntry.Language(name = lang.name, url = lang.url)
                    },
                    version_group = it.versionGroup?.let { vg ->
                        PokemonMoveDomain.FlavorTextEntry.VersionGroup(name = vg.name, url = vg.url)
                    }
                )
            }
        },
        generation = generation?.let { PokemonMoveDomain.Generation(name = it.name, url = it.url) },
        id = id,
        learned_by_pokemon = learnedByPokemon?.map { poke ->
            poke?.let { PokemonMoveDomain.LearnedByPokemon(name = it.name, url = it.url) }
        },
        machines = machines?.map { machine ->
            machine?.let {
                PokemonMoveDomain.Machine(
                    machine = it.machine?.let { m -> PokemonMoveDomain.Machine.Machine(url = m.url) },
                    version_group = it.versionGroup?.let { vg ->
                        PokemonMoveDomain.Machine.VersionGroup(name = vg.name, url = vg.url)
                    }
                )
            }
        },
        meta = meta?.let {
            PokemonMoveDomain.Meta(
                ailment = it.ailment?.let { a -> PokemonMoveDomain.Meta.Ailment(name = a.name, url = a.url) },
                ailment_chance = it.ailmentChance,
                category = it.category?.let { c -> PokemonMoveDomain.Meta.Category(name = c.name, url = c.url) },
                crit_rate = it.critRate,
                drain = it.drain,
                flinch_chance = it.flinchChance,
                healing = it.healing,
                max_hits = it.maxHits,
                max_turns = it.maxTurns,
                min_hits = it.minHits,
                min_turns = it.minTurns,
                stat_chance = it.statChance
            )
        },
        name = name,
        names = names?.map { n ->
            n?.let {
                PokemonMoveDomain.Name(
                    language = it.language?.let { lang ->
                        PokemonMoveDomain.Name.Language(name = lang.name, url = lang.url)
                    },
                    name = it.name
                )
            }
        },
        past_values = pastValues,
        power = power,
        pp = pp,
        priority = priority,
        stat_changes = statChanges,
        super_contest_effect = superContestEffect?.let { PokemonMoveDomain.SuperContestEffect(url = it.url) },
        target = target?.let { PokemonMoveDomain.Target(name = it.name, url = it.url) },
        type = type?.let { PokemonMoveDomain.Type(name = it.name, url = it.url) }
    )
}

// Mapper de DTO a Entity
fun PokemonMoveDTO.toEntity(): PokemonMoveEntity {
    val gson = Gson()
    return PokemonMoveEntity(
        id = id ?: 0,
        accuracy = accuracy,
        contestCombos = gson.toJson(contestCombos),
        contestEffect = gson.toJson(contestEffect),
        contestType = gson.toJson(contestType),
        damageClass = gson.toJson(damageClass),
        effectChance = gson.toJson(effectChance),
        effectChanges = gson.toJson(effectChanges),
        effectEntries = gson.toJson(effectEntries),
        flavorTextEntries = gson.toJson(flavorTextEntries),
        generation = gson.toJson(generation),
        learnedByPokemon = gson.toJson(learnedByPokemon),
        machines = gson.toJson(machines),
        meta = gson.toJson(meta),
        name = name,
        names = gson.toJson(names),
        pastValues = gson.toJson(pastValues),
        power = power,
        pp = pp,
        priority = priority,
        statChanges = gson.toJson(statChanges),
        superContestEffect = gson.toJson(superContestEffect),
        target = gson.toJson(target),
        type = gson.toJson(type)
    )
}

// Mapper de Entity a Domain
fun PokemonMoveEntity.toDomain(): PokemonMoveDomain {
    val gson = Gson()
    val contestCombosType = object : TypeToken<PokemonMoveDTO.ContestCombos?>() {}.type
    val contestEffectType = object : TypeToken<PokemonMoveDTO.ContestEffect?>() {}.type
    val contestTypeType = object : TypeToken<PokemonMoveDTO.ContestType?>() {}.type
    val damageClassType = object : TypeToken<PokemonMoveDTO.DamageClass?>() {}.type
    val effectChanceType = object : TypeToken<Any?>() {}.type
    val effectChangesType = object : TypeToken<List<Any?>?>() {}.type
    val effectEntriesType = object : TypeToken<List<PokemonMoveDTO.EffectEntry?>?>() {}.type
    val flavorTextEntriesType = object : TypeToken<List<PokemonMoveDTO.FlavorTextEntry?>?>() {}.type
    val generationType = object : TypeToken<PokemonMoveDTO.Generation?>() {}.type
    val learnedByPokemonType = object : TypeToken<List<PokemonMoveDTO.LearnedByPokemon?>?>() {}.type
    val machinesType = object : TypeToken<List<PokemonMoveDTO.Machine?>?>() {}.type
    val metaType = object : TypeToken<PokemonMoveDTO.Meta?>() {}.type
    val namesType = object : TypeToken<List<PokemonMoveDTO.Name?>?>() {}.type
    val pastValuesType = object : TypeToken<List<Any?>?>() {}.type
    val statChangesType = object : TypeToken<List<Any?>?>() {}.type
    val superContestEffectType = object : TypeToken<PokemonMoveDTO.SuperContestEffect?>() {}.type
    val targetType = object : TypeToken<PokemonMoveDTO.Target?>() {}.type
    val typeType = object : TypeToken<PokemonMoveDTO.Type?>() {}.type

    val contestCombosDTO: PokemonMoveDTO.ContestCombos? = gson.fromJson(contestCombos, contestCombosType)
    val contestEffectDTO: PokemonMoveDTO.ContestEffect? = gson.fromJson(contestEffect, contestEffectType)
    val contestTypeDTO: PokemonMoveDTO.ContestType? = gson.fromJson(contestType, contestTypeType)
    val damageClassDTO: PokemonMoveDTO.DamageClass? = gson.fromJson(damageClass, damageClassType)
    val effectChanceDTO: Any? = gson.fromJson(effectChance, effectChanceType)
    val effectChangesDTO: List<Any?>? = gson.fromJson(effectChanges, effectChangesType)
    val effectEntriesDTO: List<PokemonMoveDTO.EffectEntry?>? = gson.fromJson(effectEntries, effectEntriesType)
    val flavorTextEntriesDTO: List<PokemonMoveDTO.FlavorTextEntry?>? = gson.fromJson(flavorTextEntries, flavorTextEntriesType)
    val generationDTO: PokemonMoveDTO.Generation? = gson.fromJson(generation, generationType)
    val learnedByPokemonDTO: List<PokemonMoveDTO.LearnedByPokemon?>? = gson.fromJson(learnedByPokemon, learnedByPokemonType)
    val machinesDTO: List<PokemonMoveDTO.Machine?>? = gson.fromJson(machines, machinesType)
    val metaDTO: PokemonMoveDTO.Meta? = gson.fromJson(meta, metaType)
    val namesDTO: List<PokemonMoveDTO.Name?>? = gson.fromJson(names, namesType)
    val pastValuesDTO: List<Any?>? = gson.fromJson(pastValues, pastValuesType)
    val statChangesDTO: List<Any?>? = gson.fromJson(statChanges, statChangesType)
    val superContestEffectDTO: PokemonMoveDTO.SuperContestEffect? = gson.fromJson(superContestEffect, superContestEffectType)
    val targetDTO: PokemonMoveDTO.Target? = gson.fromJson(target, targetType)
    val typeDTO: PokemonMoveDTO.Type? = gson.fromJson(type, typeType)

    return PokemonMoveDomain(
        accuracy = accuracy,
        contest_combos = contestCombosDTO?.let {
            PokemonMoveDomain.ContestCombos(
                normal = it.normal?.let { normal ->
                    PokemonMoveDomain.ContestCombos.Normal(
                        use_after = normal.useAfter?.map { ua ->
                            ua?.let { PokemonMoveDomain.ContestCombos.Normal.UseAfter(name = it.name, url = it.url) }
                        },
                        use_before = normal.useBefore
                    )
                },
                `super` = it.superX?.let { superX ->
                    PokemonMoveDomain.ContestCombos.Super(
                        use_after = superX.useAfter,
                        use_before = superX.useBefore
                    )
                }
            )
        },
        contest_effect = contestEffectDTO?.let { PokemonMoveDomain.ContestEffect(url = it.url) },
        contest_type = contestTypeDTO?.let { PokemonMoveDomain.ContestType(name = it.name, url = it.url) },
        damage_class = damageClassDTO?.let { PokemonMoveDomain.DamageClass(name = it.name, url = it.url) },
        effect_chance = effectChanceDTO,
        effect_changes = effectChangesDTO,
        effect_entries = effectEntriesDTO?.map { entry ->
            entry?.let {
                PokemonMoveDomain.EffectEntry(
                    effect = it.effect,
                    language = it.language?.let { lang ->
                        PokemonMoveDomain.EffectEntry.Language(name = lang.name, url = lang.url)
                    },
                    short_effect = it.shortEffect
                )
            }
        },
        flavor_text_entries = flavorTextEntriesDTO?.map { entry ->
            entry?.let {
                PokemonMoveDomain.FlavorTextEntry(
                    flavor_text = it.flavorText,
                    language = it.language?.let { lang ->
                        PokemonMoveDomain.FlavorTextEntry.Language(name = lang.name, url = lang.url)
                    },
                    version_group = it.versionGroup?.let { vg ->
                        PokemonMoveDomain.FlavorTextEntry.VersionGroup(name = vg.name, url = vg.url)
                    }
                )
            }
        },
        generation = generationDTO?.let { PokemonMoveDomain.Generation(name = it.name, url = it.url) },
        id = id,
        learned_by_pokemon = learnedByPokemonDTO?.map { poke ->
            poke?.let { PokemonMoveDomain.LearnedByPokemon(name = it.name, url = it.url) }
        },
        machines = machinesDTO?.map { machine ->
            machine?.let {
                PokemonMoveDomain.Machine(
                    machine = it.machine?.let { m -> PokemonMoveDomain.Machine.Machine(url = m.url) },
                    version_group = it.versionGroup?.let { vg ->
                        PokemonMoveDomain.Machine.VersionGroup(name = vg.name, url = vg.url)
                    }
                )
            }
        },
        meta = metaDTO?.let {
            PokemonMoveDomain.Meta(
                ailment = it.ailment?.let { a -> PokemonMoveDomain.Meta.Ailment(name = a.name, url = a.url) },
                ailment_chance = it.ailmentChance,
                category = it.category?.let { c -> PokemonMoveDomain.Meta.Category(name = c.name, url = c.url) },
                crit_rate = it.critRate,
                drain = it.drain,
                flinch_chance = it.flinchChance,
                healing = it.healing,
                max_hits = it.maxHits,
                max_turns = it.maxTurns,
                min_hits = it.minHits,
                min_turns = it.minTurns,
                stat_chance = it.statChance
            )
        },
        name = name,
        names = namesDTO?.map { n ->
            n?.let {
                PokemonMoveDomain.Name(
                    language = it.language?.let { lang ->
                        PokemonMoveDomain.Name.Language(name = lang.name, url = lang.url)
                    },
                    name = it.name
                )
            }
        },
        past_values = pastValuesDTO,
        power = power,
        pp = pp,
        priority = priority,
        stat_changes = statChangesDTO,
        super_contest_effect = superContestEffectDTO?.let { PokemonMoveDomain.SuperContestEffect(url = it.url) },
        target = targetDTO?.let { PokemonMoveDomain.Target(name = it.name, url = it.url) },
        type = typeDTO?.let { PokemonMoveDomain.Type(name = it.name, url = it.url) }
    )
}
