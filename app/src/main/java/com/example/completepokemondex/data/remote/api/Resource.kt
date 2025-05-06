package com.example.completepokemondex.data.remote.api

/**
 * Clase sellada que representa el estado de un recurso obtenido de una fuente de datos.
 *
 * Esta clase define tres posibles estados para un recurso:
 * - Success: Indica que la operación fue exitosa y contiene los datos resultantes.
 * - Error: Indica que la operación falló y contiene un mensaje de error.
 *   También puede contener datos parciales o en caché.
 * - Loading: Indica que la operación está en curso.
 *
 * @param T El tipo de dato contenido en el recurso.
 */
sealed class Resource<out T> {
    /**
     * Representa un estado exitoso con los datos obtenidos.
     * @param data Los datos resultantes de la operación.
     */
    data class Success<T>(val data: T) : Resource<T>()

    /**
     * Representa un estado de error con un mensaje y opcionalmente datos parciales o en caché.
     * @param message Mensaje de error descriptivo.
     * @param data Datos parciales o en caché (opcional).
     */
    data class Error<T>(val message: String, val data: T? = null) : Resource<T>()

    /**
     * Representa un estado de carga mientras la operación está en curso.
     */
    data object Loading : Resource<Nothing>()
}