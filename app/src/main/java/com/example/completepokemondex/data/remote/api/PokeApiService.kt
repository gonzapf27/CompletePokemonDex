package com.example.completepokemondex.data.remote.api

import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.remote.models.PokemonListDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define el servicio API para interactuar con la PokeAPI.
 *
 * Esta interfaz utiliza anotaciones de Retrofit para definir las peticiones HTTP
 * para obtener datos de Pokémon.
 *
 * Incluye métodos para obtener una lista de Pokémon y detalles de un Pokémon específico.
 */
interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListDTO

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailsDTO
}