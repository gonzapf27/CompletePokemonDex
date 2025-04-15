package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_species_table")
data class PokemonSpeciesEntity(
    @PrimaryKey
    @ColumnInfo(name = "species_id")
    val id: Int,

    @ColumnInfo(name = "base_happiness")
    val baseHappiness: Int?,

    @ColumnInfo(name = "capture_rate")
    val captureRate: Int?,

    @ColumnInfo(name = "color")
    val color: String?, // JSON serializado

    @ColumnInfo(name = "egg_groups")
    val eggGroups: String?, // JSON serializado

    @ColumnInfo(name = "evolution_chain")
    val evolutionChain: String?, // JSON serializado

    @ColumnInfo(name = "evolves_from_species")
    val evolvesFromSpecies: String?, // JSON serializado

    @ColumnInfo(name = "flavor_text_entries")
    val flavorTextEntries: String?, // JSON serializado

    @ColumnInfo(name = "form_descriptions")
    val formDescriptions: String?, // JSON serializado

    @ColumnInfo(name = "forms_switchable")
    val formsSwitchable: Boolean?,

    @ColumnInfo(name = "gender_rate")
    val genderRate: Int?,

    @ColumnInfo(name = "genera")
    val genera: String?, // JSON serializado

    @ColumnInfo(name = "generation")
    val generation: String?, // JSON serializado

    @ColumnInfo(name = "growth_rate")
    val growthRate: String?, // JSON serializado

    @ColumnInfo(name = "habitat")
    val habitat: String?, // JSON serializado

    @ColumnInfo(name = "has_gender_differences")
    val hasGenderDifferences: Boolean?,

    @ColumnInfo(name = "hatch_counter")
    val hatchCounter: Int?,

    @ColumnInfo(name = "is_baby")
    val isBaby: Boolean?,

    @ColumnInfo(name = "is_legendary")
    val isLegendary: Boolean?,

    @ColumnInfo(name = "is_mythical")
    val isMythical: Boolean?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "names")
    val names: String?, // JSON serializado

    @ColumnInfo(name = "order")
    val order: Int?,

    @ColumnInfo(name = "pal_park_encounters")
    val palParkEncounters: String?, // JSON serializado

    @ColumnInfo(name = "pokedex_numbers")
    val pokedexNumbers: String?, // JSON serializado

    @ColumnInfo(name = "shape")
    val shape: String?, // JSON serializado

    @ColumnInfo(name = "varieties")
    val varieties: String? // JSON serializado
)