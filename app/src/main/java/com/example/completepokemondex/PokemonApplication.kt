package com.example.completepokemondex

import android.app.Application
import com.example.completepokemondex.core.RetrofitInstance

/**
 * Clase principal de la aplicación que extiende de Application.
 * Se utiliza para inicializar componentes globales de la aplicación.
 */
class PokemonApplication : Application() {
    /**
     * Método llamado cuando la aplicación es creada.
     * Inicializa la instancia de Retrofit con el contexto de la aplicación.
     */
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.initialize(applicationContext)
    }
}