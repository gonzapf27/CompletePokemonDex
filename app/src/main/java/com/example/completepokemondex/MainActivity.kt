package com.example.completepokemondex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.databinding.ActivityMainBinding

/**
 * MainActivity es la actividad principal de la aplicación que muestra la interfaz de usuario
 * y maneja la interacción del usuario.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * Método onCreate se llama cuando la actividad es creada. Inicializa el ViewModel,
     * configura los observadores y eventos.
     *
     * @param savedInstanceState Si la actividad se está recreando, este parámetro contiene
     *                           los datos más recientes suministrados en onSaveInstanceState.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar ViewModel


        // Configurar observadores

        // Configurar eventos
    }
}
