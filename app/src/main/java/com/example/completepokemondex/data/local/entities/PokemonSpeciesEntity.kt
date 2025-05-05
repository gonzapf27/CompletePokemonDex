package com.example.completepokemondex.data.local.entities

import androidx.room.*
import com.example.completepokemondex.data.local.util.PokemonSpeciesConverters

@TypeConverters(PokemonSpeciesConverters::class)
@Entity(tableName = "pokemon_species_table")
data class PokemonSpeciesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "base_happiness")
    val base_happiness: Int?,

    @ColumnInfo(name = "capture_rate")
    val capture_rate: Int?,

    @ColumnInfo(name = "color")
    val color: Color?,

    @ColumnInfo(name = "egg_groups")
    val egg_groups: List<EggGroup?>?,

    @ColumnInfo(name = "evolution_chain")
    val evolution_chain: EvolutionChain?,

    @ColumnInfo(name = "evolves_from_species")
    val evolves_from_species: EvolvesFromSpecies?,

    @ColumnInfo(name = "flavor_text_entries")
    val flavor_text_entries: List<FlavorTextEntry?>?,

    @ColumnInfo(name = "form_descriptions")
    val form_descriptions: List<Any?>?,

    @ColumnInfo(name = "forms_switchable")
    val forms_switchable: Boolean?,

    @ColumnInfo(name = "gender_rate")
    val gender_rate: Int?,

    @ColumnInfo(name = "genera")
    val genera: List<Genera?>?,

    @ColumnInfo(name = "generation")
    val generation: Generation?,

    @ColumnInfo(name = "growth_rate")
    val growth_rate: GrowthRate?,

    @ColumnInfo(name = "habitat")
    val habitat: Habitat?,

    @ColumnInfo(name = "has_gender_differences")
    val has_gender_differences: Boolean?,

    @ColumnInfo(name = "hatch_counter")
    val hatch_counter: Int?,

    @ColumnInfo(name = "is_baby")
    val is_baby: Boolean?,

    @ColumnInfo(name = "is_legendary")
    val is_legendary: Boolean?,

    @ColumnInfo(name = "is_mythical")
    val is_mythical: Boolean?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "names")
    val names: List<Name?>?,

    @ColumnInfo(name = "order")
    val order: Int?,

    @ColumnInfo(name = "pal_park_encounters")
    val pal_park_encounters: List<PalParkEncounter?>?,

    @ColumnInfo(name = "pokedex_numbers")
    val pokedex_numbers: List<PokedexNumber?>?,

    @ColumnInfo(name = "shape")
    val shape: Shape?,

    @ColumnInfo(name = "varieties")
    val varieties: List<Variety?>?
) {
    data class Color(val name: String?, val url: String?)
    data class EggGroup(val name: String?, val url: String?)
    data class EvolutionChain(val url: String?)
    data class EvolvesFromSpecies(val name: String?, val url: String?)
    data class FlavorTextEntry(
        val flavor_text: String?,
        val language: Language?,
        val version: Version?
    ) {
        data class Language(val name: String?, val url: String?)
        data class Version(val name: String?, val url: String?)
    }
    data class Genera(val genus: String?, val language: Language?) {
        data class Language(val name: String?, val url: String?)
    }
    data class Generation(val name: String?, val url: String?)
    data class GrowthRate(val name: String?, val url: String?)
    data class Habitat(val name: String?, val url: String?)
    data class Name(val name: String?, val language: Language?) {
        data class Language(val name: String?, val url: String?)
    }
    data class PalParkEncounter(
        val area: Area?,
        val base_score: Int?,
        val rate: Int?
    ) {
        data class Area(val name: String?, val url: String?)
    }
    data class PokedexNumber(
        val entry_number: Int?,
        val pokedex: Pokedex?
    ) {
        data class Pokedex(val name: String?, val url: String?)
    }
    data class Shape(val name: String?, val url: String?)
    data class Variety(
        val is_default: Boolean?,
        val pokemon: Pokemon?
    ) {
        data class Pokemon(val name: String?, val url: String?)
    }
}