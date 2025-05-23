package com.example.completepokemondex.data.repository

import android.util.Log
import com.example.completepokemondex.data.mapping.pokemonDTOToEntityList
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.example.completepokemondex.data.domain.model.PokemonEncountersDomain
import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.domain.model.AbilityDomain
import com.example.completepokemondex.data.domain.model.EvolutionChainDomain
import com.example.completepokemondex.data.domain.model.PokemonMoveDomain
import com.example.completepokemondex.data.domain.model.TypeDomain
import com.example.completepokemondex.data.local.dao.PokemonEncountersDao
import com.example.completepokemondex.data.mapping.pokemonDTOToDomainList
import com.example.completepokemondex.data.mapping.pokemonDetailsDTOToDomain
import com.example.completepokemondex.data.mapping.pokemonDetailsDTOToEntity
import com.example.completepokemondex.data.mapping.pokemonDetailsEntityToDomain
import com.example.completepokemondex.data.mapping.pokemonEntityToDomainList
import com.example.completepokemondex.data.mapping.pokemonSpeciesDTOToDomain
import com.example.completepokemondex.data.mapping.pokemonSpeciesDTOToEntity
import com.example.completepokemondex.data.mapping.pokemonSpeciesEntityToDomain
import com.example.completepokemondex.data.mapping.toEntity as pokemonEncountersDTOToEntity
import com.example.completepokemondex.data.mapping.toDomain as pokemonEncountersEntityToDomain
import com.example.completepokemondex.data.mapping.toDomain as pokemonEncountersDTOToDomain
import com.example.completepokemondex.data.mapping.toDomain as abilityDTOToDomain
import com.example.completepokemondex.data.mapping.toEntity as abilityDTOToEntity
import com.example.completepokemondex.data.mapping.toDomain as abilityEntityToDomain
import com.example.completepokemondex.data.mapping.toDomain as evolutionChainDTOToDomain
import com.example.completepokemondex.data.mapping.toEntity as evolutionChainDTOToEntity
import com.example.completepokemondex.data.mapping.toDomain as evolutionChainEntityToDomain
import com.example.completepokemondex.data.mapping.toDomain as pokemonMoveDTOToDomain
import com.example.completepokemondex.data.mapping.toEntity as pokemonMoveDTOToEntity
import com.example.completepokemondex.data.mapping.toDomain as pokemonMoveEntityToDomain
import com.example.completepokemondex.data.mapping.toDomain as typeEntityToDomain
import com.example.completepokemondex.data.mapping.toEntity as typeDTOToEntity
import com.example.completepokemondex.data.mapping.toDomain as typeDTOToDomain
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.dao.PokemonDetailsDao
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
import com.example.completepokemondex.data.local.dao.AbilityDao
import com.example.completepokemondex.data.local.dao.EvolutionChainDao
import com.example.completepokemondex.data.local.dao.PokemonMoveDao
import com.example.completepokemondex.data.local.dao.TypeDao
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Repositorio para acceder y gestionar los datos de Pokémon.
 * Implementa el patrón repositorio para abstraer las fuentes de datos (local y remota).
 * Prioriza la obtención de datos desde la base de datos local y, en caso de no encontrarlos o estar desactualizados,
 * consulta la API remota y almacena los resultados en la base de datos.
 *
 * Proporciona métodos para obtener listas de Pokémon, detalles, especies, habilidades, cadenas de evolución,
 * movimientos, encuentros y gestionar favoritos.
 *
 * @property pokemonDao DAO para acceder a la base de datos local de Pokémon
 * @property pokemonDetailsDao DAO para detalles de Pokémon
 * @property pokemonSpeciesDao DAO para especies de Pokémon
 * @property abilityDao DAO para habilidades de Pokémon
 * @property remoteDataSource Fuente de datos remota (API)
 * @property evolutionChainDao DAO para cadenas de evolución
 * @property pokemonEncountersDao DAO para encuentros de Pokémon
 * @property pokemonMoveDao DAO para movimientos de Pokémon
 * @property typeDao DAO para tipos de Pokémon
 */
class PokemonRepository(
    private val pokemonDao: PokemonDao,
    private val pokemonDetailsDao: PokemonDetailsDao,
    private val pokemonSpeciesDao: PokemonSpeciesDao,
    private val abilityDao: AbilityDao,
    private val remoteDataSource: PokemonRemoteDataSource,
    private val evolutionChainDao: EvolutionChainDao,
    private val pokemonEncountersDao: PokemonEncountersDao,
    private val pokemonMoveDao: PokemonMoveDao,
    private val typeDao: TypeDao
) {
    private val tag = "PokemonRepository"

    /**
     * Obtiene una lista de Pokémon, priorizando la base de datos local.
     * Si no hay suficientes datos locales, consulta la API y guarda los resultados.
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
                                pokemonDao.insertAllPokemon(apiResponse.pokemonDTOToEntityList())
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
     * Obtiene los detalles de un Pokémon específico por su ID.
     * Prioriza la base de datos local y, si no encuentra datos, consulta la API.
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
     * Prioriza la base de datos local y, si no encuentra datos, consulta la API.
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

    /**
     * Obtiene una habilidad por su ID, usando caché local y API remota.
     *
     * @param id Identificador único de la habilidad
     * @return Flow que emite la habilidad y el estado de carga
     */
    fun getAbilityById(id: Int): Flow<Resource<AbilityDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de habilidad [id=$id]")

            try {
                // Consultar caché local
                logDebug("Consultando base de datos local para habilidad $id...")
                val localAbility = abilityDao.getAbilityById(id)

                if (localAbility != null) {
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo habilidad $id de la base de datos local")
                    emit(Resource.Success(localAbility.abilityEntityToDomain()))
                } else {
                    logDebug("No se encontró habilidad $id en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getAbilityById(id) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibida habilidad $id de la API")
                            saveToDatabase {
                                logDebug("Insertando habilidad $id en la base de datos")
                                abilityDao.insertAbility(apiResponse.abilityDTOToEntity())
                            }
                            logDebug("Devolviendo habilidad $id obtenida de la API")
                            emit(Resource.Success(apiResponse.abilityDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener habilidad $id de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener habilidad: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Obtiene la cadena de evolución por su ID, usando caché local y API remota.
     *
     * @param id Identificador único de la evolution chain
     * @return Flow que emite la evolution chain y el estado de carga
     */
    fun getEvolutionChainById(id: Int): Flow<Resource<EvolutionChainDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de evolution chain [id=$id]")

            try {
                // Consultar caché local
                logDebug("Consultando base de datos local para evolution chain $id...")
                val localChain = evolutionChainDao.getEvolutionChainById(id)

                if (localChain != null) {
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo evolution chain $id de la base de datos local")
                    emit(Resource.Success(localChain.evolutionChainEntityToDomain()))
                } else {
                    logDebug("No se encontró evolution chain $id en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getEvolutionChainById(id) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibida evolution chain $id de la API")
                            saveToDatabase {
                                logDebug("Insertando evolution chain $id en la base de datos")
                                evolutionChainDao.insertEvolutionChain(apiResponse.evolutionChainDTOToEntity())
                            }
                            logDebug("Devolviendo evolution chain $id obtenida de la API")
                            emit(Resource.Success(apiResponse.evolutionChainDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener evolution chain $id de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener evolution chain: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Obtiene un movimiento específico por su ID.
     * Prioriza la base de datos local y, si no encuentra datos, consulta la API.
     *
     * @param id Identificador único del movimiento
     * @return Flow que emite los detalles del movimiento y el estado de carga
     */
    fun getMoveById(id: Int): Flow<Resource<PokemonMoveDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de movimiento [id=$id]")

            try {
                // Intentar obtener datos de la base de datos local
                logDebug("Consultando base de datos local para movimiento $id...")
                val localMove = pokemonMoveDao.getMoveById(id)

                if (localMove != null) {
                    // Si hay datos en la base de datos local, devolverlos
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo movimiento $id de la base de datos local")

                    emit(Resource.Success(localMove.pokemonMoveEntityToDomain()))
                } else {
                    // Si no hay datos en la base de datos local, obtenerlos de la API
                    logDebug("No se encontró movimiento $id en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getMoveById(id) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibido movimiento $id de la API")
                            
                            // Guardar en la base de datos local
                            saveToDatabase {
                                logDebug("Insertando movimiento $id en la base de datos")
                                pokemonMoveDao.insertMove(apiResponse.pokemonMoveDTOToEntity())
                            }

                            logDebug("Devolviendo movimiento $id obtenido de la API")
                            emit(Resource.Success(apiResponse.pokemonMoveDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener movimiento $id de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener movimiento: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Obtiene un tipo por su ID, usando caché local y API remota.
     *
     * @param id Identificador único del tipo
     * @return Flow que emite el tipo y el estado de carga
     */
    fun getTypeById(id: Int): Flow<Resource<TypeDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de tipo [id=$id]")

            try {
                // Consultar caché local
                logDebug("Consultando base de datos local para tipo $id...")
                val localType = typeDao.getTypeById(id)

                if (localType != null) {
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo tipo $id de la base de datos local")
                    emit(Resource.Success(localType.typeEntityToDomain()))
                } else {
                    logDebug("No se encontró tipo $id en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getTypeById(id) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibido tipo $id de la API")
                            saveToDatabase {
                                logDebug("Insertando tipo $id en la base de datos")
                                typeDao.insertType(apiResponse.typeDTOToEntity())
                            }
                            logDebug("Devolviendo tipo $id obtenido de la API")
                            emit(Resource.Success(apiResponse.typeDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener tipo $id de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener tipo: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)


    /**
     * Obtiene un tipo por su nombre, usando caché local y API remota.
     *
     * @param name Nombre por defecto del tipo
     * @return Flow que emite el tipo y el estado de carga
     */
    fun getTypeByName(name: String): Flow<Resource<TypeDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de tipo [name=$name]")

            try {
                // Consultar caché local
                logDebug("Consultando base de datos local para tipo $name...")
                val localType = typeDao.getTypeByName(name)

                if (localType != null) {
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo tipo $name de la base de datos local")
                    emit(Resource.Success(localType.typeEntityToDomain()))
                } else {
                    logDebug("No se encontró tipo $name en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getTypeByName(name) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibido tipo $name de la API")
                            saveToDatabase {
                                logDebug("Insertando tipo $name en la base de datos")
                                typeDao.insertType(apiResponse.typeDTOToEntity())
                            }
                            logDebug("Devolviendo tipo $name obtenido de la API")
                            emit(Resource.Success(apiResponse.typeDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener tipo $name de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener tipo: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Función auxiliar para manejar respuestas de la API de forma genérica.
     * Ejecuta la llamada a la API y delega en los callbacks según el resultado.
     *
     * @param apiCall Llamada suspendida a la API
     * @param onSuccess Callback en caso de éxito
     * @param onError Callback en caso de error
     */
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

    /**
     * Función auxiliar para manejar fallback a datos locales en caso de error de la API.
     * Si hay datos locales, los devuelve junto con el mensaje de error.
     * Si no hay datos locales, emite el error con datos vacíos o nulos.
     *
     * @param errorMessage Mensaje de error de la API
     * @param localData Datos locales disponibles
     * @param transformData Función para transformar los datos locales
     * @param emitResult Función para emitir el resultado
     */
    private inline fun <T, R> handleLocalFallback(
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

    /**
     * Ejecuta un bloque suspendido para guardar datos en la base de datos local en el dispatcher IO.
     *
     * @param block Bloque suspendido que realiza la operación de guardado
     */
    private suspend inline fun saveToDatabase(crossinline block: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            logDebug("💾 GUARDANDO DATOS: API → BASE DE DATOS LOCAL")
            block()
            logDebug("Datos guardados correctamente en la base de datos local")
        }
    }

    /**
     * Función auxiliar para logging de mensajes de depuración.
     *
     * @param message Mensaje a registrar
     */
    private fun logDebug(message: String) {
        Log.d(tag, message)
    }

    /**
     * Función auxiliar para logging de mensajes de error.
     *
     * @param message Mensaje a registrar
     */
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
     *
     * @param pokemonId Identificador único del Pokémon
     * @return true si es favorito, false en caso contrario
     */
    suspend fun isPokemonFavorite(pokemonId: Int): Boolean {
        val entity = pokemonDao.getPokemonById(pokemonId)
        return entity?.favorite ?: false
    }

    /**
     * Obtiene los detalles de un Pokémon específico por su nombre.
     * Prioriza la base de datos local y, si no encuentra datos, consulta la API.
     *
     * @param name Nombre del Pokémon
     * @return Flow que emite los detalles del Pokémon y el estado de carga
     */
    fun getPokemonDetailsByName(name: String): Flow<Resource<PokemonDetailsDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de detalles del Pokémon [nombre=$name]")

            try {
                // Intentar obtener datos de la base de datos local
                logDebug("Consultando base de datos local para detalles del Pokémon '$name'...")
                val localPokemonDetails = pokemonDetailsDao.getPokemonByName(name.lowercase())

                if (localPokemonDetails != null) {
                    // Si hay datos en la base de datos local, devolverlos
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo detalles del Pokémon '$name' de la base de datos local")

                    emit(Resource.Success(localPokemonDetails.pokemonDetailsEntityToDomain()))
                } else {
                    // Si no hay datos en la base de datos local, obtenerlos de la API
                    logDebug("No se encontraron detalles del Pokémon '$name' en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getPokemonDetailsByName(name.lowercase()) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibidos detalles del Pokémon '$name' de la API")
                            logDebug("Datos del pokemon en la API: $apiResponse")

                            // Guardar en la base de datos local
                            saveToDatabase {
                                logDebug("Insertando detalles del Pokémon '$name' en la base de datos")
                                pokemonDetailsDao.insertPokemonDetails(apiResponse.pokemonDetailsDTOToEntity())
                            }

                            logDebug("Devolviendo detalles del Pokémon '$name' obtenidos de la API")
                            emit(Resource.Success(apiResponse.pokemonDetailsDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener detalles del Pokémon '$name' de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener detalles del Pokémon por nombre: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Obtiene las ubicaciones donde se puede encontrar un Pokémon por su ID.
     * Prioriza la base de datos local y, si no encuentra datos, consulta la API.
     *
     * @param id Identificador único del Pokémon
     * @return Flow que emite los lugares de encuentro del Pokémon y el estado de carga
     */
    fun getPokemonEncountersById(id: Int): Flow<Resource<PokemonEncountersDomain>> =
        flow {
            emit(Resource.Loading)
            logDebug("Iniciando búsqueda de lugares de encuentro del Pokémon [id=$id]")

            try {
                // Intentar obtener datos de la base de datos local
                logDebug("Consultando base de datos local para encuentros del Pokémon $id...")
                val localEncounters = pokemonEncountersDao.getPokemonEncountersById(id)

                if (localEncounters != null) {
                    // Si hay datos en la base de datos local, devolverlos
                    logDebug("📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                    logDebug("Devolviendo encuentros del Pokémon $id de la base de datos local")
                    
                    emit(Resource.Success(localEncounters.pokemonEncountersEntityToDomain()))
                } else {
                    // Si no hay datos en la base de datos local, obtenerlos de la API
                    logDebug("No se encontraron encuentros del Pokémon $id en la DB local, consultando API...")

                    handleApiResponse(
                        apiCall = { remoteDataSource.getPokemonEncountersById(id) },
                        onSuccess = { apiResponse ->
                            logDebug("📡 ORIGEN DE DATOS: API REMOTA")
                            logDebug("Recibidos encuentros del Pokémon $id de la API")

                            // Guardar en la base de datos local
                            if (apiResponse.isNotEmpty()) {
                                saveToDatabase {
                                    logDebug("Insertando encuentros del Pokémon $id en la base de datos")
                                    pokemonEncountersDao.insertPokemonEncounters(
                                        apiResponse.pokemonEncountersDTOToEntity(id)
                                    )
                                }
                            } else {
                                logDebug("No hay encuentros para el Pokémon $id")
                            }

                            logDebug("Devolviendo encuentros del Pokémon $id obtenidos de la API")
                            emit(Resource.Success(apiResponse.pokemonEncountersDTOToDomain()))
                        },
                        onError = { errorMessage ->
                            logError("Error al obtener encuentros del Pokémon $id de la API: $errorMessage")
                            emit(Resource.Error(message = errorMessage))
                        }
                    )
                }
            } catch (e: Exception) {
                logError("Error inesperado al obtener encuentros del Pokémon: ${e.message}")
                emit(Resource.Error(message = "Error: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)
}