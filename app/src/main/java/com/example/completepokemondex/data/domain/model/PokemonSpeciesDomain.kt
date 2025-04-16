package com.example.completepokemondex.data.domain.model

data class PokemonSpeciesDomain(
    val base_happiness: Int?,
    val capture_rate: Int?,
    val color: Color?,
    val egg_groups: List<PokemonSpeciesDomain.EggGroup?>?,
    val evolution_chain: PokemonSpeciesDomain.EvolutionChain?,
    val evolves_from_species: PokemonSpeciesDomain.EvolvesFromSpecies?,
    val flavor_text_entries: List<PokemonSpeciesDomain.FlavorTextEntry?>?,
    val form_descriptions: List<Any?>?,
    val forms_switchable: Boolean?,
    val gender_rate: Int?,
    val genera: List<Genera?>?,
    val generation: Generation?,
    val growth_rate: GrowthRate?,
    val habitat: Habitat?,
    val has_gender_differences: Boolean?,
    val hatch_counter: Int?,
    val id: Int?,
    val is_baby: Boolean?,
    val is_legendary: Boolean?,
    val is_mythical: Boolean?,
    val name: String?,
    val names: List<Name?>?,
    val order: Int?,
    val pal_park_encounters: List<PalParkEncounter?>?,
    val pokedex_numbers: List<PokedexNumber?>?,
    val shape: Shape?,
    val varieties: List<Variety?>?
) {
    fun getCaptureDifficulty(): String {
        return when {
            //TODO: internacionalizar
            capture_rate == 255 || capture_rate != null && capture_rate > 200 -> "Muy fácil"
            capture_rate in 120..200 -> "Moderado"
            capture_rate in 100..119 -> "Fácil"
            capture_rate in 46..99 -> "Moderado"
            capture_rate == 45 || capture_rate in 4..44 -> "Difícil"
            capture_rate == 3 || capture_rate != null && capture_rate <= 3 -> "Extremadamente difícil"
            else -> "Desconocida"
        }
    }

    /**
     * Devuelve el ID del string de dificultad de captura según el valor de capture_rate.
     * El string debe obtenerse en la UI usando context.getString(result).
     */
    fun getCaptureDifficultyStringRes(): Int {
        return when {
            capture_rate == 255 || (capture_rate != null && capture_rate > 200) -> R.string.capture_difficulty_very_easy
            capture_rate in 120..200 -> R.string.capture_difficulty_moderate
            capture_rate in 100..119 -> R.string.capture_difficulty_easy
            capture_rate in 46..99 -> R.string.capture_difficulty_moderate
            capture_rate == 45 || (capture_rate in 4..44) -> R.string.capture_difficulty_hard
            capture_rate == 3 || (capture_rate != null && capture_rate <= 3) -> R.string.capture_difficulty_extremely_hard
            else -> R.string.capture_difficulty_unknown
        }
    }

    data class Color(
        val name: String?,
        val url: String?
    )

    data class EggGroup(
        val name: String?,
        val url: String?
    )

    data class EvolutionChain(
        val url: String?
    )

    data class EvolvesFromSpecies(
        val name: String?,
        val url: String?
    )

    data class FlavorTextEntry(
        val flavor_text: String?,
        val language: Language?,
        val version: Version?
    ) {
        data class Language(
            val name: String?,
            val url: String?
        )

        data class Version(
            val name: String?,
            val url: String?
        )
    }

    data class Genera(
        val genus: String?,
        val language: Language?
    ) {
        data class Language(
            val name: String?,
            val url: String?
        )
    }

    data class Generation(
        val name: String?,
        val url: String?
    )

    data class GrowthRate(
        val name: String?,
        val url: String?
    )

    data class Habitat(
        val name: String?,
        val url: String?
    )

    data class Name(
        val language: Language?,
        val name: String?
    ) {
        data class Language(
            val name: String?,
            val url: String?
        )
    }

    data class PalParkEncounter(
        val area: Area?,
        val base_score: Int?,
        val rate: Int?
    ) {
        data class Area(
            val name: String?,
            val url: String?
        )
    }

    data class PokedexNumber(
        val entry_number: Int?,
        val pokedex: Pokedex?
    ) {
        data class Pokedex(
            val name: String?,
            val url: String?
        )
    }

    data class Shape(
        val name: String?,
        val url: String?
    )

    data class Variety(
        val is_default: Boolean?,
        val pokemon: Pokemon?
    ) {
        data class Pokemon(
            val name: String?,
            val url: String?
        )
    }
}