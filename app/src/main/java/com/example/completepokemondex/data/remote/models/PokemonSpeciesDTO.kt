package com.example.completepokemondex.data.remote.models


import com.google.gson.annotations.SerializedName

data class PokemonSpeciesDTO(
    @SerializedName("base_happiness")
    val baseHappiness: Int?,
    @SerializedName("capture_rate")
    val captureRate: Int?,
    @SerializedName("color")
    val color: Color?,
    @SerializedName("egg_groups")
    val eggGroups: List<EggGroup?>?,
    @SerializedName("evolution_chain")
    val evolutionChain: EvolutionChain?,
    @SerializedName("evolves_from_species")
    val evolvesFromSpecies: EvolvesFromSpecies?,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry?>?,
    @SerializedName("form_descriptions")
    val formDescriptions: List<Any?>?,
    @SerializedName("forms_switchable")
    val formsSwitchable: Boolean?,
    @SerializedName("gender_rate")
    val genderRate: Int?,
    @SerializedName("genera")
    val genera: List<Genera?>?,
    @SerializedName("generation")
    val generation: Generation?,
    @SerializedName("growth_rate")
    val growthRate: GrowthRate?,
    @SerializedName("habitat")
    val habitat: Habitat?,
    @SerializedName("has_gender_differences")
    val hasGenderDifferences: Boolean?,
    @SerializedName("hatch_counter")
    val hatchCounter: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_baby")
    val isBaby: Boolean?,
    @SerializedName("is_legendary")
    val isLegendary: Boolean?,
    @SerializedName("is_mythical")
    val isMythical: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("names")
    val names: List<Name?>?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("pal_park_encounters")
    val palParkEncounters: List<PalParkEncounter?>?,
    @SerializedName("pokedex_numbers")
    val pokedexNumbers: List<PokedexNumber?>?,
    @SerializedName("shape")
    val shape: Shape?,
    @SerializedName("varieties")
    val varieties: List<Variety?>?
) {
    data class Color(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class EggGroup(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class EvolutionChain(
        @SerializedName("url")
        val url: String?
    )

    data class EvolvesFromSpecies(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class FlavorTextEntry(
        @SerializedName("flavor_text")
        val flavorText: String?,
        @SerializedName("language")
        val language: Language?,
        @SerializedName("version")
        val version: Version?
    ) {
        data class Language(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )

        data class Version(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class Genera(
        @SerializedName("genus")
        val genus: String?,
        @SerializedName("language")
        val language: Language?
    ) {
        data class Language(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class Generation(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class GrowthRate(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Habitat(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Name(
        @SerializedName("language")
        val language: Language?,
        @SerializedName("name")
        val name: String?
    ) {
        data class Language(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class PalParkEncounter(
        @SerializedName("area")
        val area: Area?,
        @SerializedName("base_score")
        val baseScore: Int?,
        @SerializedName("rate")
        val rate: Int?
    ) {
        data class Area(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class PokedexNumber(
        @SerializedName("entry_number")
        val entryNumber: Int?,
        @SerializedName("pokedex")
        val pokedex: Pokedex?
    ) {
        data class Pokedex(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class Shape(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Variety(
        @SerializedName("is_default")
        val isDefault: Boolean?,
        @SerializedName("pokemon")
        val pokemon: Pokemon?
    ) {
        data class Pokemon(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }
}