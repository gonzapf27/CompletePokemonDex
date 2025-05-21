package com.example.completepokemondex.data.remote.models


import com.google.gson.annotations.SerializedName

data class TypeDTO(
    @SerializedName("damage_relations")
    val damageRelations: DamageRelations,
    @SerializedName("game_indices")
    val gameIndices: List<GameIndice>,
    @SerializedName("generation")
    val generation: Generation,
    @SerializedName("id")
    val id: Int,
    @SerializedName("move_damage_class")
    val moveDamageClass: MoveDamageClass,
    @SerializedName("moves")
    val moves: List<Move>,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<Name>,
    @SerializedName("past_damage_relations")
    val pastDamageRelations: List<Any?>,
    @SerializedName("pokemon")
    val pokemon: List<Pokemon>,
    @SerializedName("sprites")
    val sprites: Sprites
) {
    data class DamageRelations(
        @SerializedName("double_damage_from")
        val doubleDamageFrom: List<DoubleDamageFrom>,
        @SerializedName("double_damage_to")
        val doubleDamageTo: List<DoubleDamageTo>,
        @SerializedName("half_damage_from")
        val halfDamageFrom: List<HalfDamageFrom>,
        @SerializedName("half_damage_to")
        val halfDamageTo: List<HalfDamageTo>,
        @SerializedName("no_damage_from")
        val noDamageFrom: List<NoDamageFrom>,
        @SerializedName("no_damage_to")
        val noDamageTo: List<Any?>
    ) {
        data class DoubleDamageFrom(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )

        data class DoubleDamageTo(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )

        data class HalfDamageFrom(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )

        data class HalfDamageTo(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )

        data class NoDamageFrom(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }

    data class GameIndice(
        @SerializedName("game_index")
        val gameIndex: Int,
        @SerializedName("generation")
        val generation: Generation
    ) {
        data class Generation(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }

    data class Generation(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )

    data class MoveDamageClass(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )

    data class Move(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )

    data class Name(
        @SerializedName("language")
        val language: Language,
        @SerializedName("name")
        val name: String
    ) {
        data class Language(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }

    data class Pokemon(
        @SerializedName("pokemon")
        val pokemon: Pokemon,
        @SerializedName("slot")
        val slot: Int
    ) {
        data class Pokemon(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }

    data class Sprites(
        @SerializedName("generation-iii")
        val generationIii: GenerationIii,
        @SerializedName("generation-iv")
        val generationIv: GenerationIv,
        @SerializedName("generation-ix")
        val generationIx: GenerationIx,
        @SerializedName("generation-v")
        val generationV: GenerationV,
        @SerializedName("generation-vi")
        val generationVi: GenerationVi,
        @SerializedName("generation-vii")
        val generationVii: GenerationVii,
        @SerializedName("generation-viii")
        val generationViii: GenerationViii
    ) {
        data class GenerationIii(
            @SerializedName("colosseum")
            val colosseum: Colosseum,
            @SerializedName("emerald")
            val emerald: Emerald,
            @SerializedName("firered-leafgreen")
            val fireredLeafgreen: FireredLeafgreen,
            @SerializedName("ruby-saphire")
            val rubySaphire: RubySaphire,
            @SerializedName("xd")
            val xd: Xd
        ) {
            data class Colosseum(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class Emerald(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class FireredLeafgreen(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class RubySaphire(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class Xd(
                @SerializedName("name_icon")
                val nameIcon: String
            )
        }

        data class GenerationIv(
            @SerializedName("diamond-pearl")
            val diamondPearl: DiamondPearl,
            @SerializedName("heartgold-soulsilver")
            val heartgoldSoulsilver: HeartgoldSoulsilver,
            @SerializedName("platinum")
            val platinum: Platinum
        ) {
            data class DiamondPearl(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class HeartgoldSoulsilver(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class Platinum(
                @SerializedName("name_icon")
                val nameIcon: String
            )
        }

        data class GenerationIx(
            @SerializedName("scarlet-violet")
            val scarletViolet: ScarletViolet
        ) {
            data class ScarletViolet(
                @SerializedName("name_icon")
                val nameIcon: String
            )
        }

        data class GenerationV(
            @SerializedName("black-2-white-2")
            val black2White2: Black2White2,
            @SerializedName("black-white")
            val blackWhite: BlackWhite
        ) {
            data class Black2White2(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class BlackWhite(
                @SerializedName("name_icon")
                val nameIcon: String
            )
        }

        data class GenerationVi(
            @SerializedName("omega-ruby-alpha-sapphire")
            val omegaRubyAlphaSapphire: OmegaRubyAlphaSapphire,
            @SerializedName("x-y")
            val xY: XY
        ) {
            data class OmegaRubyAlphaSapphire(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class XY(
                @SerializedName("name_icon")
                val nameIcon: String
            )
        }

        data class GenerationVii(
            @SerializedName("lets-go-pikachu-lets-go-eevee")
            val letsGoPikachuLetsGoEevee: LetsGoPikachuLetsGoEevee,
            @SerializedName("sun-moon")
            val sunMoon: SunMoon,
            @SerializedName("ultra-sun-ultra-moon")
            val ultraSunUltraMoon: UltraSunUltraMoon
        ) {
            data class LetsGoPikachuLetsGoEevee(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class SunMoon(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class UltraSunUltraMoon(
                @SerializedName("name_icon")
                val nameIcon: String
            )
        }

        data class GenerationViii(
            @SerializedName("brilliant-diamond-and-shining-pearl")
            val brilliantDiamondAndShiningPearl: BrilliantDiamondAndShiningPearl,
            @SerializedName("legends-arceus")
            val legendsArceus: LegendsArceus,
            @SerializedName("sword-shield")
            val swordShield: SwordShield
        ) {
            data class BrilliantDiamondAndShiningPearl(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class LegendsArceus(
                @SerializedName("name_icon")
                val nameIcon: String
            )

            data class SwordShield(
                @SerializedName("name_icon")
                val nameIcon: String
            )
        }
    }
}