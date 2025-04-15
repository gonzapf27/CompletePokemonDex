package com.example.completepokemondex.data.domain.model

data class AbilityDomain(
    val effect_changes: List<EffectChange?>?,
    val effect_entries: List<EffectEntry?>?,
    val flavor_text_entries: List<FlavorTextEntry?>?,
    val generation: Generation?,
    val id: Int?,
    val is_main_series: Boolean?,
    val name: String?,
    val names: List<Name?>?,
    val pokemon: List<Pokemon?>?
) {
    data class EffectChange(
        val effect_entries: List<EffectEntry?>?,
        val version_group: VersionGroup?
    ) {
        data class EffectEntry(
            val effect: String?,
            val language: Language?
        ) {
            data class Language(
                val name: String?,
                val url: String?
            )
        }

        data class VersionGroup(
            val name: String?,
            val url: String?
        )
    }

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

    data class Name(
        val language: Language?,
        val name: String?
    ) {
        data class Language(
            val name: String?,
            val url: String?
        )
    }

    data class Pokemon(
        val is_hidden: Boolean?,
        val pokemon: Pokemon?,
        val slot: Int?
    ) {
        data class Pokemon(
            val name: String?,
            val url: String?
        )
    }
}