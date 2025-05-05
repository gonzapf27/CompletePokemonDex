package com.example.completepokemondex.data.local.util

import androidx.room.TypeConverter
import com.example.completepokemondex.data.local.entities.PokemonSpeciesEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PokemonSpeciesConverters {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun fromColor(color: PokemonSpeciesEntity.Color?): String? = gson.toJson(color)

    @TypeConverter
    @JvmStatic
    fun toColor(data: String?): PokemonSpeciesEntity.Color? =
        gson.fromJson(data, object : TypeToken<PokemonSpeciesEntity.Color?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromEggGroupList(list: List<PokemonSpeciesEntity.EggGroup?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toEggGroupList(data: String?): List<PokemonSpeciesEntity.EggGroup?>? =
        gson.fromJson(data, object : TypeToken<List<PokemonSpeciesEntity.EggGroup?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromEvolutionChain(chain: PokemonSpeciesEntity.EvolutionChain?): String? = gson.toJson(chain)

    @TypeConverter
    @JvmStatic
    fun toEvolutionChain(data: String?): PokemonSpeciesEntity.EvolutionChain? =
        gson.fromJson(data, object : TypeToken<PokemonSpeciesEntity.EvolutionChain?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromEvolvesFromSpecies(evolves: PokemonSpeciesEntity.EvolvesFromSpecies?): String? = gson.toJson(evolves)

    @TypeConverter
    @JvmStatic
    fun toEvolvesFromSpecies(data: String?): PokemonSpeciesEntity.EvolvesFromSpecies? =
        gson.fromJson(data, object : TypeToken<PokemonSpeciesEntity.EvolvesFromSpecies?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromFlavorTextEntryList(list: List<PokemonSpeciesEntity.FlavorTextEntry?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toFlavorTextEntryList(data: String?): List<PokemonSpeciesEntity.FlavorTextEntry?>? =
        gson.fromJson(data, object : TypeToken<List<PokemonSpeciesEntity.FlavorTextEntry?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromFormDescriptions(list: List<Any?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toFormDescriptions(data: String?): List<Any?>? =
        gson.fromJson(data, object : TypeToken<List<Any?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromGeneraList(list: List<PokemonSpeciesEntity.Genera?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toGeneraList(data: String?): List<PokemonSpeciesEntity.Genera?>? =
        gson.fromJson(data, object : TypeToken<List<PokemonSpeciesEntity.Genera?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromGeneration(gen: PokemonSpeciesEntity.Generation?): String? = gson.toJson(gen)

    @TypeConverter
    @JvmStatic
    fun toGeneration(data: String?): PokemonSpeciesEntity.Generation? =
        gson.fromJson(data, object : TypeToken<PokemonSpeciesEntity.Generation?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromGrowthRate(rate: PokemonSpeciesEntity.GrowthRate?): String? = gson.toJson(rate)

    @TypeConverter
    @JvmStatic
    fun toGrowthRate(data: String?): PokemonSpeciesEntity.GrowthRate? =
        gson.fromJson(data, object : TypeToken<PokemonSpeciesEntity.GrowthRate?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromHabitat(habitat: PokemonSpeciesEntity.Habitat?): String? = gson.toJson(habitat)

    @TypeConverter
    @JvmStatic
    fun toHabitat(data: String?): PokemonSpeciesEntity.Habitat? =
        gson.fromJson(data, object : TypeToken<PokemonSpeciesEntity.Habitat?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromNameList(list: List<PokemonSpeciesEntity.Name?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toNameList(data: String?): List<PokemonSpeciesEntity.Name?>? =
        gson.fromJson(data, object : TypeToken<List<PokemonSpeciesEntity.Name?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromPalParkEncounterList(list: List<PokemonSpeciesEntity.PalParkEncounter?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toPalParkEncounterList(data: String?): List<PokemonSpeciesEntity.PalParkEncounter?>? =
        gson.fromJson(data, object : TypeToken<List<PokemonSpeciesEntity.PalParkEncounter?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromPokedexNumberList(list: List<PokemonSpeciesEntity.PokedexNumber?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toPokedexNumberList(data: String?): List<PokemonSpeciesEntity.PokedexNumber?>? =
        gson.fromJson(data, object : TypeToken<List<PokemonSpeciesEntity.PokedexNumber?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromShape(shape: PokemonSpeciesEntity.Shape?): String? = gson.toJson(shape)

    @TypeConverter
    @JvmStatic
    fun toShape(data: String?): PokemonSpeciesEntity.Shape? =
        gson.fromJson(data, object : TypeToken<PokemonSpeciesEntity.Shape?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromVarietyList(list: List<PokemonSpeciesEntity.Variety?>?): String? = gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toVarietyList(data: String?): List<PokemonSpeciesEntity.Variety?>? =
        gson.fromJson(data, object : TypeToken<List<PokemonSpeciesEntity.Variety?>?>() {}.type)
}
