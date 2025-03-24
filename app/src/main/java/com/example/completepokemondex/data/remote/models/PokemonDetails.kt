package com.example.completepokemondex.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Representa la información detallada de un Pokémon.
 *
 * Esta clase de datos encapsula la respuesta de una llamada a PokeAPi que proporciona
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
data class PokemonDetailsDTO(
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

/**
 * Representa un tipo elemental de un Pokémon.
 *
 * Esta clase de datos encapsula la información sobre uno de los tipos elementales
 * que puede tener un Pokémon, incluyendo su posición (slot) y la información del tipo.
 *
 * @property slot La posición del tipo en la lista de tipos del Pokémon.
 * @property type Objeto [Type] que contiene información detallada sobre el tipo.
 */
data class PokemonType(
    @SerializedName("slot")
    val slot: Int,
    
    @SerializedName("type")
    val type: Type
)

/**
 * Representa la información básica de un tipo elemental.
 *
 * Esta clase de datos contiene el nombre del tipo elemental y la URL
 * donde se puede obtener información más detallada sobre el mismo.
 *
 * @property name El nombre del tipo elemental (ej. "fire", "water", "grass").
 * @property url La URL a la API que contiene información detallada sobre este tipo.
 */
data class Type(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("url")
    val url: String
)