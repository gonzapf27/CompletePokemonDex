package com.example.completepokemondex.data.domain.model

/**
 * Modelo de dominio que representa los detalles de un Pokémon.
 * 
 * Esta clase proporciona una representación limpia y específica de dominio para los detalles
 * de un Pokémon, abstrayendo los detalles de implementación de la capa de datos.
 * 
 * @property id Identificador único del Pokémon.
 * @property name Nombre del Pokémon con primera letra en mayúscula.
 * @property height Altura del Pokémon (en decímetros).
 * @property weight Peso del Pokémon (en hectogramos).
 * @property sprites Sprites (imágenes) del Pokémon.
 */

data class PokemonDetailsDomain(
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
            val order: Int?,
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
        val back_female: String,
        val back_shiny: String,
        val back_shiny_female: String,
        val front_default: String,
        val front_female: String,
        val front_shiny: String,
        val front_shiny_female: String,
        val other: Other,
        val versions: Versions
    ) {
        data class Other(
            val dream_world: DreamWorld,
            val home: Home,
            val `official-artwork`: OfficialArtwork,
            val showdown: Showdown
        ) {
            data class DreamWorld(
                val front_default: String,
                val front_female: Any?
            )

            data class Home(
                val front_default: String,
                val front_female: String,
                val front_shiny: String,
                val front_shiny_female: String
            )

            data class OfficialArtwork(
                val front_default: String,
                val front_shiny: String
            )

            data class Showdown(
                val back_default: String,
                val back_female: String,
                val back_shiny: String,
                val back_shiny_female: Any?,
                val front_default: String,
                val front_female: String,
                val front_shiny: String,
                val front_shiny_female: String
            )
        }

        data class Versions(
            val `generation-i`: GenerationI,
            val `generation-ii`: GenerationIi,
            val `generation-iii`: GenerationIii,
            val `generation-iv`: GenerationIv,
            val `generation-v`: GenerationV,
            val `generation-vi`: GenerationVi,
            val `generation-vii`: GenerationVii,
            val `generation-viii`: GenerationViii
        ) {
            data class GenerationI(
                val `red-blue`: RedBlue,
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
                val `firered-leafgreen`: FireredLeafgreen,
                val `ruby-sapphire`: RubySapphire
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
                val `diamond-pearl`: DiamondPearl,
                val `heartgold-soulsilver`: HeartgoldSoulsilver,
                val platinum: Platinum
            ) {
                data class DiamondPearl(
                    val back_default: String,
                    val back_female: String,
                    val back_shiny: String,
                    val back_shiny_female: String,
                    val front_default: String,
                    val front_female: String,
                    val front_shiny: String,
                    val front_shiny_female: String
                )

                data class HeartgoldSoulsilver(
                    val back_default: String,
                    val back_female: String,
                    val back_shiny: String,
                    val back_shiny_female: String,
                    val front_default: String,
                    val front_female: String,
                    val front_shiny: String,
                    val front_shiny_female: String
                )

                data class Platinum(
                    val back_default: String,
                    val back_female: String,
                    val back_shiny: String,
                    val back_shiny_female: String,
                    val front_default: String,
                    val front_female: String,
                    val front_shiny: String,
                    val front_shiny_female: String
                )
            }

            data class GenerationV(
                val `black-white`: BlackWhite
            ) {
                data class BlackWhite(
                    val animated: Animated,
                    val back_default: String,
                    val back_female: String,
                    val back_shiny: String,
                    val back_shiny_female: String,
                    val front_default: String,
                    val front_female: String,
                    val front_shiny: String,
                    val front_shiny_female: String
                ) {
                    data class Animated(
                        val back_default: String,
                        val back_female: String,
                        val back_shiny: String,
                        val back_shiny_female: String,
                        val front_default: String,
                        val front_female: String,
                        val front_shiny: String,
                        val front_shiny_female: String
                    )
                }
            }

            data class GenerationVi(
                val `omegaruby-alphasapphire`: OmegarubyAlphasapphire,
                val `x-y`: XY
            ) {
                data class OmegarubyAlphasapphire(
                    val front_default: String,
                    val front_female: String,
                    val front_shiny: String,
                    val front_shiny_female: String
                )

                data class XY(
                    val front_default: String,
                    val front_female: String,
                    val front_shiny: String,
                    val front_shiny_female: String
                )
            }

            data class GenerationVii(
                val icons: Icons,
                val `ultra-sun-ultra-moon`: UltraSunUltraMoon
            ) {
                data class Icons(
                    val front_default: String,
                    val front_female: Any?
                )

                data class UltraSunUltraMoon(
                    val front_default: String,
                    val front_female: String,
                    val front_shiny: String,
                    val front_shiny_female: String
                )
            }

            data class GenerationViii(
                val icons: Icons
            ) {
                data class Icons(
                    val front_default: String,
                    val front_female: String
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