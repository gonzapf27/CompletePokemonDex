package com.example.completepokemondex.data.local.entities

 import androidx.room.Entity
 import androidx.room.PrimaryKey
import androidx.room.TypeConverters
 import com.example.completepokemondex.data.local.converters.SpritesTypeConverters

/**
 * Entidad que representa los sprites de un Pokémon en la base de datos.
 *
 * Esta entidad almacena la estructura completa de sprites de un Pokémon según la PokeAPI,
 * incluyendo las representaciones oficiales, por generación y variantes especiales.
 */
 @Entity(tableName = "pokemonsprites")
@TypeConverters(SpritesTypeConverters::class)
 data class PokemonSpritesEntity(
     @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Default sprites
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?,

    // Other artwork collections
    val other: OtherSprites?,

    // Generation-specific sprites
    val versions: VersionSprites?
)

/**
 * Representa colecciones alternativas de sprites oficiales.
 */
data class OtherSprites(
    val dream_world: DreamWorldSprites?,
    val official_artwork: OfficialArtworkSprites?,
    val showdown: ShowdownSprites?,
    val home: HomeSprites?
)

data class DreamWorldSprites(
    val front_default: String?,
    val front_female: String?
)

data class OfficialArtworkSprites(
    val front_default: String?,
    val front_shiny: String?
)

data class ShowdownSprites(
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class HomeSprites(
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

/**
 * Representa sprites específicos por generación de juegos.
 */
data class VersionSprites(
    val generation_i: GenerationISprites?,
    val generation_ii: GenerationIISprites?,
    val generation_iii: GenerationIIISprites?,
    val generation_iv: GenerationIVSprites?,
    val generation_v: GenerationVSprites?,
    val generation_vi: GenerationVISprites?,
    val generation_vii: GenerationVIISprites?,
    val generation_viii: GenerationVIIISprites?
)

data class GenerationISprites(
    val red_blue: RedBlueSprites?,
    val yellow: YellowSprites?
)

data class RedBlueSprites(
    val back_default: String?,
    val back_gray: String?,
    val back_transparent: String?,
    val front_default: String?,
    val front_gray: String?,
    val front_transparent: String?
)

data class YellowSprites(
    val back_default: String?,
    val back_gbc: String?,
    val back_gray: String?,
    val back_transparent: String?,
    val front_default: String?,
    val front_gbc: String?,
    val front_gray: String?,
    val front_transparent: String?
)

data class GenerationIISprites(
    val crystal: CrystalSprites?,
    val gold: GoldSprites?,
    val silver: SilverSprites?
)

data class CrystalSprites(
    val back_default: String?,
    val back_shiny: String?,
    val back_transparent: String?,
    val back_transparent_shiny: String?,
    val front_default: String?,
    val front_shiny: String?,
    val front_transparent: String?,
    val front_transparent_shiny: String?
)

data class GoldSprites(
    val back_default: String?,
    val back_shiny: String?,
    val front_default: String?,
    val front_shiny: String?,
    val front_transparent: String?
)

data class SilverSprites(
    val back_default: String?,
    val back_shiny: String?,
    val front_default: String?,
    val front_shiny: String?,
    val front_transparent: String?
)

data class GenerationIIISprites(
    val emerald: EmeraldSprites?,
    val firered_leafgreen: FireRedLeafGreenSprites?,
    val ruby_sapphire: RubySapphireSprites?
)

data class EmeraldSprites(
    val front_default: String?,
    val front_shiny: String?
)

data class FireRedLeafGreenSprites(
    val back_default: String?,
    val back_shiny: String?,
    val front_default: String?,
    val front_shiny: String?
)

data class RubySapphireSprites(
    val back_default: String?,
    val back_shiny: String?,
    val front_default: String?,
    val front_shiny: String?
)

data class GenerationIVSprites(
    val diamond_pearl: DiamondPearlSprites?,
    val heartgold_soulsilver: HeartGoldSoulSilverSprites?,
    val platinum: PlatinumSprites?
)

data class DiamondPearlSprites(
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class HeartGoldSoulSilverSprites(
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class PlatinumSprites(
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
 )

data class GenerationVSprites(
    val black_white: BlackWhiteSprites?
)

data class BlackWhiteSprites(
    val animated: AnimatedSprites?,
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class AnimatedSprites(
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class GenerationVISprites(
    val omegaruby_alphasapphire: OmegaRubyAlphaSapphireSprites?,
    val x_y: XYSprites?
)

data class OmegaRubyAlphaSapphireSprites(
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class XYSprites(
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class GenerationVIISprites(
    val ultra_sun_ultra_moon: UltraSunUltraMoonSprites?,
    val icons: IconsSprites?
)

data class UltraSunUltraMoonSprites(
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?
)

data class IconsSprites(
    val front_default: String?,
    val front_female: String?
)

data class GenerationVIIISprites(
    val icons: GenerationVIIIIconsSprites?
)

data class GenerationVIIIIconsSprites(
    val front_default: String?,
    val front_female: String?
)