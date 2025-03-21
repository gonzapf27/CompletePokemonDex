package com.example.completepokemondex.data.repository

import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.remote.models.ApiResponse
import com.example.completepokemondex.data.remote.models.PokemonResponse

class PokemonRepository {

    val remoteDataSource = PokemonRemoteDataSource()

    suspend fun getPokemonList(limit: Int, offset: Int): ApiResponse<PokemonResponse> {
        // Comprobar si existen datos en la base de datos local
        // Si existen, devolver los datos almacenados

        // Si no existen, realizar una petición a la API remota
        // Por ahora devolvemos los resultados de la API remota, pero en el futuro se almacenarán en la base de datos local
        return remoteDataSource.getPokemonList(limit, offset)
    }
}