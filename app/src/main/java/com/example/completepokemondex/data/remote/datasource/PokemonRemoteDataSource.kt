package com.example.completepokemondex.data.remote.datasource

import android.util.Log
import com.example.completepokemondex.data.remote.api.ApiClient
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.models.AbilityDTO
import com.example.completepokemondex.data.remote.models.EvolutionChainDTO
import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import com.example.completepokemondex.data.remote.models.PokemonEncountersDTO
import com.example.completepokemondex.data.remote.models.PokemonMoveDTO
import com.example.completepokemondex.data.remote.models.PokemonSpeciesDTO
import com.example.completepokemondex.data.remote.models.TypeDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * `PokemonRemoteDataSource` es responsable de abstraer el uso de la Api de Pokémon.
 *
 * Esta clase maneja las peticiones de red para recuperar datos de Pokémon. Utiliza el `ApiClient`
 * para interactuar con la PokeAPI y gestiona posibles errores durante la comunicación de red.
 */
class PokemonRemoteDataSource(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val apiService = ApiClient.pokeApiService

    /**
     * Obtiene una lista de Pokémon de la API remota.
     *
     * @param limit Número máximo de elementos a devolver.
     * @param offset Posición desde donde empezar a devolver elementos.
     * @return [Resource] que contiene la respuesta de la API o un error.
     */
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<List<PokemonDTO>> {
        return withContext(dispatcher) {
            try {
                val response = apiService.getPokemonList(limit, offset)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Resource.Success(it.results)
                    } ?: Resource.Error("Respuesta vacía", emptyList())
                } else {
                    Resource.Error(
                        "Error HTTP ${response.code()}: ${response.message()}",
                        emptyList()
                    )
                }
            } catch (e: IOException) {
                Resource.Error("Error de red: ${e.message}", emptyList())
            } catch (e: HttpException) {
                Resource.Error("Error HTTP ${e.code()}: ${e.message}", emptyList())
            } catch (e: Exception) {
                Resource.Error("Error desconocido: ${e.message}", emptyList())
            }
        }
    }

    /**
     * Obtiene los detalles de un Pokémon específico por su ID desde la API.
     *
     * @param id Identificador único del Pokémon
     * @return Un objeto Resource que contiene los detalles del Pokémon o un mensaje de error
     */
    suspend fun getPokemonDetailsById(id: Int): Resource<PokemonDetailsDTO> {
        return try {
            val response = apiService.getPokemonDetailsById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("PokemonRemoteDataSource", "Detalles del pokemon obtenidos: $body")
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PokemonRemoteDataSource", "Error obteniendo detalles del pokemon: ${e.message}")
            Resource.Error("Error de red: ${e.message}")
        }
    }

    /**
     * Obtiene la especie de un Pokémon específico por su ID desde la API.
     *
     * @param id Identificador único de la especie Pokémon
     * @return Un objeto Resource que contiene la especie del Pokémon o un mensaje de error
     */
    suspend fun getPokemonSpeciesById(id: Int): Resource<PokemonSpeciesDTO> {
        return try {
            val response = apiService.getPokemonSpeciesById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("PokemonRemoteDataSource", "Especie del pokemon obtenida: $body")
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PokemonRemoteDataSource", "Error obteniendo especie del pokemon: ${e.message}")
            Resource.Error("Error de red: ${e.message}")
        }
    }

    /**
     * Obtiene la cadena de evolución de un Pokémon por su ID desde la API.
     *
     * @param id Identificador único de la evolution chain
     * @return Un objeto Resource que contiene la evolution chain o un mensaje de error
     */
    suspend fun getEvolutionChainById(id: Int): Resource<EvolutionChainDTO> {
        return try {
            val response = apiService.getEvolutionChainById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("PokemonRemoteDataSource", "Evolution chain obtenida: $body")
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PokemonRemoteDataSource", "Error obteniendo evolution chain: ${e.message}")
            Resource.Error("Error de red: ${e.message}")
        }
    }

    /**
     * Obtiene una habilidad Pokémon específica por su ID desde la API.
     *
     * @param id Identificador único de la habilidad
     * @return Un objeto Resource que contiene los detalles de la habilidad o un mensaje de error
     */
    suspend fun getAbilityById(id: Int): Resource<AbilityDTO> {
        return try {
            val response = apiService.getAbilityById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("PokemonRemoteDataSource", "Habilidad obtenida: $body")
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PokemonRemoteDataSource", "Error obteniendo habilidad: ${e.message}")
            Resource.Error("Error de red: ${e.message}")
        }
    }

    /**
     * Obtiene los detalles de un Pokémon específico por su nombre desde la API.
     *
     * @param name Nombre del Pokémon
     * @return Un objeto Resource que contiene los detalles del Pokémon o un mensaje de error
     */
    suspend fun getPokemonDetailsByName(name: String): Resource<PokemonDetailsDTO> {
        return try {
            val response = apiService.getPokemonDetailsByName(name)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d(
                        "PokemonRemoteDataSource",
                        "Detalles del pokemon obtenidos por nombre: $body"
                    )
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(
                "PokemonRemoteDataSource",
                "Error obteniendo detalles del pokemon por nombre: ${e.message}"
            )
            Resource.Error("Error de red: ${e.message}")
        }
    }

    /**
     * Obtiene las ubicaciones donde se puede encontrar un Pokémon específico por su ID desde la API.
     *
     * @param id Identificador único del Pokémon
     * @return Un objeto Resource que contiene la lista de ubicaciones donde se puede encontrar el Pokémon o un mensaje de error
     */
    suspend fun getPokemonEncountersById(id: Int): Resource<List<PokemonEncountersDTO>> {
        return try {
            val response = apiService.getPokemonEncountersById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d(
                        "PokemonRemoteDataSource",
                        "Encuentros del pokemon obtenidos: ${body.size}"
                    )
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío", emptyList())
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}", emptyList())
            }
        } catch (e: Exception) {
            Log.e(
                "PokemonRemoteDataSource",
                "Error obteniendo encuentros del pokemon: ${e.message}"
            )
            Resource.Error("Error de red: ${e.message}", emptyList())
        }
    }

    /**
     * Obtiene un movimiento específico por su ID desde la API.
     *
     * @param id Identificador único del movimiento
     * @return Un objeto Resource que contiene los detalles del movimiento o un mensaje de error
     */
    suspend fun getMoveById(id: Int): Resource<PokemonMoveDTO> {
        return try {
            val response = apiService.getMoveById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("PokemonRemoteDataSource", "Movimiento obtenido: $id")
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PokemonRemoteDataSource", "Error obteniendo movimiento: ${e.message}")
            Resource.Error("Error de red: ${e.message}")
        }
    }

    /**
     * Obtiene un tipo específico por su ID desde la API.
     *
     * @param id Identificador único del tipo
     * @return Un objeto Resource que contiene los detalles del tipo o un mensaje de error
     */
    suspend fun getTypeById(id: Int): Resource<TypeDTO> {
        return try {
            val response = apiService.getTypeById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("PokemonRemoteDataSource", "Tipo obtenido: $body")
                    Resource.Success(body)
                } else {
                    Resource.Error("Cuerpo de respuesta vacío")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PokemonRemoteDataSource", "Error obteniendo tipo: ${e.message}")
            Resource.Error("Error de red: ${e.message}")
        }
    }
}
