package com.example.completepokemondex.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Representa la información detallada de un Pokémon.
 *
 * Esta clase de datos encapsula la respuesta de una llamada a la API que proporciona
 * detalles específicos sobre un Pokémon en particular, como su ID, nombre, altura,
 * peso y los tipos a los que pertenece.
 *
 * @property id El identificador único del Pokémon.
 * @property name El nombre del Pokémon.
 * @property height La altura del Pokémon, típicamente en decímetros.
 * @property weight El peso del Pokémon, típicamente en hectogramos.
 * @property types Una lista de objetos [PokemonType], que representan los
 *                 tipos elementales asociados con el Pokémon.
 */
data class PokemonDetailResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,
    
    @SerializedName("height")
    val height: Int,
    
    @SerializedName("weight")
    val weight: Int,
    
    @SerializedName("types")
    val types: List<PokemonType>
)

data class PokemonType(
    @SerializedName("slot")
    val slot: Int,
    
    @SerializedName("type")
    val type: Type
)

data class Type(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("url")
    val url: String
)