package com.example.completepokemondex.data.remote.api

import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz que define el servicio API para interactuar con la PokeAPI.
 *
 * Esta interfaz utiliza anotaciones de Retrofit para definir las peticiones HTTP
 * para obtener datos de Pokémon.
 *
 * Incluye métodos para obtener una lista de Pokémon y detalles de un Pokémon específico.
 * Cada método devuelve un objeto Response de Retrofit que encapsula el resultado de la operación.
 */
interface ApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetailsById(
        @Query("id") id: Int
    ): Response<PokemonDetailsDTO>
}


/**
 * Clase que representa la respuesta de la API para una lista de Pokémon
 */
data class PokemonListResponse(
    val results: List<PokemonDTO>
)