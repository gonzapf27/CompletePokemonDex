package com.example.completepokemondex.data.domain.model

data class PokemonDetails(
    val abilities: List<Ability>,
    val base_experience: Int,
    val cries: Cries,
    val forms: List<Form>,
    val game_indices: List<GameIndice>,
    val height: Int,
    val held_items: List<HeldItem>,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    val past_abilities: List<PastAbility>,
    val past_types: List<Any?>,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
) {
    data class Ability(
        val ability: Ability,
        val is_hidden: Boolean,
        val slot: Int
    ) {
        data class Ability(
            val name: String,
            val url: String
        )
    }

    data class Cries(
        val latest: String,
        val legacy: String
    )

    data class Form(
        val name: String,
        val url: String
    )

    data class GameIndice(
        val game_index: Int,
        val version: Version
    ) {
        data class Version(
            val name: String,
            val url: String
        )
    }

    data class HeldItem(
        val item: Item,
        val version_details: List<VersionDetail>
    ) {
        data class Item(
            val name: String,
            val url: String
        )

        data class VersionDetail(
            val rarity: Int,
            val version: Version
        ) {
            data class Version(
                val name: String,
                val url: String
            )
        }
    }

    data class Move(
        val move: Move,
        val version_group_details: List<VersionGroupDetail>
    ) {
        data class Move(
            val name: String,
            val url: String
        )

        data class VersionGroupDetail(
            val level_learned_at: Int,
            val move_learn_method: MoveLearnMethod,
            val order: Any?,
            val version_group: VersionGroup
        ) {
            data class MoveLearnMethod(
                val name: String,
                val url: String
            )

            data class VersionGroup(
                val name: String,
                val url: String
            )
        }
    }

    data class PastAbility(
        val abilities: List<Ability>,
        val generation: Generation
    ) {
        data class Ability(
            val ability: Any?,
            val is_hidden: Boolean,
            val slot: Int
        )

        data class Generation(
            val name: String,
            val url: String
        )
    }

    data class Species(
        val name: String,
        val url: String
    )

    data class Sprites(
        val back_default: String,
        val back_female: Any?,
        val back_shiny: String,
        val back_shiny_female: Any?,
        val front_default: String,
        val front_female: Any?,
        val front_shiny: String,
        val front_shiny_female: Any?,
        val other: Other,
        val versions: Versions
    ) {
        data class Other(
            val dream_world: DreamWorld,
            val home: Home,
            val official_artwork: OfficialArtwork,
            val showdown: Showdown
        ) {
            data class DreamWorld(
                val front_default: String,
                val front_female: Any?
            )

            data class Home(
                val front_default: String,
                val front_female: Any?,
                val front_shiny: String,
                val front_shiny_female: Any?
            )

            data class OfficialArtwork(
                val front_default: String,
                val front_shiny: String
            )

            data class Showdown(
                val back_default: String,
                val back_female: Any?,
                val back_shiny: String,
                val back_shiny_female: Any?,
                val front_default: String,
                val front_female: Any?,
                val front_shiny: String,
                val front_shiny_female: Any?
            )
        }

        data class Versions(
            val generation_i: GenerationI,
            val generation_ii: GenerationIi,
            val generation_iii: GenerationIii,
            val generation_iv: GenerationIv,
            val generation_v: GenerationV,
            val generation_vi: GenerationVi,
            val generation_vii: GenerationVii,
            val generation_viii: GenerationViii
        ) {
            data class GenerationI(
                val red_blue: RedBlue,
                val yellow: Yellow
            ) {
                data class RedBlue(
                    val back_default: String,
                    val back_gray: String,
                    val back_transparent: String,
                    val front_default: String,
                    val front_gray: String,
                    val front_transparent: String
                )

                data class Yellow(
                    val back_default: String,
                    val back_gray: String,
                    val back_transparent: String,
                    val front_default: String,
                    val front_gray: String,
                    val front_transparent: String
                )
            }

            data class GenerationIi(
                val crystal: Crystal,
                val gold: Gold,
                val silver: Silver
            ) {
                data class Crystal(
                    val back_default: String,
                    val back_shiny: String,
                    val back_shiny_transparent: String,
                    val back_transparent: String,
                    val front_default: String,
                    val front_shiny: String,
                    val front_shiny_transparent: String,
                    val front_transparent: String
                )

                data class Gold(
                    val back_default: String,
                    val back_shiny: String,
                    val front_default: String,
                    val front_shiny: String,
                    val front_transparent: String
                )

                data class Silver(
                    val back_default: String,
                    val back_shiny: String,
                    val front_default: String,
                    val front_shiny: String,
                    val front_transparent: String
                )
            }

            data class GenerationIii(
                val emerald: Emerald,
                val firered_leafgreen: FireredLeafgreen,
                val ruby_sapphire: RubySapphire
            ) {
                data class Emerald(
                    val front_default: String,
                    val front_shiny: String
                )

                data class FireredLeafgreen(
                    val back_default: String,
                    val back_shiny: String,
                    val front_default: String,
                    val front_shiny: String
                )

                data class RubySapphire(
                    val back_default: String,
                    val back_shiny: String,
                    val front_default: String,
                    val front_shiny: String
                )
            }

            data class GenerationIv(
                val diamond_pearl: DiamondPearl,
                val heartgold_soulsilver: HeartgoldSoulsilver,
                val platinum: Platinum
            ) {
                data class DiamondPearl(
                    val back_default: String,
                    val back_female: Any?,
                    val back_shiny: String,
                    val back_shiny_female: Any?,
                    val front_default: String,
                    val front_female: Any?,
                    val front_shiny: String,
                    val front_shiny_female: Any?
                )

                data class HeartgoldSoulsilver(
                    val back_default: String,
                    val back_female: Any?,
                    val back_shiny: String,
                    val back_shiny_female: Any?,
                    val front_default: String,
                    val front_female: Any?,
                    val front_shiny: String,
                    val front_shiny_female: Any?
                )

                data class Platinum(
                    val back_default: String,
                    val back_female: Any?,
                    val back_shiny: String,
                    val back_shiny_female: Any?,
                    val front_default: String,
                    val front_female: Any?,
                    val front_shiny: String,
                    val front_shiny_female: Any?
                )
            }

            data class GenerationV(
                val black_white: BlackWhite
            ) {
                data class BlackWhite(
                    val animated: Animated,
                    val back_default: String,
                    val back_female: Any?,
                    val back_shiny: String,
                    val back_shiny_female: Any?,
                    val front_default: String,
                    val front_female: Any?,
                    val front_shiny: String,
                    val front_shiny_female: Any?
                ) {
                    data class Animated(
                        val back_default: String,
                        val back_female: Any?,
                        val back_shiny: String,
                        val back_shiny_female: Any?,
                        val front_default: String,
                        val front_female: Any?,
                        val front_shiny: String,
                        val front_shiny_female: Any?
                    )
                }
            }

            data class GenerationVi(
                val omega_ruby_alpha_sapphire: OmegarubyAlphasapphire,
                val x_y: XY
            ) {
                data class OmegarubyAlphasapphire(
                    val front_default: String,
                    val front_female: Any?,
                    val front_shiny: String,
                    val front_shiny_female: Any?
                )

                data class XY(
                    val front_default: String,
                    val front_female: Any?,
                    val front_shiny: String,
                    val front_shiny_female: Any?
                )
            }

            data class GenerationVii(
                val icons: Icons,
                val ultra_sun_ultra_moon: UltraSunUltraMoon
            ) {
                data class Icons(
                    val front_default: String,
                    val front_female: Any?
                )

                data class UltraSunUltraMoon(
                    val front_default: String,
                    val front_female: Any?,
                    val front_shiny: String,
                    val front_shiny_female: Any?
                )
            }

            data class GenerationViii(
                val icons: Icons
            ) {
                data class Icons(
                    val front_default: String,
                    val front_female: Any?
                )
            }
        }
    }

    data class Stat(
        val base_stat: Int,
        val effort: Int,
        val stat: Stat
    ) {
        data class Stat(
            val name: String,
            val url: String
        )
    }

    data class Type(
        val slot: Int,
        val type: Type
    ) {
        data class Type(
            val name: String,
            val url: String
        )
    }
}