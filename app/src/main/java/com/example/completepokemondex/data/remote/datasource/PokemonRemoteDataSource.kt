package com.example.completepokemondex.data.remote.datasource

import com.example.completepokemondex.data.remote.api.ApiClient
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.models.PokemonDTO
import com.example.completepokemondex.data.remote.models.PokemonDetailsDTO
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

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
                    Resource.Error("Error HTTP ${response.code()}: ${response.message()}", emptyList())
                }
            } catch (e: IOException) {
                Resource.Error("Error de red: ${e.message}", emptyList())
            } catch (e: HttpException) {
                Resource.Error("Error HTTP ${e.code()}: ${e.message()}", emptyList())
            } catch (e: Exception) {
                Resource.Error("Error desconocido: ${e.message}", emptyList())
            }
        }
    }

    /**
     * Obtiene un PokemonDetails de la API a partir de su ID.
     *
     * @param id ID del PokemonDetails a obtener..
     * @return [Resource] que contiene la respuesta de la API o un error.
     */
    suspend fun getPokemonById(id: Int): Resource<PokemonDetailsDTO> {
        return withContext(dispatcher) {
            try {
                val response = apiService.getPokemonDetailsById(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Respuesta vacía", null)
                } else {
                    Resource.Error("Error HTTP ${response.code()}: ${response.message()}", null)
                }
            } catch (e: IOException) {
                Resource.Error("Error de red: ${e.message}", null)
            } catch (e: HttpException) {
                Resource.Error("Error HTTP ${e.code()}: ${e.message()}", null)
            } catch (e: Exception) {
                Resource.Error("Error desconocido: ${e.message}", null)
            }
        }
    }
}


