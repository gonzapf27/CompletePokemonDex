package com.example.completepokemondex.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Representa un Pokémon dentro de la lista de resultados de la API.
 *
 * Esta clase de datos encapsula la información básica de un Pokémon que es devuelta
 * por la PokeAPI cuando se solicita una lista de Pokémon. Contiene el nombre del Pokémon
 * y la URL donde se pueden obtener detalles específicos sobre el mismo.
 *
 * @property name El nombre del Pokémon.
 * @property url La URL que apunta a los detalles completos del Pokémon en la API.
 *             Esta URL puede ser utilizada para extraer el ID del Pokémon o para
 *             realizar peticiones adicionales para obtener información detallada.
 */
data class PokemonDTO(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("url")
    val url: String
)
