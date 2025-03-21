package com.example.completepokemondex.core

import com.example.completepokemondex.data.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Servicio de API para obtener información de Pokémon.
 */
interface PokeApiService {

    /**
     * Obtiene la información de un Pokémon por su nombre.
     *
     * @param name El nombre del Pokémon.
     * @return Un objeto `Call` que se puede usar para realizar la solicitud HTTP.
     */
    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name: String): Call<Pokemon>
}