package com.example.completepokemondex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entidad que representa los sprites (imágenes) de un Pokémon.
 * Almacena las URLs de las diferentes variantes de sprites disponibles.
 *
 * @property id Identificador único del sprite en la base de datos.
 * @property pokemonId ID del Pokémon al que pertenecen estos sprites.
 * @property backDefault URL de la vista trasera predeterminada.
 * @property backFemale URL de la vista trasera femenina (puede ser nula).
 * @property backShiny URL de la vista trasera brillante.
 * @property backShinyFemale URL de la vista trasera brillante femenina (puede ser nula).
 * @property frontDefault URL de la vista frontal predeterminada.
 * @property frontFemale URL de la vista frontal femenina (puede ser nula).
 * @property frontShiny URL de la vista frontal brillante.
 * @property frontShinyFemale URL de la vista frontal brillante femenina (puede ser nula).
 */
@Entity(
    tableName = "pokemon_sprites",
    foreignKeys = [
        ForeignKey(
            entity = PokemonDetailsEntity::class,
            parentColumns = ["pokemon_id"],
            childColumns = ["pokemon_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PokemonSpritesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sprite_id")
    val id: Int = 0,

    @ColumnInfo(name = "pokemon_id", index = true)
    val pokemonId: Int,

    @ColumnInfo(name = "back_default")
    val backDefault: String?,

    @ColumnInfo(name = "back_female")
    val backFemale: String?,

    @ColumnInfo(name = "back_shiny")
    val backShiny: String?,

    @ColumnInfo(name = "back_shiny_female")
    val backShinyFemale: String?,

    @ColumnInfo(name = "front_default")
    val frontDefault: String?,

    @ColumnInfo(name = "front_female")
    val frontFemale: String?,

    @ColumnInfo(name = "front_shiny")
    val frontShiny: String?,

    @ColumnInfo(name = "front_shiny_female")
    val frontShinyFemale: String?
)