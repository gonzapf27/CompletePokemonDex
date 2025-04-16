package com.example.completepokemondex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonInfoBinding
import com.google.android.material.chip.Chip

class PokemonInfoFragment : Fragment() {
    private var _binding: FragmentPokemonInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetallesViewModel by viewModels {
        PokemonDetallesViewModel.Factory(PokedexDatabase.getDatabase(requireContext()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("pokemon_id")?.let { viewModel.setPokemonId(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var currentPokemonId: Int? = null
        var currentIsFavorite: Boolean = false
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.loadingIndicator.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            binding.pokemonDetailsId.text = "#" + state.id
            binding.pokemonDetailsNombre.text = state.nombre
            binding.pokemonDetailsHeight.text = state.height
            binding.pokemonDetailsWeight.text = state.weight
            // Imagen
            state.imageUrl?.let {
                binding.pokemonImage.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                com.bumptech.glide.Glide.with(requireContext())
                    .load(it)
                    .into(binding.pokemonImage)
            }
            // Cambiar icono de favorito según el estado
            if (state.isFavorite) {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_outline)
            }
            // Animación al cambiar favorito
            val context = binding.btnFavorite.context
            val anim = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.favorite_pop)
            binding.btnFavorite.startAnimation(anim)
            // Guardar id y estado actual para el click
            currentPokemonId = state.id.toIntOrNull()
            currentIsFavorite = state.isFavorite
            // Fondo gradiente
            val gradientBg = binding.pokemonFragmentGradientBg
            // Obtener los colores de los tipos (máximo 2, como en item_pokemon.xml)
            val typeColors = state.types.take(2).map { 
                androidx.core.content.ContextCompat.getColor(requireContext(), it.color)
            }
            val gradientColors = when {
                typeColors.isEmpty() -> intArrayOf(android.graphics.Color.LTGRAY, android.graphics.Color.LTGRAY)
                typeColors.size == 1 -> intArrayOf(typeColors[0], typeColors[0])
                else -> typeColors.toIntArray()
            }
            val gradientDrawable = android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, // vertical
                gradientColors
            )
            gradientDrawable.cornerRadius = 0f
            gradientBg.background = gradientDrawable

            // Mostrar tipos (máximo 2 chips)
            val chips = listOf(binding.pokemonTypeChip1, binding.pokemonTypeChip2)
            chips.forEach { it.visibility = View.GONE }
            state.types.take(2).forEachIndexed { idx, typeUi ->
                val chip = chips[idx]
                chip.visibility = View.VISIBLE
                chip.text = if (typeUi.stringRes != 0) getString(typeUi.stringRes)
                    else typeUi.name.replaceFirstChar { it.uppercase() }
                val realColor = androidx.core.content.ContextCompat.getColor(requireContext(), typeUi.color)
                chip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(realColor)
                chip.setTextColor(android.graphics.Color.WHITE)
            }

            // Mostrar habilidades (máximo 3)
            val habilidadCards = listOf(
                Triple(binding.habilidadCard1, binding.habilidadNombre1, binding.habilidadDesc1),
                Triple(binding.habilidadCard2, binding.habilidadNombre2, binding.habilidadDesc2),
                Triple(binding.habilidadCard3, binding.habilidadNombre3, binding.habilidadDesc3)
            )
            habilidadCards.forEach { triple ->
                triple.first.visibility = View.GONE
            }
            state.habilidades.take(3).forEachIndexed { idx, habilidad ->
                val triple = habilidadCards[idx]
                triple.first.visibility = View.VISIBLE
                // Si la habilidad es oculta, mostrar el texto correspondiente
                if (habilidad.isOculta == true) {
                    triple.second.text = "${habilidad.nombre} (${getString(R.string.hidden_ability)})"
                } else {
                    triple.second.text = habilidad.nombre
                }
                triple.third.text = habilidad.descripcion
                triple.third.visibility = if (habilidad.descripcion.isNotBlank()) View.VISIBLE else View.GONE
            }

            // Descripción
            binding.pokemonDetailsDescription.text = state.descripcion

            // Mostrar tasa de captura + dificultad
            val captureRateText = state.captureRate?.toString() ?: "?"
            val difficultyText = state.captureRate?.let {
                com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain(
                    capture_rate = it,
                    base_happiness = null, color = null, egg_groups = null, evolution_chain = null,
                    evolves_from_species = null, flavor_text_entries = null, form_descriptions = null,
                    forms_switchable = null, gender_rate = null, genera = null, generation = null,
                    growth_rate = null, habitat = null, has_gender_differences = null, hatch_counter = null,
                    id = null, is_baby = null, is_legendary = null, is_mythical = null, name = null,
                    names = null, order = null, pal_park_encounters = null, pokedex_numbers = null,
                    shape = null, varieties = null
                ).getCaptureDifficulty()
            } ?: ""
            binding.pokemonCaptureRate.text =
                if (difficultyText.isNotBlank())
                    "$captureRateText  •  $difficultyText"
                else
                    captureRateText

        }
        // Listener para marcar/desmarcar favorito
        binding.btnFavorite.setOnClickListener {
            val id = currentPokemonId
            if (id != null) {
                viewModel.toggleFavorite(id, !currentIsFavorite)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pokemonId: Int) = PokemonInfoFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
