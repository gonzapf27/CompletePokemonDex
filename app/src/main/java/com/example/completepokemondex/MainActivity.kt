package com.example.completepokemondex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.completepokemondex.data.PokemonRepository
import com.example.completepokemondex.databinding.ActivityMainBinding
import com.example.completepokemondex.viewmodel.PokemonViewModel
import com.example.completepokemondex.viewmodel.PokemonViewModelFactory
import com.squareup.picasso.Picasso

/**
 * MainActivity es la actividad principal de la aplicación que muestra la interfaz de usuario
 * y maneja la interacción del usuario.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PokemonViewModel

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
        val repository = PokemonRepository()
        val viewModelFactory = PokemonViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[PokemonViewModel::class.java]

        // Configurar observadores
        setupObservers()

        // Configurar eventos
        binding.btnPrueba.setOnClickListener {
            val pokemonName =
                binding.txtNombrePokemon.text.toString().takeIf { it.isNotEmpty() } ?: "pikachu"
            viewModel.fetchPokemon(pokemonName)
        }
    }

    /**
     * Configura los observadores para el ViewModel.
     * Observa los cambios en los datos del Pokémon, el estado de carga y los errores.
     */
    private fun setupObservers() {
        viewModel.pokemon.observe(this) { pokemon ->
            if (pokemon.sprites.front_default != null) {
                Picasso.get().load(pokemon.sprites.front_default).into(binding.imgPokemon)
                binding.imgPokemon.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.imgPokemon.visibility = View.GONE
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            // Mostrar mensaje de error
            println(errorMessage)
        }
    }
}