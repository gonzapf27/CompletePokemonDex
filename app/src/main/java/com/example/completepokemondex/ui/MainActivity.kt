package com.example.completepokemondex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity es la actividad principal de la aplicación que muestra la interfaz de usuario y
 * maneja la interacción del usuario.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pokemonRepository: PokemonRepository

    /**
     * Método onCreate se llama cuando la actividad es creada. Inicializa el ViewModel, configura
     * los observadores y eventos.
     *
     * @param savedInstanceState Si la actividad se está recreando, este parámetro contiene
     * ```
     *                           los datos más recientes suministrados en onSaveInstanceState.
     * ```
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar la gestión de insets para manejar notch y safeArea
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplicar insets para respetar el área segura
        //setupInsets()

        // Inicializar repository manualmente
        val database = PokedexDatabase.Companion.getDatabase(applicationContext)
        val pokemonDao = database.pokemonDao()
        val pokemonDetailsDao = database.pokemonDetailsDao()
        val remoteDataSource = PokemonRemoteDataSource()
        val pokemonSpeciesDao = database.pokemonSpeciesDao()
        val abilityDao = database.abilityDao()
        val evolutionChainDao = database.evolutionChainDao()
        val pokemonEncountersDao = database.pokemonEncountersDao()
        pokemonRepository = PokemonRepository(
            pokemonDao,
            pokemonDetailsDao,
            pokemonSpeciesDao,
            abilityDao,
            remoteDataSource,
            evolutionChainDao,
            pokemonEncountersDao
        )
    }

    /**
     * Configura los insets para manejar correctamente el área segura
     */
    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplicar padding al fragmentContainerView para respetar las áreas seguras
            binding.fragmentContainerView.updatePadding(
                top = insets.top,
                bottom = insets.bottom
            )

            windowInsets
        }
    }

}
