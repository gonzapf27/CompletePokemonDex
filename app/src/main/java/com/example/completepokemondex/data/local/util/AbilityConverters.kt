package com.example.completepokemondex.data.local.util

import androidx.room.TypeConverter
import com.example.completepokemondex.data.local.entities.AbilityEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AbilityConverters {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun fromEffectChangeList(list: List<AbilityEntity.EffectChange?>?): String? =
        gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toEffectChangeList(data: String?): List<AbilityEntity.EffectChange?>? =
        gson.fromJson(data, object : TypeToken<List<AbilityEntity.EffectChange?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromEffectEntryList(list: List<AbilityEntity.EffectEntry?>?): String? =
        gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toEffectEntryList(data: String?): List<AbilityEntity.EffectEntry?>? =
        gson.fromJson(data, object : TypeToken<List<AbilityEntity.EffectEntry?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromFlavorTextEntryList(list: List<AbilityEntity.FlavorTextEntry?>?): String? =
        gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toFlavorTextEntryList(data: String?): List<AbilityEntity.FlavorTextEntry?>? =
        gson.fromJson(data, object : TypeToken<List<AbilityEntity.FlavorTextEntry?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromNameList(list: List<AbilityEntity.Name?>?): String? =
        gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toNameList(data: String?): List<AbilityEntity.Name?>? =
        gson.fromJson(data, object : TypeToken<List<AbilityEntity.Name?>?>() {}.type)

    @TypeConverter
    @JvmStatic
    fun fromPokemonList(list: List<AbilityEntity.Pokemon?>?): String? =
        gson.toJson(list)

    @TypeConverter
    @JvmStatic
    fun toPokemonList(data: String?): List<AbilityEntity.Pokemon?>? =
        gson.fromJson(data, object : TypeToken<List<AbilityEntity.Pokemon?>?>() {}.type)
}
