package com.example.completepokemondex.util

import com.example.completepokemondex.R

/**
 * Clase de utilidad para manejar información relacionada con tipos de Pokémon.
 */
object PokemonTypeUtil {

    /**
     * Representa un tipo de Pokémon con su nombre, recurso de color y recurso de cadena.
     */
    data class PokemonType(
        val name: String,
        val colorRes: Int,
        val stringRes: Int = 0
    )

    /**
     * Lista de todos los tipos de Pokémon disponibles.
     * Usa los recursos de color definidos en colors.xml.
     */
    val allTypes = listOf(
        PokemonType("all", R.color.type_all, R.string.type_all),
        PokemonType("normal", R.color.type_normal, R.string.type_normal),
        PokemonType("fire", R.color.type_fire, R.string.type_fire),
        PokemonType("water", R.color.type_water, R.string.type_water),
        PokemonType("electric", R.color.type_electric, R.string.type_electric),
        PokemonType("grass", R.color.type_grass, R.string.type_grass),
        PokemonType("ice", R.color.type_ice, R.string.type_ice),
        PokemonType("fighting", R.color.type_fighting, R.string.type_fighting),
        PokemonType("poison", R.color.type_poison, R.string.type_poison),
        PokemonType("ground", R.color.type_ground, R.string.type_ground),
        PokemonType("flying", R.color.type_flying, R.string.type_flying),
        PokemonType("psychic", R.color.type_psychic, R.string.type_psychic),
        PokemonType("bug", R.color.type_bug, R.string.type_bug),
        PokemonType("rock", R.color.type_rock, R.string.type_rock),
        PokemonType("ghost", R.color.type_ghost, R.string.type_ghost),
        PokemonType("dragon", R.color.type_dragon, R.string.type_dragon),
        PokemonType("dark", R.color.type_dark, R.string.type_dark),
        PokemonType("steel", R.color.type_steel, R.string.type_steel),
        PokemonType("fairy", R.color.type_fairy, R.string.type_fairy)
    )

    /**
     * Encuentra el PokemonType correspondiente al nombre del tipo proporcionado.
     *
     * @param typeName Nombre del tipo de Pokémon a buscar
     * @return PokemonType correspondiente o tipo "normal" como fallback
     */
    fun getTypeByName(typeName: String): PokemonType {
        return allTypes.find { it.name == typeName.lowercase() } ?: allTypes[1]
    }
}
