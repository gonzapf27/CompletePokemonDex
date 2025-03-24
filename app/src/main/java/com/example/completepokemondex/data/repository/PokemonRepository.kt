package com.example.completepokemondex.data.repository

import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.remote.models.ApiResponse
import com.example.completepokemondex.domain.model.PokemonDomain
import com.example.completepokemondex.domain.toDomainModelList
import com.example.completepokemondex.domain.toEntityDomainModelList
import com.example.completepokemondex.domain.toEntityList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Repositorio para acceder a los datos de Pokémon. Implementa el patrón repositorio para abstraer
 * las fuentes de datos. Primero busca en la base de datos local, y si no encuentra los datos o
 * están obsoletos, consulta la API y guarda los resultados en la base de datos.
 *
 * @property pokemonDao DAO para acceder a la base de datos local
 * @property remoteDataSource Fuente de datos remota para obtener datos de la API de Pokémon
 */
class PokemonRepository(
        private val pokemonDao: PokemonDao,
        private val remoteDataSource: PokemonRemoteDataSource
) {
    /**
     * Obtiene una lista de Pokémon. Primero intenta obtener los datos de la base de datos local. Si
     * la base de datos está vacía, obtiene los datos de la API y los guarda en la base de datos.
     *
     * @param limit Número máximo de Pokémon a obtener
     * @param offset Posición desde donde empezar a obtener Pokémon
     * @return Flow que emite la lista de Pokémon y el estado de carga
     */
    fun getPokemonList(limit: Int, offset: Int): Flow<ApiResponse<List<PokemonDomain>>> =
            flow {
                        // Emitir estado de carga
                        emit(ApiResponse.Loading)

                        try {
                            // Intentar obtener datos de la base de datos local
                            val localPokemon = pokemonDao.getAllPokemon()

                            // Si hay datos en la base de datos local y son suficientes, devolverlos
                            if (localPokemon.isNotEmpty() && localPokemon.size >= offset + limit) {
                                emit(
                                        ApiResponse.Success(
                                                localPokemon
                                                        .drop(offset)
                                                        .take(limit)
                                                        .toEntityDomainModelList()
                                        )
                                )
                            } else {
                                // Si no hay suficientes datos en la base de datos local, obtenerlos
                                // de la API
                                when (val apiResponse =
                                                remoteDataSource.getPokemonList(limit, offset)
                                ) {
                                    is ApiResponse.Success -> {
                                        // Guardar los datos en la base de datos local
                                        withContext(Dispatchers.IO) {
                                            pokemonDao.insertAllPokemon(
                                                    apiResponse.data.toEntityList()
                                            )
                                        }

                                        // Devolver los datos obtenidos de la API
                                        emit(
                                                ApiResponse.Success(
                                                        apiResponse.data.toDomainModelList()
                                                )
                                        )
                                    }
                                    is ApiResponse.Error -> {
                                        // Si hay un error al obtener los datos de la API, pero hay
                                        // algunos datos en la base de datos local,
                                        // devolver los datos disponibles con un mensaje de error
                                        if (localPokemon.isNotEmpty()) {
                                            emit(
                                                    ApiResponse.Error(
                                                            message = apiResponse.message,
                                                            data = localPokemon.toEntityDomainModelList()
                                                    )
                                            )
                                        } else {
                                            // Si no hay datos en la base de datos local, devolver
                                            // el error
                                            emit(
                                                    ApiResponse.Error(
                                                            message = apiResponse.message,
                                                            data = emptyList()
                                                    )
                                            )
                                        }
                                    }
                                    is ApiResponse.Loading -> {
                                        // Mantener el estado de carga
                                        emit(ApiResponse.Loading)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Manejar cualquier excepción no controladay
                            emit(
                                    ApiResponse.Error(
                                            message = "Error: ${e.message}",
                                            data = emptyList()
                                    )
                            )
                        }
                    }
                    .flowOn(Dispatchers.IO)
}
