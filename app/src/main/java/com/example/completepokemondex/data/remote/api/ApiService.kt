package com.example.completepokemondex.data.remote.api

import com.example.completepokemondex.data.remote.models.AbilityDTO
import com.example.completepokemondex.data.remote.models.EvolutionChainDTO
import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.remote.models.PokemonEncountersDTO
import com.example.completepokemondex.data.remote.models.PokemonMoveDTO
import com.example.completepokemondex.data.remote.models.PokemonSpeciesDTO
import com.example.completepokemondex.data.remote.models.TypeDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define el servicio API para interactuar con la PokeAPI.
 *
 * Esta interfaz utiliza Retrofit para definir las peticiones HTTP necesarias para obtener
 * información de Pokémon, especies, habilidades, cadenas de evolución, encuentros y movimientos.
 * Cada método corresponde a un endpoint de la API y devuelve un objeto Response de Retrofit.
 */
interface ApiService {
    /**
     * Obtiene una lista de Pokémon con paginación.
     *
     * @param limit Número máximo de Pokémon a obtener.
     * @param offset Desplazamiento para la paginación.
     * @return Respuesta con la lista de Pokémon.
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>

    /**
     * Obtiene los detalles de un Pokémon por su ID.
     *
     * @param id ID del Pokémon.
     * @return Respuesta con los detalles del Pokémon.
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonDetailsById(
        @Path("id") id: Int
    ): Response<PokemonDetailsDTO>

    /**
     * Obtiene los detalles de un Pokémon por su nombre.
     *
     * @param name Nombre del Pokémon.
     * @return Respuesta con los detalles del Pokémon.
     */
    @GET("pokemon/{name}")
    suspend fun getPokemonDetailsByName(
        @Path("name") name: String
    ): Response<PokemonDetailsDTO>

    /**
     * Obtiene la especie de un Pokémon por su ID.
     *
     * @param id ID de la especie.
     * @return Respuesta con los datos de la especie.
     */
    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpeciesById(
        @Path("id") id: Int
    ): Response<PokemonSpeciesDTO>

    /**
     * Obtiene una habilidad por su ID.
     *
     * @param id ID de la habilidad.
     * @return Respuesta con los datos de la habilidad.
     */
    @GET("ability/{id}")
    suspend fun getAbilityById(
        @Path("id") id: Int
    ): Response<AbilityDTO>

    /**
     * Obtiene la cadena de evolución por su ID.
     *
     * @param id ID de la cadena de evolución.
     * @return Respuesta con los datos de la cadena de evolución.
     */
    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChainById(
        @Path("id") id: Int
    ): Response<EvolutionChainDTO>

    /**
     * Obtiene los lugares donde se puede encontrar un Pokémon por su ID.
     *
     * @param id ID del Pokémon.
     * @return Respuesta con la lista de ubicaciones de encuentro.
     */
    @GET("pokemon/{id}/encounters")
    suspend fun getPokemonEncountersById(
        @Path("id") id: Int
    ): Response<List<PokemonEncountersDTO>>

    /**
     * Obtiene los lugares donde se puede encontrar un Pokémon por su nombre.
     *
     * @param name Nombre del Pokémon.
     * @return Respuesta con la lista de ubicaciones de encuentro.
     */
    @GET("pokemon/{name}/encounters")
    suspend fun getPokemonEncountersByName(
        @Path("name") name: String
    ): Response<List<PokemonEncountersDTO>>

    /**
     * Obtiene un movimiento dado su ID.
     *
     * @param id ID del movimiento.
     * @return Respuesta con los datos del movimiento.
     */
    @GET("move/{id}")
    suspend fun getMoveById(
        @Path("id") id: Int
    ): Response<PokemonMoveDTO>

    /**
     * Obtiene un tipo por su ID.
     *
     * @param id ID del tipo.
     * @return Respuesta con los datos del tipo.
     */
    @GET("type/{id}")
    suspend fun getTypeById(
        @Path("id") id: Int
    ): Response<TypeDTO>

    /**
     * Obtiene un tipo por su nombre.
     *
     * @param id ID del tipo.
     * @return Respuesta con los datos del tipo.
     */
    @GET("type/{name}")
    suspend fun getTypeByName(
        @Path("name") name: String
    ): Response<TypeDTO>
}

/**
 * Clase que representa la respuesta de la API para una lista de Pokémon.
 *
 * @property results Lista de objetos PokémonDTO.
 */
data class PokemonListResponse(
    val results: List<PokemonDTO>
)
