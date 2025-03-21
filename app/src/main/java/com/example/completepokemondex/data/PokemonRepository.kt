package com.example.completepokemondex.data

import com.example.completepokemondex.core.RetrofitInstance
import com.example.completepokemondex.data.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Clase que se encarga de realizar las solicitudes a la API para obtener los Pokémon.
 *
 */
class PokemonRepository {
    /**
     * Suspende la función para obtener un Pokémon por su nombre.
     * @param name El nombre del Pokémon a buscar.
     * @return Un Result que contiene un objeto Pokemon si la solicitud es exitosa, o una excepción si falla.
     */
    suspend fun getPokemon(name: String): Result<Pokemon> {
        return withContext(Dispatchers.IO) {
            try {
                // Realiza la solicitud a la API para obtener el Pokémon.
                val response = RetrofitInstance.api.getPokemon(name.lowercase()).execute()
                if (response.isSuccessful && response.body() != null) {
                    // Devuelve el resultado exitoso con el cuerpo de la respuesta.
                    Result.success(response.body()!!)
                } else {
                    // Devuelve un resultado fallido con el mensaje de error.
                    Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                // Captura cualquier excepción y devuelve un resultado fallido.
                Result.failure(e)
            }
        }
    }
}