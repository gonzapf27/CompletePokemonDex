package com.example.completepokemondex.data.remote.datasource

import com.example.completepokemondex.data.remote.api.ApiClient
import com.example.completepokemondex.data.remote.models.ApiResponse
import com.example.completepokemondex.data.remote.models.PokemonDTO
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
     * @return [ApiResponse] que contiene la respuesta de la API o un error.
     */
    suspend fun getPokemonList(limit: Int, offset: Int): ApiResponse<List<PokemonDTO>> {
        return withContext(dispatcher) {
            try {
                val response = apiService.getPokemonList(limit, offset)
                when (response) {
                    is ApiResponse.Success -> ApiResponse.Success(response.data)
                    is ApiResponse.Error -> ApiResponse.Error(response.message, emptyList())
                    is ApiResponse.Loading -> ApiResponse.Loading
                }
            } catch (e: IOException) {
                ApiResponse.Error("Error de red: ${e.message}", emptyList())
            } catch (e: HttpException) {
                ApiResponse.Error("Error HTTP ${e.code()}: ${e.message()}", emptyList())
            } catch (e: Exception) {
                ApiResponse.Error("Error desconocido: ${e.message}", emptyList())
            }
        }
    }

   
}
