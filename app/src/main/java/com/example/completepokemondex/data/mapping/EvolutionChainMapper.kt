package com.example.completepokemondex.data.mapping

import com.example.completepokemondex.data.domain.model.EvolutionChainDomain
import com.example.completepokemondex.data.local.entities.EvolutionChainEntity
import com.example.completepokemondex.data.remote.models.EvolutionChainDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Mapper de DTO a Domain
fun EvolutionChainDTO.toDomain(): EvolutionChainDomain {
    return EvolutionChainDomain(
        baby_trigger_item = babyTriggerItem,
        chain = chain?.toDomain(),
        id = id
    )
}

private fun EvolutionChainDTO.Chain.toDomain(): EvolutionChainDomain.Chain {
    return EvolutionChainDomain.Chain(
        evolution_details = evolutionDetails,
        evolves_to = evolvesTo?.map { it?.toDomain() },
        is_baby = isBaby,
        species = species?.toDomain()
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.toDomain(): EvolutionChainDomain.Chain.EvolvesTo {
    return EvolutionChainDomain.Chain.EvolvesTo(
        evolution_details = evolutionDetails?.map { it?.toDomain() },
        evolves_to = evolvesTo?.map { it?.toDomain() },
        is_baby = isBaby,
        species = species?.toDomain()
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.EvolutionDetail.toDomain(): EvolutionChainDomain.Chain.EvolvesTo.EvolutionDetail {
    return EvolutionChainDomain.Chain.EvolvesTo.EvolutionDetail(
        gender = gender,
        held_item = heldItem,
        item = item,
        known_move = knownMove,
        known_move_type = knownMoveType,
        location = location,
        min_affection = minAffection,
        min_beauty = minBeauty,
        min_happiness = minHappiness,
        min_level = minLevel,
        needs_overworld_rain = needsOverworldRain,
        party_species = partySpecies,
        party_type = partyType,
        relative_physical_stats = relativePhysicalStats,
        time_of_day = timeOfDay,
        trade_species = tradeSpecies,
        trigger = trigger?.toDomain(),
        turn_upside_down = turnUpsideDown
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.EvolutionDetail.Trigger.toDomain(): EvolutionChainDomain.Chain.EvolvesTo.EvolutionDetail.Trigger {
    return EvolutionChainDomain.Chain.EvolvesTo.EvolutionDetail.Trigger(
        name = name,
        url = url
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.EvolvesTo.toDomain(): EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo {
    return EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo(
        evolution_details = evolutionDetails?.map { it?.toDomain() },
        evolves_to = evolvesTo,
        is_baby = isBaby,
        species = species?.toDomain()
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.EvolvesTo.EvolutionDetail.toDomain(): EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo.EvolutionDetail {
    return EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo.EvolutionDetail(
        gender = gender,
        held_item = heldItem,
        item = item,
        known_move = knownMove,
        known_move_type = knownMoveType,
        location = location,
        min_affection = minAffection,
        min_beauty = minBeauty,
        min_happiness = minHappiness,
        min_level = minLevel,
        needs_overworld_rain = needsOverworldRain,
        party_species = partySpecies,
        party_type = partyType,
        relative_physical_stats = relativePhysicalStats,
        time_of_day = timeOfDay,
        trade_species = tradeSpecies,
        trigger = trigger?.toDomain(),
        turn_upside_down = turnUpsideDown
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.EvolvesTo.EvolutionDetail.Trigger.toDomain(): EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo.EvolutionDetail.Trigger {
    return EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo.EvolutionDetail.Trigger(
        name = name,
        url = url
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.EvolvesTo.Species.toDomain(): EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo.Species {
    return EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo.Species(
        name = name,
        url = url
    )
}

private fun EvolutionChainDTO.Chain.EvolvesTo.Species.toDomain(): EvolutionChainDomain.Chain.EvolvesTo.Species {
    return EvolutionChainDomain.Chain.EvolvesTo.Species(
        name = name,
        url = url
    )
}

private fun EvolutionChainDTO.Chain.Species.toDomain(): EvolutionChainDomain.Chain.Species {
    return EvolutionChainDomain.Chain.Species(
        name = name,
        url = url
    )
}

// Mapper de DTO a Entity
fun EvolutionChainDTO.toEntity(): EvolutionChainEntity {
    val gson = Gson()
    return EvolutionChainEntity(
        id = id ?: 0,
        babyTriggerItem = gson.toJson(babyTriggerItem),
        chain = gson.toJson(chain)
    )
}

// Mapper de Entity a Domain
fun EvolutionChainEntity.toDomain(): EvolutionChainDomain {
    val gson = Gson()
    val babyTriggerItemType = object : TypeToken<Any?>() {}.type
    val chainType = object : TypeToken<EvolutionChainDTO.Chain?>() {}.type

    val babyTriggerItemDTO: Any? = gson.fromJson(babyTriggerItem, babyTriggerItemType)
    val chainDTO: EvolutionChainDTO.Chain? = gson.fromJson(chain, chainType)

    return EvolutionChainDomain(
        baby_trigger_item = babyTriggerItemDTO,
        chain = chainDTO?.toDomain(),
        id = id
    )
}
