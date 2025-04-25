package com.example.completepokemondex.data.domain.model

data class PokemonMoveDomain(
    val accuracy: Int?,
    val contest_combos: ContestCombos?,
    val contest_effect: ContestEffect?,
    val contest_type: ContestType?,
    val damage_class: DamageClass?,
    val effect_chance: Any?,
    val effect_changes: List<Any?>?,
    val effect_entries: List<EffectEntry?>?,
    val flavor_text_entries: List<FlavorTextEntry?>?,
    val generation: Generation?,
    val id: Int?,
    val learned_by_pokemon: List<LearnedByPokemon?>?,
    val machines: List<Machine?>?,
    val meta: Meta?,
    val name: String?,
    val names: List<Name?>?,
    val past_values: List<Any?>?,
    val power: Int?,
    val pp: Int?,
    val priority: Int?,
    val stat_changes: List<Any?>?,
    val super_contest_effect: SuperContestEffect?,
    val target: Target?,
    val type: Type?
) {
    data class ContestCombos(
        val normal: Normal?,
        val `super`: Super?
    ) {
        data class Normal(
            val use_after: List<UseAfter?>?,
            val use_before: Any?
        ) {
            data class UseAfter(
                val name: String?,
                val url: String?
            )
        }

        data class Super(
            val use_after: Any?,
            val use_before: Any?
        )
    }

    data class ContestEffect(
        val url: String?
    )

    data class ContestType(
        val name: String?,
        val url: String?
    )

    data class DamageClass(
        val name: String?,
        val url: String?
    )

    data class EffectEntry(
        val effect: String?,
        val language: Language?,
        val short_effect: String?
    ) {
        data class Language(
            val name: String?,
            val url: String?
        )
    }

    data class FlavorTextEntry(
        val flavor_text: String?,
        val language: Language?,
        val version_group: VersionGroup?
    ) {
        data class Language(
            val name: String?,
            val url: String?
        )

        data class VersionGroup(
            val name: String?,
            val url: String?
        )
    }

    data class Generation(
        val name: String?,
        val url: String?
    )

    data class LearnedByPokemon(
        val name: String?,
        val url: String?
    )

    data class Machine(
        val machine: Machine?,
        val version_group: VersionGroup?
    ) {
        data class Machine(
            val url: String?
        )

        data class VersionGroup(
            val name: String?,
            val url: String?
        )
    }

    data class Meta(
        val ailment: Ailment?,
        val ailment_chance: Int?,
        val category: Category?,
        val crit_rate: Int?,
        val drain: Int?,
        val flinch_chance: Int?,
        val healing: Int?,
        val max_hits: Any?,
        val max_turns: Any?,
        val min_hits: Any?,
        val min_turns: Any?,
        val stat_chance: Int?
    ) {
        data class Ailment(
            val name: String?,
            val url: String?
        )

        data class Category(
            val name: String?,
            val url: String?
        )
    }

    data class Name(
        val language: Language?,
        val name: String?
    ) {
        data class Language(
            val name: String?,
            val url: String?
        )
    }

    data class SuperContestEffect(
        val url: String?
    )

    data class Target(
        val name: String?,
        val url: String?
    )

    data class Type(
        val name: String?,
        val url: String?
    )
}