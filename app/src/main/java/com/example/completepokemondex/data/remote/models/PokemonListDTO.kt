package com.example.completepokemondex.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta de la API para una lista de Pokémon.
 *
 * Esta clase de datos modela la estructura de la respuesta JSON recibida desde un
 * endpoint de la API de Pokémon que proporciona una lista paginada de Pokémon.
 * Contiene información sobre el número total de Pokémon disponibles, enlaces para
 * navegar a través de las páginas (siguiente/anterior), y la lista de Pokémon
 * recuperados en la página de respuesta actual.
 *
 * @property count El número total de Pokémon disponibles en todo el conjunto de datos.
 * @property next La URL para obtener la siguiente página de resultados de Pokémon.
 *               Si es `null`, no hay más páginas después de esta.
 * @property previous La URL para obtener la página anterior de resultados de Pokémon.
 *                   Si es `null`, esta es la primera página.
 * @property results Una lista de objetos [PokemonDTO], cada uno representando una
 *                  entrada de Pokémon individual en la página actual de resultados.
 *
 * Ejemplo de estructura JSON:
 * ```json
 * {
 *   "count": 1281,
 *   "next": "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
 *   "previous": null,
 *   "results": [
 *     {"name": "bulbasaur", "url": "https://pokeapi.co/api/v2/pokemon/1/"},
 *     {"name": "ivysaur", "url": "https://pokeapi.co/api/v2/pokemon/2/"},
 *     // ... más resultados de Pokémon
 *   ]
 * }
 * ```
 */// Clase principal que representa la respuesta de la API para la lista de Pokémon
data class PokemonListDTO(
    @SerializedName("count")
    val count: Int, // Total de Pokémon obtenidos

    @SerializedName("next")
    val next: String?, // URL de la siguiente página, si existe

    @SerializedName("previous")
    val previous: String?, // URL de la página anterior, si existe

    @SerializedName("results")
    val results: List<PokemonDTO> // Lista de Pokémon obtenidos en esta página
)

