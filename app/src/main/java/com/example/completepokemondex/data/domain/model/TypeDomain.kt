package com.example.completepokemondex.data.domain.model

data class TypeDomain(
    val damageRelations: DamageRelations,
    val gameIndices: List<GameIndice>,
    val generation: Generation,
    val id: Int,
    val moveDamageClass: MoveDamageClass,
    val moves: List<Move>,
    val name: String,
    val names: List<Name>,
    val pastDamageRelations: List<Any?>,
    val pokemon: List<Pokemon>,
    val sprites: Sprites
) {
    data class DamageRelations(
        val doubleDamageFrom: List<DoubleDamageFrom>,
        val doubleDamageTo: List<DoubleDamageTo>,
        val halfDamageFrom: List<HalfDamageFrom>,
        val halfDamageTo: List<HalfDamageTo>,
        val noDamageFrom: List<NoDamageFrom>,
        val noDamageTo: List<Any?>
    ) {
        data class DoubleDamageFrom(
            val name: String,
            val url: String
        )

        data class DoubleDamageTo(
            val name: String,
            val url: String
        )

        data class HalfDamageFrom(
            val name: String,
            val url: String
        )

        data class HalfDamageTo(
            val name: String,
            val url: String
        )

        data class NoDamageFrom(
            val name: String,
            val url: String
        )
    }

    data class GameIndice(
        val gameIndex: Int,
        val generation: Generation
    ) {
        data class Generation(
            val name: String,
            val url: String
        )
    }

    data class Generation(
        val name: String,
        val url: String
    )

    data class MoveDamageClass(
        val name: String,
        val url: String
    )

    data class Move(
        val name: String,
        val url: String
    )

    data class Name(
        val language: Language,
        val name: String
    ) {
        data class Language(
            val name: String,
            val url: String
        )
    }

    data class Pokemon(
        val pokemon: Pokemon,
        val slot: Int
    ) {
        data class Pokemon(
            val name: String,
            val url: String
        )
    }

    data class Sprites(
        val generationIii: GenerationIii,
        val generationIv: GenerationIv,
        val generationIx: GenerationIx,
        val generationV: GenerationV,
        val generationVi: GenerationVi,
        val generationVii: GenerationVii,
        val generationViii: GenerationViii
    ) {
        data class GenerationIii(
            val colosseum: Colosseum,
            val emerald: Emerald,
            val fireredLeafgreen: FireredLeafgreen,
            val rubySaphire: RubySaphire,
            val xd: Xd
        ) {
            data class Colosseum(
                val nameIcon: String
            )

            data class Emerald(
                val nameIcon: String
            )

            data class FireredLeafgreen(
                val nameIcon: String
            )

            data class RubySaphire(
                val nameIcon: String
            )

            data class Xd(
                val nameIcon: String
            )
        }

        data class GenerationIv(
            val diamondPearl: DiamondPearl,
            val heartgoldSoulsilver: HeartgoldSoulsilver,
            val platinum: Platinum
        ) {
            data class DiamondPearl(
                val nameIcon: String
            )

            data class HeartgoldSoulsilver(
                val nameIcon: String
            )

            data class Platinum(
                val nameIcon: String
            )
        }

        data class GenerationIx(
            val scarletViolet: ScarletViolet
        ) {
            data class ScarletViolet(
                val nameIcon: String
            )
        }

        data class GenerationV(
            val black2White2: Black2White2,
            val blackWhite: BlackWhite
        ) {
            data class Black2White2(
                val nameIcon: String
            )

            data class BlackWhite(
                val nameIcon: String
            )
        }

        data class GenerationVi(
            val omegaRubyAlphaSapphire: OmegaRubyAlphaSapphire,
            val xY: XY
        ) {
            data class OmegaRubyAlphaSapphire(
                val nameIcon: String
            )

            data class XY(
                val nameIcon: String
            )
        }

        data class GenerationVii(
            val letsGoPikachuLetsGoEevee: LetsGoPikachuLetsGoEevee,
            val sunMoon: SunMoon,
            val ultraSunUltraMoon: UltraSunUltraMoon
        ) {
            data class LetsGoPikachuLetsGoEevee(
                val nameIcon: String
            )

            data class SunMoon(
                val nameIcon: String
            )

            data class UltraSunUltraMoon(
                val nameIcon: String
            )
        }

        data class GenerationViii(
            val brilliantDiamondAndShiningPearl: BrilliantDiamondAndShiningPearl,
            val legendsArceus: LegendsArceus,
            val swordShield: SwordShield
        ) {
            data class BrilliantDiamondAndShiningPearl(
                val nameIcon: String
            )

            data class LegendsArceus(
                val nameIcon: String
            )

            data class SwordShield(
                val nameIcon: String
            )
        }
    }
}