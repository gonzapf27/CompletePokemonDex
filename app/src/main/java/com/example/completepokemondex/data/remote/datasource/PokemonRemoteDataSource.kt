package com.example.completepokemondex.data.remote.datasource

import com.example.completepokemondex.data.remote.api.ApiClient
import com.example.completepokemondex.data.remote.models.ApiResponse
import com.example.completepokemondex.data.remote.models.PokemonResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * `PokemonRemoteDataSource` es responsable de obtener datos de Pokémon desde una API remota.
 *
 * Esta clase maneja las peticiones de red para recuperar datos de Pokémon. Utiliza el `ApiClient`
 * para interactuar con la PokeAPI y gestiona posibles errores durante la comunicación de red.
 */
class PokemonRemoteDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val apiService = ApiClient.pokeApiService

    /**
     * Obtiene una lista de Pokémon de la API remota.
     *
     * @param limit Número máximo de elementos a devolver.
     * @param offset Posición desde donde empezar a devolver elementos.
     * @return [ApiResponse] que contiene la respuesta de la API o un error.
     */
    suspend fun getPokemonList(limit: Int, offset: Int): ApiResponse<PokemonResponse> {
        return withContext(dispatcher) {
            try {
                val response = apiService.getPokemonList(limit, offset)
                ApiResponse.Success(response)
            } catch (e: IOException) {
                // Error de red (sin conexión, timeout, etc.)
                ApiResponse.Error("Error de conexión: ${e.localizedMessage}", createEmptyPokemonListResponse())
            } catch (e: HttpException) {
                // Error en la respuesta HTTP (4xx, 5xx)
                ApiResponse.Error("Error del servidor: Código ${e.code()}", createEmptyPokemonListResponse())
            } catch (e: Exception) {
                // Otros errores inesperados
                ApiResponse.Error(
                    "Error inesperado: ${e.message ?: "Error desconocido"}",
                    createEmptyPokemonListResponse()
                )
            }
        }
    }

    /**
     * Crea una respuesta vacía para casos de error.
     *
     * @return Una instancia de [PokemonResponse] con valores predeterminados.
     */
    private fun createEmptyPokemonListResponse() = PokemonResponse(
        count = 0,
        next = null,
        previous = null,
        results = emptyList()
    )
}