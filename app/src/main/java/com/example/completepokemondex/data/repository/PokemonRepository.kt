package com.example.completepokemondex.data.repository

import android.util.Log
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.domain.model.PokemonDomain
import com.example.completepokemondex.domain.toDomainModelList
import com.example.completepokemondex.domain.toEntityDomainModelList
import com.example.completepokemondex.domain.toEntityList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.math.log2

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
    fun getPokemonList(limit: Int, offset: Int): Flow<Resource<List<PokemonDomain>>> =
            flow {
                // Emitir estado de carga
                emit(Resource.Loading)
                Log.d("PokemonRepository", "Iniciando búsqueda de Pokémon [limit=$limit, offset=$offset]")

                try {
                    // Intentar obtener datos de la base de datos local
                    Log.d("PokemonRepository", "Consultando base de datos local...")
                    val localPokemon = pokemonDao.getAllPokemon()
                    Log.d("PokemonRepository", "Base de datos local contiene ${localPokemon.size} Pokémon")

                    // Si hay datos en la base de datos local y son suficientes, devolverlos
                    if (localPokemon.isNotEmpty() && localPokemon.size >= offset + limit) {
                        Log.d("PokemonRepository", "📋 ORIGEN DE DATOS: BASE DE DATOS LOCAL")
                        Log.d("PokemonRepository", "Datos suficientes en DB local para offset=$offset y limit=$limit")
                        
                        val resultList = localPokemon.drop(offset).take(limit)
                        Log.d("PokemonRepository", "Devolviendo ${resultList.size} Pokémon de la base de datos local")
                        
                        emit(
                            Resource.Success(
                                resultList.toEntityDomainModelList()
                            )
                        )
                    } else {
                        // Si no hay suficientes datos en la base de datos local, obtenerlos
                        // de la API
                        Log.d("PokemonRepository", "Datos insuficientes en DB local, consultando API...")
                        
                        when (val apiResponse = remoteDataSource.getPokemonList(limit, offset)) {
                            is Resource.Success -> {
                                Log.d("PokemonRepository", "📡 ORIGEN DE DATOS: API REMOTA")
                                Log.d("PokemonRepository", "Recibidos ${apiResponse.data.size} Pokémon de la API")
                                
                                // Guardar los datos en la base de datos local
                                withContext(Dispatchers.IO) {
                                    Log.d("PokemonRepository", "💾 GUARDANDO DATOS: API → BASE DE DATOS LOCAL")
                                    Log.d("PokemonRepository", "Insertando ${apiResponse.data.size} Pokémon en la base de datos")
                                    
                                    pokemonDao.insertAllPokemon(
                                        apiResponse.data.toEntityList()
                                    )
                                    
                                    Log.d("PokemonRepository", "Datos guardados correctamente en la base de datos local")
                                }

                                // Devolver los datos obtenidos de la API
                                Log.d("PokemonRepository", "Devolviendo ${apiResponse.data.size} Pokémon obtenidos de la API")

                                emit(
                                    Resource.Success(
                                        apiResponse.data.toDomainModelList()
                                    )
                                )
                            }
                            is Resource.Error -> {
                                // Si hay un error al obtener los datos de la API, pero hay
                                // algunos datos en la base de datos local,
                                // devolver los datos disponibles con un mensaje de error
                                if (localPokemon.isNotEmpty()) {
                                    Log.d("PokemonRepository", "Devolviendo lista de Pokemon desde la base de datos local con un mensaje de error por la Api")
                                    Log.e("PokemonRepository", "Error al obtener lista de Pokemon de la API: ${apiResponse.message}")
                                    emit(
                                        Resource.Error(
                                            message = apiResponse.message,
                                            data = localPokemon.toEntityDomainModelList()
                                        )
                                    )
                                } else {
                                    // Si  hay datos en la base de datos local, devolver
                                    Log.d("PokemonRepository", "No hay datos en la base de datos local - devolviendo el error de la API")
                                    // el error
                                    Log.e("PokemonRepository", "Error al obtener lista de Pokemon de la API: ${apiResponse.message}")
                                    emit(
                                        Resource.Error(
                                            message = apiResponse.message,
                                            data = emptyList()
                                        )
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                // Mantener el estado de carga
                                emit(Resource.Loading)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Manejar cualquier excepción no controlada
                    emit(
                        Resource.Error(
                            message = "Error: ${e.message}",
                            data = emptyList()
                        )
                    )
                }
            }
            .flowOn(Dispatchers.IO)
}
