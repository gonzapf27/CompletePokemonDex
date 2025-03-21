package com.example.completepokemondex.data.local.converters

import androidx.room.TypeConverter
import com.example.completepokemondex.data.local.entities.OtherSprites
import com.example.completepokemondex.data.local.entities.VersionSprites
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Clase de conversores tipo para Room que maneja la serialización y deserialización
 * de objetos complejos de sprites utilizados en la base de datos local.
 */
class SpritesTypeConverters {

    private val gson = Gson()

    /**
     * Convierte un objeto OtherSprites a una cadena JSON para almacenamiento en la base de datos.
     *
     * @param otherSprites El objeto OtherSprites que será convertido.
     * @return Una representación en cadena JSON del objeto, o null si el objeto de entrada es null.
     */
    @TypeConverter
    fun fromOtherSprites(otherSprites: OtherSprites?): String? {
        return gson.toJson(otherSprites)
    }

    /**
     * Convierte una cadena JSON a un objeto OtherSprites.
     *
     * @param data La cadena JSON que será convertida.
     * @return El objeto OtherSprites reconstruido, o null si la cadena de entrada es null.
     */
    @TypeConverter
    fun toOtherSprites(data: String?): OtherSprites? {
        if (data == null) {
            return null
        }
        val type = object : TypeToken<OtherSprites>() {}.type
        return gson.fromJson(data, type)
    }

    /**
     * Convierte un objeto VersionSprites a una cadena JSON para almacenamiento en la base de datos.
     *
     * @param versionSprites El objeto VersionSprites que será convertido.
     * @return Una representación en cadena JSON del objeto, o null si el objeto de entrada es null.
     */
    @TypeConverter
    fun fromVersionSprites(versionSprites: VersionSprites?): String? {
        return gson.toJson(versionSprites)
    }

    /**
     * Convierte una cadena JSON a un objeto VersionSprites.
     *
     * @param data La cadena JSON que será convertida.
     * @return El objeto VersionSprites reconstruido, o null si la cadena de entrada es null.
     */
    @TypeConverter
    fun toVersionSprites(data: String?): VersionSprites? {
        if (data == null) {
            return null
        }
        val type = object : TypeToken<VersionSprites>() {}.type
        return gson.fromJson(data, type)
    }
}