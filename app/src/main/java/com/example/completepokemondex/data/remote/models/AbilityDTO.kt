package com.example.completepokemondex.data.remote.models


import com.google.gson.annotations.SerializedName

data class AbilityDTO(
    @SerializedName("effect_changes")
    val effectChanges: List<EffectChange?>?,
    @SerializedName("effect_entries")
    val effectEntries: List<EffectEntry?>?,
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry?>?,
    @SerializedName("generation")
    val generation: Generation?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_main_series")
    val isMainSeries: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("names")
    val names: List<Name?>?,
    @SerializedName("pokemon")
    val pokemon: List<Pokemon?>?
) {
    data class EffectChange(
        @SerializedName("effect_entries")
        val effectEntries: List<EffectEntry?>?,
        @SerializedName("version_group")
        val versionGroup: VersionGroup?
    ) {
        data class EffectEntry(
            @SerializedName("effect")
            val effect: String?,
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

        data class VersionGroup(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

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

    data class Pokemon(
        @SerializedName("is_hidden")
        val isHidden: Boolean?,
        @SerializedName("pokemon")
        val pokemon: Pokemon?,
        @SerializedName("slot")
        val slot: Int?
    ) {
        data class Pokemon(
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }
}