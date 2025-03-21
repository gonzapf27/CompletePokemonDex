
package com.example.completepokemondex.data.remote.models

/**
 * Clase sellada que representa la respuesta de una API.
 *
 * Esta clase define tres posibles estados para la respuesta de una API:
 * - Success: Indica que la llamada a la API fue exitosa y contiene los datos resultantes.
 * - Error: Indica que la llamada a la API falló y contiene un mensaje de error.
 *   También devuelve una lista vacía como dato.
 * - Loading: Indica que la llamada a la API está en curso.
 *
 * @param T El tipo de dato contenido en la respuesta.
 */
sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val message: String, val data: T? = null) : ApiResponse<T>()
    class Loading<T> : ApiResponse<T>()
}