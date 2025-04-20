package com.example.completepokemondex.data.remote.api

import com.example.completepokemondex.data.remote.models.AbilityDTO
import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.remote.models.PokemonEncountersDTO
import com.example.completepokemondex.data.remote.models.PokemonSpeciesDTO
import com.example.completepokemondex.data.remote.models.EvolutionChainDTO
import retrofit2.Response
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
        @Path("id") id: Int
    ): Response<PokemonDetailsDTO>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetailsByName(
        @Path("name") name: String
    ): Response<PokemonDetailsDTO>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpeciesById(
        @Path("id") id: Int
    ): Response<PokemonSpeciesDTO>

    @GET("ability/{id}")
    suspend fun getAbilityById(
        @Path("id") id: Int
    ): Response<AbilityDTO>

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChainById(
        @Path("id") id: Int
    ): Response<EvolutionChainDTO>
    
    /**
     * Obtiene los lugares donde se puede encontrar un Pokémon por su ID.
     *
     * @param id El ID del Pokémon.
     * @return Una respuesta que contiene una lista de ubicaciones donde se puede encontrar el Pokémon.
     */
    @GET("pokemon/{id}/encounters")
    suspend fun getPokemonEncountersById(
        @Path("id") id: Int
    ): Response<List<PokemonEncountersDTO>>
    
    /**
     * Obtiene los lugares donde se puede encontrar un Pokémon por su nombre.
     *
     * @param name El nombre del Pokémon.
     * @return Una respuesta que contiene una lista de ubicaciones donde se puede encontrar el Pokémon.
     */
    @GET("pokemon/{name}/encounters")
    suspend fun getPokemonEncountersByName(
        @Path("name") name: String
    ): Response<List<PokemonEncountersDTO>>
}

/**
 * Clase que representa la respuesta de la API para una lista de Pokémon
 */
data class PokemonListResponse(
    val results: List<PokemonDTO>
)