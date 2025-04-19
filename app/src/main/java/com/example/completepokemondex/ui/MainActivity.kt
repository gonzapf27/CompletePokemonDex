package com.example.completepokemondex.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        setupInsets()

        // Inicializar repository manualmente
        val database = PokedexDatabase.Companion.getDatabase(applicationContext)
        val pokemonDao = database.pokemonDao()
        val pokemonDetailsDao = database.pokemonDetailsDao()
        val remoteDataSource = PokemonRemoteDataSource()
        val pokemonSpeciesDao = database.pokemonSpeciesDao()
        val abilityDao = database.abilityDao()
        val evolutionChainDao = database.evolutionChainDao()
        pokemonRepository = PokemonRepository(
            pokemonDao,
            pokemonDetailsDao,
            pokemonSpeciesDao,
            abilityDao,
            remoteDataSource,
            evolutionChainDao
        )

        // Ejemplo de uso de evolution chain
        loadEvolutionChain(1) // Puedes cambiar el ID por el que desees probar
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

    /**
     * Carga la lista de Pokémon y muestra los resultados por consola
     */
    private fun loadPokemonList() {
        lifecycleScope.launch {
            pokemonRepository.getPokemonList(10, 0).collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        Log.d("Pokemon", "Cargando lista de Pokémon...")
                    }

                    is Resource.Success -> {
                        val pokemons = response.data
                        Log.d(
                            "Pokemon",
                            "Lista de Pokémon cargada exitosamente. Total: ${pokemons.size}"
                        )
                        pokemons.forEach { pokemon ->
                            Log.d("Pokemon", "Nombre: ${pokemon.name}, ID: ${pokemon.id}")
                        }
                    }

                    is Resource.Error -> {
                        Log.e("Pokemon", "Error al cargar la lista: ${response.message}")
                        if (response.data?.isNotEmpty() == true) {
                            Log.d(
                                "Pokemon",
                                "Mostrando datos en caché. Total: ${response.data.size}"
                            )
                            response.data.forEach { pokemon ->
                                Log.d("Pokemon", "Nombre: ${pokemon.name}, ID: ${pokemon.id}")
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Carga la evolution chain y muestra los resultados por consola
     */
    private fun loadEvolutionChain(chainId: Int) {
        lifecycleScope.launch {
            pokemonRepository.getEvolutionChainById(chainId).collect { response ->
                when (response) {
                    is com.example.completepokemondex.data.remote.api.Resource.Loading -> {
                        Log.d("EvolutionChain", "Cargando evolution chain...")
                    }
                    is com.example.completepokemondex.data.remote.api.Resource.Success -> {
                        val chain = response.data
                        Log.d("EvolutionChain", "Evolution chain cargada exitosamente: $chain")
                    }
                    is com.example.completepokemondex.data.remote.api.Resource.Error -> {
                        Log.e("EvolutionChain", "Error al cargar evolution chain: ${response.message}")
                    }
                }
            }
        }
    }
}
