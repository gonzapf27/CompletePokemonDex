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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.databinding.FragmentPokemonInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento encargado de mostrar la información detallada de un Pokémon.
 * Observa el estado de la UI desde el ViewModel y actualiza la vista en consecuencia.
 */
@AndroidEntryPoint
class PokemonInfoFragment : Fragment() {
    private var _binding: FragmentPokemonInfoBinding? = null
    private val binding get() = _binding!!

    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }

    private val viewModel: PokemonInfoViewModel by viewModels()

    /**
     * Inicializa el ViewModel con el ID del Pokémon recibido por argumentos.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPokemonId(pokemonId)
    }

    /**
     * Infla el layout del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Observa el estado de la UI y actualiza los elementos visuales.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            // Mostrar el gif de carga y ocultar el contenido mientras isLoading es true
            if (state.isLoading) {
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.contentContainer.visibility = View.GONE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading_pokeball)
                    .into(binding.loadingIndicator)
                return@observe
            } else {
                binding.loadingIndicator.visibility = View.GONE
                binding.contentContainer.visibility = View.VISIBLE
            }
            binding.pokemonDetailsId.text = "#" + state.id
            binding.pokemonDetailsNombre.text = state.nombre
            binding.pokemonDetailsHeight.text = state.height
            binding.pokemonDetailsWeight.text = state.weight
            // Imagen principal animada
            state.imageUrl?.let {
                binding.pokemonImage.setBackgroundColor(Color.TRANSPARENT)
                binding.pokemonImage.alpha = 0f
                binding.pokemonImage.scaleX = 0.92f
                binding.pokemonImage.scaleY = 0.92f
                Glide.with(requireContext())
                    .load(it)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(binding.pokemonImage)
                binding.pokemonImage.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(500)
                    .setStartDelay(100)
                    .start()
            }
            // Cambiar icono de favorito según el estado
            if (state.isFavorite) {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_outline)
            }
            // Fondo gradiente animado
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
            gradientBg.alpha = 0f
            gradientBg.animate().alpha(1f).setDuration(600).start()

            // Mostrar tipos (máximo 2 chips) con animación
            val chips = listOf(binding.pokemonTypeChip1, binding.pokemonTypeChip2)
            chips.forEach { chip -> chip.visibility = View.GONE }
            state.types.take(2).forEachIndexed { idx, typeUi ->
                val chip = chips[idx]
                chip.visibility = View.VISIBLE
                chip.text = if (typeUi.stringRes != 0) getString(typeUi.stringRes)
                    else typeUi.name.replaceFirstChar { it.uppercase() }
                val realColor = ContextCompat.getColor(requireContext(), typeUi.color)
                chip.chipBackgroundColor = ColorStateList.valueOf(realColor)
                chip.setTextColor(Color.WHITE)
                chip.scaleX = 0.9f
                chip.scaleY = 0.9f
                chip.alpha = 0f
                chip.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(350).setStartDelay(200L * idx).start()
            }

            // Mostrar habilidades (máximo 3) con animación
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
                triple.first.alpha = 0f
                triple.first.translationY = 40f
                triple.first.animate().alpha(1f).translationY(0f).setDuration(400).setStartDelay(250L * idx).start()
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
                val resId = com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain(
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

            // Mostrar la cadena de evolución (imágenes y nombres) con animación
            val evolutionContainer = binding.evolutionChainContainer
            evolutionContainer.removeAllViews()
            val context = requireContext()
            val evolutionDetails = state.evolutionChainDetails
            for ((idx, poke) in evolutionDetails.withIndex()) {
                val pokeLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(8)
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    setOnClickListener {
                        poke.id?.let { pokemonId ->
                            navigateToPokemon(pokemonId)
                        }
                    }
                    isClickable = true
                    isFocusable = true
                    background = ContextCompat.getDrawable(context, R.drawable.ripple_effect)
                    alpha = 0f
                    scaleX = 0.92f
                    scaleY = 0.92f
                }
                val imageView = ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1f
                    ).apply {
                        height = 0
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                    }
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    minimumHeight = 80
                }
                val nameView = TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = poke.name?.replaceFirstChar { it.uppercase() } ?: ""
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                    textSize = 12f
                    maxLines = 2
                    isSingleLine = false
                    setLineSpacing(0f, 1.1f)
                    ellipsize = null
                }
                val imageUrl = poke.sprites?.other?.`official-artwork`?.front_default
                    ?: poke.sprites?.front_default
                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_pokeball)
                    .into(imageView)
                pokeLayout.addView(imageView, 0)
                pokeLayout.addView(nameView, 1)
                evolutionContainer.addView(pokeLayout)
                // Animación vistosa para cada Pokémon de la cadena
                pokeLayout.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setStartDelay(120L * idx)
                    .start()
                // Flecha entre pokémon (excepto el último)
                if (idx < evolutionDetails.size - 1) {
                    val arrow = ImageView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            40, 40
                        ).apply {
                            setMargins(0, 0, 0, 0)
                        }
                        setImageResource(R.drawable.ic_arrow_right)
                        setColorFilter(ContextCompat.getColor(context, R.color.text_secondary))
                        alpha = 0f
                    }
                    evolutionContainer.addView(arrow)
                    arrow.animate().alpha(1f).setDuration(300).setStartDelay(120L * idx + 80).start()
                }
            }
        }
    }
    
    /**
     * Navega a la información de otro Pokémon al hacer clic en la cadena evolutiva.
     */
    private fun navigateToPokemon(pokemonId: Int) {
        // Crear una nueva instancia del fragmento con el ID del Pokémon seleccionado
        val fragment = newInstance(pokemonId)
        
        // Obtener el ID del contenedor de fragmentos actual
        val currentView = view
        if (currentView != null) {
            val parent = currentView.parent as? ViewGroup
            val containerId = parent?.id
            
            if (containerId != null && containerId != View.NO_ID) {
                // Si encontramos un ID válido, usarlo para reemplazar el fragmento
                parentFragmentManager.beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                // Si no podemos encontrar un ID válido, usar una estrategia alternativa:
                // Reemplazar este fragmento por el nuevo
                parentFragmentManager.beginTransaction()
                    .replace(this.id, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            // Fallback más seguro: simplemente agregar un nuevo fragmento encima
            parentFragmentManager.beginTransaction()
                .add(fragment, "pokemon_detail_$pokemonId")
                .addToBackStack(null)
                .commit()
        }
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
        fun newInstance(pokemonId: Int) = PokemonInfoFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
