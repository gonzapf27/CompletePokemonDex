package com.example.completepokemondex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.remote.models.ApiResponse
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * MainActivity es la actividad principal de la aplicación que muestra la interfaz de usuario y
 * maneja la interacción del usuario.
 */
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar repository manualmente
        val pokemonDao = PokedexDatabase.getDatabase(applicationContext).pokemonDao()
        val remoteDataSource = PokemonRemoteDataSource()
        pokemonRepository = PokemonRepository(pokemonDao, remoteDataSource)

        // Configurar eventos
        binding.btnPrueba.setOnClickListener {
            println("Hola mundio!")
          //  Log.d("MainActivity", "Botón prueba clicado")
            loadPokemonList()
        }
    }

    /**
     * Carga la lista de Pokémon y muestra los resultados por consola
     */
    private fun loadPokemonList() {
        lifecycleScope.launch {
            pokemonRepository.getPokemonList(10, 0).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        Log.d("Pokemon", "Cargando lista de Pokémon...")
                    }

                    is ApiResponse.Success -> {
                        val pokemons = response.data
                        Log.d(
                            "Pokemon",
                            "Lista de Pokémon cargada exitosamente. Total: ${pokemons.size}"
                        )
                        pokemons.forEach { pokemon ->
                            Log.d("Pokemon", "Nombre: ${pokemon.name}, ID: ${pokemon.id}")
                        }
                    }

                    is ApiResponse.Error -> {
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
}
