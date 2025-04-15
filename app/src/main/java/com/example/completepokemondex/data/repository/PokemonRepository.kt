package com.example.completepokemondex.data.repository

import android.util.Log
import com.example.completepokemondex.data.mapping.PokemonDTOToEntityList
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.mapping.pokemonDTOToDomainList
import com.example.completepokemondex.data.mapping.pokemonDetailsDTOToDomain
import com.example.completepokemondex.data.mapping.pokemonDetailsDTOToEntity
import com.example.completepokemondex.data.mapping.pokemonDetailsEntityToDomain
import com.example.completepokemondex.data.mapping.pokemonEntityToDomainList
import com.example.completepokemondex.data.mapping.pokemonSpeciesDTOToDomain
import com.example.completepokemondex.data.mapping.pokemonSpeciesDTOToEntity
import com.example.completepokemondex.data.mapping.pokemonSpeciesEntityToDomain
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.dao.PokemonDetailsDao
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
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
    private val pokemonDetailsDao: PokemonDetailsDao,
    private val pokemonSpeciesDao: PokemonSpeciesDao,
    private val remoteDataSource: PokemonRemoteDataSource,
) {
    private val tag = "PokemonRepository"

    /**
     * Obtiene una lista de Pokémon. Primero intenta obtener los datos de la base de datos local. Si
     * la base de datos está vacía, obtiene los datos de la API y los guarda en la base de datos.
     *
     * @param limit Número máximo de Pokémon a obtener
     * @param offset Posición desde donde empezar a obtener Pokémon
     * @return Flow que emite la lista de Pokémon y el estado de carga
     */
    fun getPokemonList(limit: Int, offset: Int): Flow<Resource<List<PokemonDomain>>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de Pokémon [limit=$limit, offset=$offset]")

            try {
                // Intentar obtener datos de la base de datos local
                logDebug("Consultando base de datos local...")
                val localPokemon = pokemonDao.getAllPokemon()
                logDebug("Base de datos local contiene ${localPokemon.size} Pokémon")

                // Si hay datos suficientes en la base de datos local, devolverlos
                if (localPokemon.isNotEmpty() && localPokemon.size >= offset + limit) {
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Datos suficientes en DB local para offset=$offset y limit=$limit")

                    val resultList = localPokemon.drop(offset).take(limit)
                    logDebug("Devolviendo ${resultList.size} Pokémon de la base de datos local")

                    emit(Resource.Success(resultList.pokemonEntityToDomainList()))
                } else {
                    // Si no hay suficientes datos, obtenerlos de la API
                    logDebug("Datos insuficientes en DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getPokemonList(limit, offset) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibidos ${apiResponse.size} Pokémon de la API")

                            // Guardar en la base de datos local
                            saveToDatabase {
                                logDebug("Insertando ${apiResponse.size} Pokémon en la base de datos")
                                pokemonDao.insertAllPokemon(apiResponse.PokemonDTOToEntityList())
                            }

                            logDebug("Devolviendo ${apiResponse.size} Pokémon obtenidos de la API")
                            emit(Resource.Success(apiResponse.pokemonDTOToDomainList()))
                        },
                        onError = { errorMessage ->
                            handleLocalFallback(
                                errorMessage = errorMessage,
                                localData = localPokemon,
                                transformData = { it.pokemonEntityToDomainList() },
                                emitResult = { data, message ->
                                    emit(Resource.Error(message = message, data = data))
                                }
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener lista de Pokémon: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}", data = emptyList()))
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Obtiene los detalles de un Pokémon específico por su ID. Primero intenta obtener los datos de la
     * base de datos local. Si no los encuentra, los obtiene de la API y los guarda en la base de datos.
     *
     * @param id Identificador único del Pokémon
     * @return Flow que emite los detalles del Pokémon y el estado de carga
     */
    fun getPokemonDetailsById(id: Int): Flow<Resource<PokemonDetailsDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de detalles del Pokémon [id=$id]")

            try {
                // Intentar obtener datos de la base de datos local
                logDebug("Consultando base de datos local para detalles del Pokémon $id...")
                val localPokemonDetails = pokemonDetailsDao.getPokemonById(id)

                if (localPokemonDetails != null) {
                    // Si hay datos en la base de datos local, devolverlos
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo detalles del Pokémon $id de la base de datos local")

                    emit(Resource.Success(localPokemonDetails.pokemonDetailsEntityToDomain()))
                } else {
                    // Si no hay datos en la base de datos local, obtenerlos de la API
                    logDebug("No se encontraron detalles del Pokémon $id en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getPokemonDetailsById(id) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibidos detalles del Pokémon $id de la API")
                            logDebug("Datos del pokemon en la API: $apiResponse")

                            // Guardar en la base de datos local
                            saveToDatabase {
                                logDebug("Insertando detalles del Pokémon $id en la base de datos")
                                pokemonDetailsDao.insertPokemonDetails(apiResponse.pokemonDetailsDTOToEntity())
                            }

                            logDebug("Devolviendo detalles del Pokémon $id obtenidos de la API")
                            emit(Resource.Success(apiResponse.pokemonDetailsDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener detalles del Pokémon $id de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener detalles del Pokémon: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Obtiene la especie de un Pokémon por su ID.
     * Primero intenta obtener los datos de la base de datos local. Si no los encuentra, los obtiene de la API y los guarda en la base de datos.
     *
     * @param id Identificador único de la especie Pokémon
     * @return Flow que emite la especie del Pokémon y el estado de carga
     */
    fun getPokemonSpeciesById(id: Int): Flow<Resource<PokemonSpeciesDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de species del Pokémon [id=$id]")

            try {
                // Intentar obtener datos de la base de datos local
                logDebug("Consultando base de datos local para species del Pokémon $id...")
                val localSpecies = pokemonSpeciesDao.getPokemonSpeciesById(id)

                if (localSpecies != null) {
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo species del Pokémon $id de la base de datos local")
                    emit(Resource.Success(localSpecies.pokemonSpeciesEntityToDomain()))
                } else {
                    logDebug("No se encontró species del Pokémon $id en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getPokemonSpeciesById(id) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibida species del Pokémon $id de la API")
                            saveToDatabase {
                                logDebug("Insertando species del Pokémon $id en la base de datos")
                                pokemonSpeciesDao.insertPokemonSpecies(apiResponse.pokemonSpeciesDTOToEntity())
                            }
                            logDebug("Devolviendo species del Pokémon $id obtenida de la API")
                            emit(Resource.Success(apiResponse.pokemonSpeciesDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener species del Pokémon $id de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener species del Pokémon: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)

    // Funciones auxiliares para evitar código repetido

    private suspend inline fun <T> handleApiResponse(
        crossinline apiCall: suspend () -> Resource<T>,
        crossinline onSuccess: suspend (T) -> Unit,
        crossinline onError: suspend (String) -> Unit
    ) {
        when (val response = apiCall()) {
            is Resource.Success -> onSuccess(response.data)
            is Resource.Error -> onError(response.message)
            is Resource.Loading -> { /* No action needed, already in loading state */
            }
        }
    }

    private suspend inline fun <T, R> handleLocalFallback(
        errorMessage: String,
        localData: T,
        transformData: (T) -> R,
        emitResult: (R, String) -> Unit
    ) {
        if (localData is Collection<*> && !localData.isNullOrEmpty() || localData != null) {
            logDebug("Devolviendo datos locales con un mensaje de error por la API")
            logError("Error al obtener datos de la API: $errorMessage")
            emitResult(transformData(localData), errorMessage)
        } else {
            logDebug("No hay datos en la base de datos local - devolviendo el error de la API")
            logError("Error al obtener datos de la API: $errorMessage")
            @Suppress("UNCHECKED_CAST")
            when (transformData(null as T)) {
                is List<*> -> emitResult(emptyList<Any>() as R, errorMessage)
                else -> emitResult(null as R, errorMessage)
            }
        }
    }

    private suspend inline fun saveToDatabase(crossinline block: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            logDebug("💾 GUARDANDO DATOS: API → BASE DE DATOS LOCAL")
            block()
            logDebug("Datos guardados correctamente en la base de datos local")
        }
    }

    // Funciones de logging para mantener consistencia
    private fun logDebug(message: String) {
        Log.d(tag, message)
    }

    private fun logError(message: String) {
        Log.e(tag, message)
    }

    /**
     * Actualiza el estado de favorito de un Pokémon en la base de datos local.
     *
     * @param pokemonId Identificador único del Pokémon
     * @param isFavorite Estado de favorito a establecer
     */
    suspend fun updatePokemonFavorite(pokemonId: Int, isFavorite: Boolean) {
        logDebug("Actualizando estado de favorito del Pokémon $pokemonId a $isFavorite")
        pokemonDao.updatePokemonFavorite(pokemonId, isFavorite)
        logDebug("Estado de favorito actualizado correctamente")
    }

    /**
     * Devuelve si un Pokémon es favorito.
     */
    suspend fun isPokemonFavorite(pokemonId: Int): Boolean {
        val entity = pokemonDao.getPokemonById(pokemonId)
        return entity?.favorite ?: false
    }
}
