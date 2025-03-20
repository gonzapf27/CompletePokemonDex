package com.example.completepokemondex.services

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Objeto singleton que proporciona una instancia de Retrofit configurada con un cliente HTTP con caché.
 * 
 * @property CACHE_SIZE Tamaño de la caché en bytes (10 MB).
 * @property instance Instancia de PokeApiService.
 */
object RetrofitInstance {
    private const val CACHE_SIZE = 10 * 1024 * 1024 // 10 MB
    private var instance: PokeApiService? = null

    /**
     * Inicializa la instancia de Retrofit con un cliente HTTP con caché.
     * 
     * @param context Contexto de la aplicación necesario para acceder al directorio de caché.
     */
    fun initialize(context: Context) {
        if (instance == null) {
            val cacheDirectory = File(context.cacheDir, "http-cache")
            val cache = Cache(cacheDirectory, CACHE_SIZE.toLong())

            val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    var request = chain.request()

                    // Añadir encabezados de control de caché basados en la disponibilidad de red
                    request = if (isNetworkAvailable(context)) {
                        // Modo en línea - obtener de la red si la caché es mayor a 2 minutos
                        request.newBuilder()
                            .header("Cache-Control", "public, max-age=120")
                            .build()
                    } else {
                        // Modo fuera de línea - usar datos en caché hasta por 7 días
                        request.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=604800")
                            .build()
                    }

                    chain.proceed(request)
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            instance = retrofit.create(PokeApiService::class.java)
        }
    }

    /**
     * Obtiene la instancia de PokeApiService.
     * 
     * @return Instancia de PokeApiService.
     * @throws IllegalStateException Si RetrofitInstance no ha sido inicializado.
     */
    val api: PokeApiService
        get() {
            return instance ?: throw IllegalStateException("RetrofitInstance must be initialized before use")
        }

    /**
     * Verifica si la red está disponible.
     * 
     * @param context Contexto de la aplicación necesario para acceder al servicio de conectividad.
     * @return `true` si la red está disponible, `false` en caso contrario.
     */
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return actNw.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
               actNw.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
