package com.example.completepokemondex.data.remote.models


import com.google.gson.annotations.SerializedName

data class PokemonMoveDTO(
    @SerializedName("accuracy")
    val accuracy: Int?,
    @SerializedName("contest_combos")
    val contestCombos: ContestCombos?,
    @SerializedName("contest_effect")
    val contestEffect: ContestEffect?,
    @SerializedName("contest_type")
    val contestType: ContestType?,
    @SerializedName("damage_class")
    val damageClass: DamageClass?,
    @SerializedName("effect_chance")
    val effectChance: Any?,
    @SerializedName("effect_changes")
    val effectChanges: List<Any?>?,
    @SerializedName("effect_entries")
    val effectEntries: List<EffectEntry?>?,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry?>?,
    @SerializedName("generation")
    val generation: Generation?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("learned_by_pokemon")
    val learnedByPokemon: List<LearnedByPokemon?>?,
    @SerializedName("machines")
    val machines: List<Machine?>?,
    @SerializedName("meta")
    val meta: Meta?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("names")
    val names: List<Name?>?,
    @SerializedName("past_values")
    val pastValues: List<Any?>?,
    @SerializedName("power")
    val power: Int?,
    @SerializedName("pp")
    val pp: Int?,
    @SerializedName("priority")
    val priority: Int?,
    @SerializedName("stat_changes")
    val statChanges: List<Any?>?,
    @SerializedName("super_contest_effect")
    val superContestEffect: SuperContestEffect?,
    @SerializedName("target")
    val target: Target?,
    @SerializedName("type")
    val type: Type?
) {
    data class ContestCombos(
        @SerializedName("normal")
        val normal: Normal?,
        @SerializedName("super")
        val superX: Super?
    ) {
        data class Normal(
            @SerializedName("use_after")
            val useAfter: List<UseAfter?>?,
            @SerializedName("use_before")
            val useBefore: Any?
        ) {
            data class UseAfter(
                @SerializedName("name")
                val name: String?,
                @SerializedName("url")
                val url: String?
            )
        }

        data class Super(
            @SerializedName("use_after")
            val useAfter: Any?,
            @SerializedName("use_before")
            val useBefore: Any?
        )
    }

    data class ContestEffect(
        @SerializedName("url")
        val url: String?
    )

    data class ContestType(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class DamageClass(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class EffectEntry(
        @SerializedName("effect")
        val effect: String?,
        @SerializedName("language")
        val language: Language?,
        @SerializedName("short_effect")
        val shortEffect: String?
    ) {
        data class Language(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class FlavorTextEntry(
        @SerializedName("flavor_text")
        val flavorText: String?,
        @SerializedName("language")
        val language: Language?,
        @SerializedName("version_group")
        val versionGroup: VersionGroup?
    ) {
        data class Language(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )

        data class VersionGroup(
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

    data class LearnedByPokemon(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Machine(
        @SerializedName("machine")
        val machine: Machine?,
        @SerializedName("version_group")
        val versionGroup: VersionGroup?
    ) {
        data class Machine(
            @SerializedName("url")
            val url: String?
        )

        data class VersionGroup(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class Meta(
        @SerializedName("ailment")
        val ailment: Ailment?,
        @SerializedName("ailment_chance")
        val ailmentChance: Int?,
        @SerializedName("category")
        val category: Category?,
        @SerializedName("crit_rate")
        val critRate: Int?,
        @SerializedName("drain")
        val drain: Int?,
        @SerializedName("flinch_chance")
        val flinchChance: Int?,
        @SerializedName("healing")
        val healing: Int?,
        @SerializedName("max_hits")
        val maxHits: Any?,
        @SerializedName("max_turns")
        val maxTurns: Any?,
        @SerializedName("min_hits")
        val minHits: Any?,
        @SerializedName("min_turns")
        val minTurns: Any?,
        @SerializedName("stat_chance")
        val statChance: Int?
    ) {
        data class Ailment(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )

        data class Category(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

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

    data class SuperContestEffect(
        @SerializedName("url")
        val url: String?
    )

    data class Target(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Type(
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?
    )
}