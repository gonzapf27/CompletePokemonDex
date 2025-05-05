package com.example.completepokemondex.data.local.entities

import androidx.room.*
import com.example.completepokemondex.data.local.util.AbilityConverters

@TypeConverters(AbilityConverters::class)
@Entity(tableName = "ability_table")
data class AbilityEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "effect_changes")
    val effect_changes: List<EffectChange?>?,

    @ColumnInfo(name = "effect_entries")
    val effect_entries: List<EffectEntry?>?,

    @ColumnInfo(name = "flavor_text_entries")
    val flavor_text_entries: List<FlavorTextEntry?>?,

    @Embedded(prefix = "generation_")
    val generation: Generation?,

    @ColumnInfo(name = "is_main_series")
    val is_main_series: Boolean?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "names")
    val names: List<Name?>?,

    @ColumnInfo(name = "pokemon")
    val pokemon: List<Pokemon?>?
) {
    data class EffectChange(
        val effect_entries: List<EffectEntry?>?,
        @Embedded(prefix = "version_group_")
        val version_group: VersionGroup?
    ) {
        data class EffectEntry(
            val effect: String?,
            @Embedded(prefix = "language_")
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
        @Embedded(prefix = "language_")
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
        @Embedded(prefix = "language_")
        val language: Language?,
        @Embedded(prefix = "version_group_")
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
        @Embedded(prefix = "language_")
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
        @Embedded(prefix = "pokemon_")
        val pokemon: Pokemon?,
        val slot: Int?
    ) {
        data class Pokemon(
            val name: String?,
            val url: String?
        )
    }
}
