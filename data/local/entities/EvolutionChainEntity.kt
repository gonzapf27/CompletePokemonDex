package com.example.completepokemondex.data.local.entities

import androidx.room.*
import com.example.completepokemondex.data.local.entities.converters.EvolutionChainConverters

@Entity(tableName = "evolution_chain_table")
@TypeConverters(EvolutionChainConverters::class)
data class EvolutionChainEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "baby_trigger_item")
    val babyTriggerItem: Any?, // Si necesitas una clase específica, cámbiala aquí

    @Embedded(prefix = "chain_")
    val chain: ChainEntity?
) {
    data class ChainEntity(
        val isBaby: Boolean?,
        @Embedded(prefix = "species_")
        val species: SpeciesEntity?,
        @ColumnInfo(name = "evolution_details")
        val evolutionDetails: List<Any?>?, // Si necesitas una clase específica, cámbiala aquí
        @ColumnInfo(name = "evolves_to")
        val evolvesTo: List<EvolvesToEntity?>?
    ) {
        data class EvolvesToEntity(
            @ColumnInfo(name = "evolution_details")
            val evolutionDetails: List<EvolutionDetailEntity?>?,
            @ColumnInfo(name = "evolves_to")
            val evolvesTo: List<EvolvesToEntity?>?,
            val isBaby: Boolean?,
            @Embedded(prefix = "species_")
            val species: SpeciesEntity?
        ) {
            data class EvolutionDetailEntity(
                val gender: Any?,
                @ColumnInfo(name = "held_item")
                val heldItem: Any?,
                val item: Any?,
                @ColumnInfo(name = "known_move")
                val knownMove: Any?,
                @ColumnInfo(name = "known_move_type")
                val knownMoveType: Any?,
                val location: Any?,
                @ColumnInfo(name = "min_affection")
                val minAffection: Any?,
                @ColumnInfo(name = "min_beauty")
                val minBeauty: Any?,
                @ColumnInfo(name = "min_happiness")
                val minHappiness: Any?,
                @ColumnInfo(name = "min_level")
                val minLevel: Int?,
                @ColumnInfo(name = "needs_overworld_rain")
                val needsOverworldRain: Boolean?,
                @ColumnInfo(name = "party_species")
                val partySpecies: Any?,
                @ColumnInfo(name = "party_type")
                val partyType: Any?,
                @ColumnInfo(name = "relative_physical_stats")
                val relativePhysicalStats: Any?,
                @ColumnInfo(name = "time_of_day")
                val timeOfDay: String?,
                @ColumnInfo(name = "trade_species")
                val tradeSpecies: Any?,
                @Embedded(prefix = "trigger_")
                val trigger: TriggerEntity?,
                @ColumnInfo(name = "turn_upside_down")
                val turnUpsideDown: Boolean?
            ) {
                data class TriggerEntity(
                    val name: String?,
                    val url: String?
                )
            }

            data class SpeciesEntity(
                val name: String?,
                val url: String?
            )
        }

        data class SpeciesEntity(
            val name: String?,
            val url: String?
        )
    }
}
