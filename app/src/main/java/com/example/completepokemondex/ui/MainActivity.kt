package com.example.completepokemondex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MainActivity es la actividad principal de la aplicación.
 * Se encarga de inicializar el repositorio de Pokémon y de gestionar la vista principal.
 * 
 * - Configura la gestión de insets para manejar notch y áreas seguras.
 * - Inicializa el repositorio de Pokémon utilizando DAOs locales y el data source remoto.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var pokemonRepository: PokemonRepository

    /**
     * Método onCreate se llama cuando la actividad es creada.
     * Inicializa el binding, configura la gestión de insets y el repositorio de Pokémon.
     *
     * @param savedInstanceState Si la actividad se está recreando, este parámetro contiene
     * los datos más recientes suministrados en onSaveInstanceState.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar la gestión de insets para manejar notch y safeArea
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplicar insets para respetar el área segura
        //setupInsets()
    }

}
