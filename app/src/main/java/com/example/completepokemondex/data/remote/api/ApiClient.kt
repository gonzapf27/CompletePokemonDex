
package com.example.completepokemondex.data.remote.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton que proporciona una instancia de la API de PokeAPI utilizando Retrofit.
 *
 * Este objeto se encarga de configurar y crear una instancia de `PokeApiService`
 * para realizar llamadas a la API de Pokémon. Utiliza OkHttp para la gestión de la conexión
 * y Gson para la conversión de los datos JSON.
 */
object ApiClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val okHttpClient = OkHttpClient.Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val pokeApiService: ApiService = retrofit.create(ApiService::class.java)
}