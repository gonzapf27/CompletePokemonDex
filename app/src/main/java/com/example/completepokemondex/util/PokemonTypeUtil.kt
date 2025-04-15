package com.example.completepokemondex.util

import android.graphics.Color
import com.example.completepokemondex.R

/**
 * Clase de utilidad para manejar información relacionada con tipos de Pokémon.
 */
object PokemonTypeUtil {
    
    /**
     * Representa un tipo de Pokémon con su nombre, color y recurso de cadena.
     */
    data class PokemonType(
        val name: String,
        val color: Int,
        val stringRes: Int = 0
    )
    
    /**
     * Lista de todos los tipos de Pokémon disponibles.
     */
    val allTypes = listOf(
        PokemonType("all", Color.GRAY, R.string.type_all),
        PokemonType("normal", Color.parseColor("#A8A878"), R.string.type_normal),
        PokemonType("fire", Color.parseColor("#F08030"), R.string.type_fire),
        PokemonType("water", Color.parseColor("#6890F0"), R.string.type_water),
        PokemonType("electric", Color.parseColor("#F8D030"), R.string.type_electric),
        PokemonType("grass", Color.parseColor("#78C850"), R.string.type_grass),
        PokemonType("ice", Color.parseColor("#98D8D8"), R.string.type_ice),
        PokemonType("fighting", Color.parseColor("#C03028"), R.string.type_fighting),
        PokemonType("poison", Color.parseColor("#A040A0"), R.string.type_poison),
        PokemonType("ground", Color.parseColor("#E0C068"), R.string.type_ground),
        PokemonType("flying", Color.parseColor("#A890F0"), R.string.type_flying),
        PokemonType("psychic", Color.parseColor("#F85888"), R.string.type_psychic),
        PokemonType("bug", Color.parseColor("#A8B820"), R.string.type_bug),
        PokemonType("rock", Color.parseColor("#B8A038"), R.string.type_rock),
        PokemonType("ghost", Color.parseColor("#705898"), R.string.type_ghost),
        PokemonType("dragon", Color.parseColor("#7038F8"), R.string.type_dragon),
        PokemonType("dark", Color.parseColor("#705848"), R.string.type_dark),
        PokemonType("steel", Color.parseColor("#B8B8D0"), R.string.type_steel),
        PokemonType("fairy", Color.parseColor("#EE99AC"), R.string.type_fairy)
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
