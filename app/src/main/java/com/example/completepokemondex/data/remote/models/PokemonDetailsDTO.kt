package com.example.completepokemondex.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) que representa la información detallada de un Pokémon obtenida de la API.
 *
 * Esta clase encapsula los detalles de un Pokémon que son devueltos por la PokeAPI
 * cuando se solicita información específica de un Pokémon. Contiene atributos como
 * id, nombre, altura y peso del Pokémon.
 *
 * @property id Identificador único del Pokémon.
 * @property name Nombre del Pokémon.
 * @property height Altura del Pokémon (en decímetros).
 * @property weight Peso del Pokémon (en hectogramos).
 */
data class PokemonDetailsDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,
    
    @SerializedName("height")
    val height: Int,

    @SerializedName("weight")
    val weight: Int
)
