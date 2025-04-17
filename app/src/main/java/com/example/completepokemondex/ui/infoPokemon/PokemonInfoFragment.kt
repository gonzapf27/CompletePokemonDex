package com.example.completepokemondex.ui.infoPokemon

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonInfoFragment : Fragment() {
    private var _binding: FragmentPokemonInfoBinding? = null
    private val binding get() = _binding!!

    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }

    private val viewModel: PokemonInfoViewModel by viewModels {
        PokemonInfoViewModel.Factory(PokedexDatabase.Companion.getDatabase(requireContext()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPokemonId(pokemonId)
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
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.loadingIndicator.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            binding.pokemonDetailsId.text = "#" + state.id
            binding.pokemonDetailsNombre.text = state.nombre
            binding.pokemonDetailsHeight.text = state.height
            binding.pokemonDetailsWeight.text = state.weight
            // Imagen
            state.imageUrl?.let {
                binding.pokemonImage.setBackgroundColor(Color.TRANSPARENT)
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.pokemonImage)
            }
            // Cambiar icono de favorito según el estado
            if (state.isFavorite) {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_outline)
            }
            // Fondo gradiente
            val gradientBg = binding.pokemonFragmentGradientBg
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

            // Mostrar tipos (máximo 2 chips)
            val chips = listOf(binding.pokemonTypeChip1, binding.pokemonTypeChip2)
            chips.forEach { it.visibility = View.GONE }
            state.types.take(2).forEachIndexed { idx, typeUi ->
                val chip = chips[idx]
                chip.visibility = View.VISIBLE
                chip.text = if (typeUi.stringRes != 0) getString(typeUi.stringRes)
                    else typeUi.name.replaceFirstChar { it.uppercase() }
                val realColor = ContextCompat.getColor(requireContext(), typeUi.color)
                chip.chipBackgroundColor = ColorStateList.valueOf(realColor)
                chip.setTextColor(Color.WHITE)
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
            // Mostrar especie
            binding.pokemonSpeciesType.text = state.speciesGenus

            // Mostrar % de sexo
            if (state.genderMalePercent == 0.0 && state.genderFemalePercent == 0.0) {
                binding.pokemonGenderMale.text = "—"
                binding.pokemonGenderFemale.text = "—"
                binding.genderRatioIndicator.progress = 0
            } else {
                binding.pokemonGenderMale.text = if (state.genderMalePercent != null) String.format("%.1f%%", state.genderMalePercent) else "?"
                binding.pokemonGenderFemale.text = if (state.genderFemalePercent != null) String.format("%.1f%%", state.genderFemalePercent) else "?"
                binding.genderRatioIndicator.progress = state.genderMalePercent?.toInt() ?: 0
            }

            // Mostrar tasa de captura + dificultad
            val captureRateText = state.captureRate?.toString() ?: "?"
            val difficultyText = state.captureRate?.let {
                val resId = PokemonSpeciesDomain(
                    capture_rate = it,
                    base_happiness = null, color = null, egg_groups = null, evolution_chain = null,
                    evolves_from_species = null, flavor_text_entries = null, form_descriptions = null,
                    forms_switchable = null, gender_rate = null, genera = null, generation = null,
                    growth_rate = null, habitat = null, has_gender_differences = null, hatch_counter = null,
                    id = null, is_baby = null, is_legendary = null, is_mythical = null, name = null,
                    names = null, order = null, pal_park_encounters = null, pokedex_numbers = null,
                    shape = null, varieties = null
                ).getCaptureDifficultyStringRes()
                requireContext().getString(resId)
            } ?: ""
            binding.pokemonCaptureRate.text =
                if (difficultyText.isNotBlank())
                    "$captureRateText  •  $difficultyText"
                else
                    captureRateText

            // Listener para marcar/desmarcar favorito
            binding.btnFavorite.setOnClickListener {
                val id = state.id.toIntOrNull()
                if (id != null) {
                    viewModel.toggleFavorite(id, !state.isFavorite)
                }
            }

            // Mostrar la cadena de evolución (imágenes y nombres)
            val evolutionContainer = binding.evolutionChainContainer
            evolutionContainer.removeAllViews()
            val context = requireContext()
            val evolutionDetails = state.evolutionChainDetails
            for ((idx, poke) in evolutionDetails.withIndex()) {
                val pokeLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(12)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                val imageView = ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(140, 140)
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }
                val nameView = TextView(context).apply {
                    text = poke.name?.replaceFirstChar { it.uppercase() } ?: ""
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                    textSize = 14f
                }
                val imageUrl = poke.sprites?.other?.`official-artwork`?.front_default
                    ?: poke.sprites?.front_default
                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_pokeball)
                    .into(imageView)
                pokeLayout.addView(imageView)
                pokeLayout.addView(nameView)
                evolutionContainer.addView(pokeLayout)

                // Flecha entre pokémon (excepto el último)
                if (idx < evolutionDetails.size - 1) {
                    val arrow = ImageView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(60, 60)
                        setImageResource(R.drawable.ic_arrow_right) // Usa tu propio drawable de flecha
                        setColorFilter(ContextCompat.getColor(context, R.color.text_secondary))
                    }
                    evolutionContainer.addView(arrow)
                }
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