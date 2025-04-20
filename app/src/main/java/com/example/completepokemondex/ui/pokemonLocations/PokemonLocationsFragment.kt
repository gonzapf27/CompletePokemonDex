package com.example.completepokemondex.ui.pokemonLocations

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonEncountersBinding
import com.example.completepokemondex.util.PokemonTypeUtil
import dagger.hilt.android.AndroidEntryPoint
import android.widget.ImageView

@AndroidEntryPoint
class PokemonLocationsFragment : Fragment() {
    private var _binding: FragmentPokemonEncountersBinding? = null
    private val binding get() = _binding!!

    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }

    private val viewModel: PokemonLocationsVIewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPokemonId(pokemonId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonEncountersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Crear una ImageView temporal para mostrar la animación de carga
        val loadingImageView = ImageView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        
        // Cargar la imagen del mapa con opciones mejoradas para asegurar que no se corte
        Glide.with(this)
            .load(R.drawable.map)
            .apply(RequestOptions()
                .fitCenter()
                .override(1024, 768)) // Usar una resolución más alta para mejor calidad
            .into(binding.mapImage)
        
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            // Gestionar estado de carga
            if (state.isLoading) {
                binding.loadingIndicator.visibility = View.VISIBLE
                
                // Usar Glide para cargar el GIF de carga en un ImageView temporal y luego configurar el ProgressBar
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading_pokeball)
                    .into(loadingImageView)
                
                return@observe
            } else {
                binding.loadingIndicator.visibility = View.GONE
            }

            // Actualizar título
            binding.encountersTitle.text = "Lugares en Rojo/Azul: ${state.nombre}"

            // Establecer fondo con el tipo de Pokémon (usando agua como predeterminado para las ubicaciones)
            val waterType = PokemonTypeUtil.getTypeByName("water")
            val color = ContextCompat.getColor(requireContext(), waterType.colorRes)
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(color, Color.LTGRAY)
            )
            binding.pokemonEncountersGradientBg.background = gradientDrawable

            // Mostrar texto de encuentros o mensaje de vacío
            if (state.hasEncounters) {
                binding.encountersEmpty.visibility = View.GONE
                binding.encountersText.visibility = View.VISIBLE
                
                // Formatear y mostrar los datos de encuentro
                val textBuilder = SpannableStringBuilder()
                
                state.encounters.forEachIndexed { index, encounter ->
                    // Nombre de la ubicación en negrita
                    val locationText = SpannableString(encounter.locationName)
                    locationText.setSpan(StyleSpan(Typeface.BOLD), 0, locationText.length, 0)
                    textBuilder.append(locationText).append("\n")
                    
                    // Juegos
                    val gamesText = "Juegos: ${encounter.games.joinToString(", ")}"
                    textBuilder.append(gamesText).append("\n")
                    
                    // Niveles y probabilidad
                    textBuilder.append("${encounter.levels} • Prob: ${encounter.chance}").append("\n")
                    
                    // Métodos de encuentro
                    if (encounter.methods.isNotEmpty()) {
                        textBuilder.append("Métodos: ${encounter.methods.joinToString(", ")}").append("\n")
                    }
                    
                    // Separador entre ubicaciones (excepto la última)
                    if (index < state.encounters.size - 1) {
                        textBuilder.append("\n")
                    }
                }
                
                binding.encountersText.text = textBuilder
            } else {
                binding.encountersText.visibility = View.GONE
                binding.encountersEmpty.visibility = View.VISIBLE
                binding.encountersEmpty.text = if (state.error != null) 
                    "Error al cargar las ubicaciones: ${state.error}" 
                else 
                    "No se encontraron lugares de encuentro para este Pokémon en los juegos Rojo y Azul."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pokemonId: Int) = PokemonLocationsFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
