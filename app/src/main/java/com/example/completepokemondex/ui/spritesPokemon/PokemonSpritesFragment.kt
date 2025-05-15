package com.example.completepokemondex.ui.spritesPokemon

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonSpritesBinding
import com.example.completepokemondex.ui.adapters.PokemonSpritesListAdapter
import com.example.completepokemondex.ui.adapters.PokemonSpriteItem
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento encargado de mostrar los sprites de un Pokémon.
 * Observa el estado del ViewModel y actualiza la UI en consecuencia.
 */
@AndroidEntryPoint
class PokemonSpritesFragment : Fragment() {
    private var _binding: FragmentPokemonSpritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonSpritesViewModel by viewModels()
    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }
    private lateinit var spritesAdapter: PokemonSpritesListAdapter

    /**
     * Inicializa la vista y el binding del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonSpritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura los observadores y carga los datos al crear la vista.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar datos en el ViewModel usando el pokemonId
        viewModel.loadPokemonSprites(pokemonId)

        spritesAdapter = PokemonSpritesListAdapter()
        binding.spritesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = spritesAdapter
        }

        // Observar cambios en el estado de la UI
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.spritesScrollView.visibility = View.GONE

                Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading_pokeball)
                    .into(binding.loadingIndicator)
                return@observe
            } else {
                binding.loadingIndicator.visibility = View.GONE
                binding.spritesScrollView.visibility = View.VISIBLE

                // Configurar el gradiente según los tipos
                val gradientBg = binding.spriteFragmentGradientBg
                val typeColors = state.types.take(2).map {
                    ContextCompat.getColor(requireContext(), it.color)
                }
                val gradientColors = when {
                    typeColors.isEmpty() -> intArrayOf(Color.LTGRAY, Color.LTGRAY)
                    typeColors.size == 1 -> intArrayOf(typeColors[0], typeColors[0])
                    else -> typeColors.toIntArray()
                }
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, // vertical
                    gradientColors
                )
                gradientDrawable.cornerRadius = 0f
                gradientBg.background = gradientDrawable

                // Animación de aparición para el grid de sprites
                binding.spritesRecyclerView.apply {
                    alpha = 0f
                    animate().alpha(1f).setDuration(500).start()
                }
            }

            // Mostrar los sprites directamente en la plantilla
            displayStaticSprites(state.pokemon?.sprites)
        }
    }

    /**
     * Prepara y muestra la lista de sprites en el RecyclerView.
     */
    private fun displayStaticSprites(sprites: com.example.completepokemondex.data.domain.model.PokemonDetailsDomain.Sprites?) {
        val items = mutableListOf<PokemonSpriteItem>()
        // Básicos
        listOf(
            PokemonSpriteItem("Front Default", sprites?.front_default),
            PokemonSpriteItem("Back Default", sprites?.back_default),
            PokemonSpriteItem("Front Female", sprites?.front_female?.toString()),
            PokemonSpriteItem("Back Female", sprites?.back_female?.toString()),
            PokemonSpriteItem("Front Shiny", sprites?.front_shiny),
            PokemonSpriteItem("Back Shiny", sprites?.back_shiny),
            PokemonSpriteItem("Front Shiny Female", sprites?.front_shiny_female?.toString()),
            PokemonSpriteItem("Back Shiny Female", sprites?.back_shiny_female?.toString()),
        // Dream World
            PokemonSpriteItem("Dream World", sprites?.other?.dream_world?.front_default.toString()),
            PokemonSpriteItem("Dream World Female", sprites?.other?.dream_world?.front_female?.toString()),
        // Home
            PokemonSpriteItem("Home Default", sprites?.other?.home?.front_default),
            PokemonSpriteItem("Home Female", sprites?.other?.home?.front_female?.toString()),
            PokemonSpriteItem("Home Shiny", sprites?.other?.home?.front_shiny),
            PokemonSpriteItem("Home Shiny Female", sprites?.other?.home?.front_shiny_female?.toString()),
        // Official Artwork
            PokemonSpriteItem("Official Artwork", sprites?.other?.`official-artwork`?.front_default),
            PokemonSpriteItem("Official Artwork Shiny", sprites?.other?.`official-artwork`?.front_shiny),
        // Showdown
            PokemonSpriteItem("Showdown Front Default", sprites?.other?.showdown?.front_default),
            PokemonSpriteItem("Showdown Back Default", sprites?.other?.showdown?.back_default),
            PokemonSpriteItem("Showdown Front Female", sprites?.other?.showdown?.front_female?.toString()),
            PokemonSpriteItem("Showdown Back Female", sprites?.other?.showdown?.back_female?.toString()),
            PokemonSpriteItem("Showdown Front Shiny", sprites?.other?.showdown?.front_shiny),
            PokemonSpriteItem("Showdown Back Shiny", sprites?.other?.showdown?.back_shiny),
            PokemonSpriteItem("Showdown Front Shiny Female", sprites?.other?.showdown?.front_shiny_female?.toString()),
            PokemonSpriteItem("Showdown Back Shiny Female", sprites?.other?.showdown?.back_shiny_female?.toString())
        ).forEach { item ->
            // Solo añadir si la URL no es nula, vacía, igual a "null" y no termina en ".svg"
            if (!item.url.isNullOrBlank() && item.url != "null" && !item.url.endsWith(".svg", ignoreCase = true)) {
                items.add(item)
            }
        }
        spritesAdapter.submitList(items)
    }

    /**
     * Libera el binding al destruir la vista.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Crea una nueva instancia del fragmento con el ID del Pokémon.
         */
        fun newInstance(pokemonId: Int) = PokemonSpritesFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
