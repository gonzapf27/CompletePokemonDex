package com.example.completepokemondex.data.local.entities

import androidx.room.*
import com.example.completepokemondex.data.local.entities.converters.AbilityConverters

@Entity(tableName = "ability_table")
@TypeConverters(AbilityConverters::class)
data class AbilityEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "effect_changes")
    val effectChanges: List<EffectChangeEntity?>?,

    @ColumnInfo(name = "effect_entries")
    val effectEntries: List<EffectEntryEntity?>?,

    @ColumnInfo(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntryEntity?>?,

    @Embedded(prefix = "generation_")
    val generation: GenerationEntity?,

    @ColumnInfo(name = "is_main_series")
    val isMainSeries: Boolean?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "names")
    val names: List<NameEntity?>?,

    @ColumnInfo(name = "pokemon")
    val pokemon: List<PokemonEntity?>?
) {
    data class EffectChangeEntity(
        val effectEntries: List<EffectEntryEntity?>?,
        @Embedded(prefix = "version_group_")
        val versionGroup: VersionGroupEntity?
    ) {
        data class EffectEntryEntity(
            val effect: String?,
            @Embedded(prefix = "language_")
            val language: LanguageEntity?
        ) {
            data class LanguageEntity(
                val name: String?,
                val url: String?
            )
        }

        data class VersionGroupEntity(
            val name: String?,
            val url: String?
        )
    }

    data class EffectEntryEntity(
        val effect: String?,
        @Embedded(prefix = "language_")
        val language: LanguageEntity?,
        val shortEffect: String?
    ) {
        data class LanguageEntity(
            val name: String?,
            val url: String?
        )
    }

    data class FlavorTextEntryEntity(
        val flavorText: String?,
        @Embedded(prefix = "language_")
        val language: LanguageEntity?,
        @Embedded(prefix = "version_group_")
        val versionGroup: VersionGroupEntity?
    ) {
        data class LanguageEntity(
            val name: String?,
            val url: String?
        )

        data class VersionGroupEntity(
            val name: String?,
            val url: String?
        )
    }

    data class GenerationEntity(
        val name: String?,
        val url: String?
    )

    data class NameEntity(
        @Embedded(prefix = "language_")
        val language: LanguageEntity?,
        val name: String?
    ) {
        data class LanguageEntity(
            val name: String?,
            val url: String?
        )
    }

    data class PokemonEntity(
        val isHidden: Boolean?,
        @Embedded(prefix = "pokemon_")
        val pokemon: PokemonDataEntity?,
        val slot: Int?
    ) {
        data class PokemonDataEntity(
            val name: String?,
            val url: String?
        )
    }
}
