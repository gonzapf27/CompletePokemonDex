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
 * @property dreamWorldFrontDefault URL del sprite Dream World frontal predeterminado.
 * @property dreamWorldFrontFemale URL del sprite Dream World frontal femenino.
 * @property homeFrontDefault URL del sprite Home frontal predeterminado.
 * @property homeFrontFemale URL del sprite Home frontal femenino.
 * @property homeFrontShiny URL del sprite Home frontal brillante.
 * @property homeFrontShinyFemale URL del sprite Home frontal brillante femenino.
 * @property officialArtworkDefault URL del artwork oficial predeterminado.
 * @property officialArtworkShiny URL del artwork oficial brillante.
 * @property showdownBackDefault URL del sprite Showdown trasero predeterminado.
 * @property showdownBackFemale URL del sprite Showdown trasero femenino.
 * @property showdownBackShiny URL del sprite Showdown trasero brillante.
 * @property showdownBackShinyFemale URL del sprite Showdown trasero brillante femenino.
 * @property showdownFrontDefault URL del sprite Showdown frontal predeterminado.
 * @property showdownFrontFemale URL del sprite Showdown frontal femenino.
 * @property showdownFrontShiny URL del sprite Showdown frontal brillante.
 * @property showdownFrontShinyFemale URL del sprite Showdown frontal brillante femenino.
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
    val frontShinyFemale: String?,

    @ColumnInfo(name = "dream_world_front_default")
    val dreamWorldFrontDefault: String?,

    @ColumnInfo(name = "dream_world_front_female")
    val dreamWorldFrontFemale: String?,

    @ColumnInfo(name = "home_front_default")
    val homeFrontDefault: String?,

    @ColumnInfo(name = "home_front_female")
    val homeFrontFemale: String?,

    @ColumnInfo(name = "home_front_shiny")
    val homeFrontShiny: String?,

    @ColumnInfo(name = "home_front_shiny_female")
    val homeFrontShinyFemale: String?,

    @ColumnInfo(name = "official_artwork_default")
    val officialArtworkDefault: String?,

    @ColumnInfo(name = "official_artwork_shiny")
    val officialArtworkShiny: String?,

    @ColumnInfo(name = "showdown_back_default")
    val showdownBackDefault: String?,

    @ColumnInfo(name = "showdown_back_female")
    val showdownBackFemale: String?,

    @ColumnInfo(name = "showdown_back_shiny")
    val showdownBackShiny: String?,

    @ColumnInfo(name = "showdown_back_shiny_female")
    val showdownBackShinyFemale: String?,

    @ColumnInfo(name = "showdown_front_default")
    val showdownFrontDefault: String?,

    @ColumnInfo(name = "showdown_front_female")
    val showdownFrontFemale: String?,

    @ColumnInfo(name = "showdown_front_shiny")
    val showdownFrontShiny: String?,

    @ColumnInfo(name = "showdown_front_shiny_female")
    val showdownFrontShinyFemale: String?
)

/**
 * Clase que contiene solo los campos de sprites para embeber en PokemonDetailsEntity.
 * Esta clase no es una entidad por sí misma, sino un objeto que se incrustará en otra entidad.
 */
data class PokemonSpritesEmbedded(
    val backDefault: String?,
    val backFemale: String?,
    val backShiny: String?,
    val backShinyFemale: String?,
    val frontDefault: String?,
    val frontFemale: String?,
    val frontShiny: String?,
    val frontShinyFemale: String?,
    val dreamWorldFrontDefault: String?,
    val dreamWorldFrontFemale: String?,
    val homeFrontDefault: String?,
    val homeFrontFemale: String?,
    val homeFrontShiny: String?,
    val homeFrontShinyFemale: String?,
    val officialArtworkDefault: String?,
    val officialArtworkShiny: String?,
    val showdownBackDefault: String?,
    val showdownBackFemale: String?,
    val showdownBackShiny: String?,
    val showdownBackShinyFemale: String?,
    val showdownFrontDefault: String?,
    val showdownFrontFemale: String?,
    val showdownFrontShiny: String?,
    val showdownFrontShinyFemale: String?
)