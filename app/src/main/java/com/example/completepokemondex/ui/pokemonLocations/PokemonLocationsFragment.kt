package com.example.completepokemondex.ui.pokemonLocations

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonEncountersBinding
import com.example.completepokemondex.util.PokemonLocationsUtil
import com.example.completepokemondex.util.PokemonTypeUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento encargado de mostrar las localizaciones donde se puede encontrar un Pokémon
 * en los juegos Rojo y Azul, resaltando las ubicaciones en un mapa y mostrando detalles de encuentros.
 */
@AndroidEntryPoint
class PokemonLocationsFragment : Fragment() {
    private var _binding: FragmentPokemonEncountersBinding? = null
    // Modificar el getter del binding para evitar NullPointerException
    private val binding get() = _binding ?: throw IllegalStateException("Binding no puede ser nulo")

    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }

    private val viewModel: PokemonLocationsVIewModel by viewModels()
    
    // Para mantener una referencia al mapa base
    private var baseMapBitmap: Bitmap? = null

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
        ImageView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Cargar la imagen del mapa base primero
        loadBaseMap()
        
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            // Mostrar el gif de carga y ocultar el contenido mientras isLoading es true
            if (state.isLoading) {
                _binding?.loadingIndicator?.visibility = View.VISIBLE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading_pokeball)
                    .into(_binding!!.loadingIndicator)
                _binding?.encountersTitle?.visibility = View.GONE
                _binding?.encountersDescription?.visibility = View.GONE
                _binding?.encountersText?.visibility = View.GONE
                _binding?.encountersEmpty?.visibility = View.GONE
                _binding?.mapImage?.visibility = View.GONE
                return@observe
            } else {
                _binding?.loadingIndicator?.visibility = View.GONE
                _binding?.encountersTitle?.visibility = View.VISIBLE
                _binding?.encountersDescription?.visibility = View.VISIBLE
                _binding?.mapImage?.visibility = View.VISIBLE
            }

            // Gestionar estado de carga
            _binding?.loadingIndicator?.isVisible = state.isLoading

            // Actualizar título
            _binding?.encountersTitle?.text = getString(R.string.encounters_title_template, getString(R.string.encounters_red_blue))

            // Solo aplica el gradiente si pokemonTypes no es nulo
            if (state.pokemonTypes != null) {
                setupGradientBackground(state.pokemonTypes)
            }
            // No hay else/fallback, así se evita el fondo azul inicial

            // Si tenemos ubicaciones, actualizar el mapa para resaltarlas
            if (state.locationNames.isNotEmpty() && baseMapBitmap != null) {
                updateMapWithLocations(state.locationNames)
            }

            // Mostrar texto de encuentros o mensaje de vacío
            if (state.hasEncounters) {
                _binding?.encountersEmpty?.visibility = View.GONE
                _binding?.encountersText?.visibility = View.VISIBLE
                
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
                
                _binding?.encountersText?.text = textBuilder
            } else {
                _binding?.encountersText?.visibility = View.GONE
                _binding?.encountersEmpty?.visibility = View.VISIBLE
                _binding?.encountersEmpty?.text = if (state.error != null) 
                    "Error al cargar las ubicaciones: ${state.error}" 
                else 
                    "No se encontraron lugares de encuentro para este Pokémon en los juegos Rojo y Azul."
            }
        }
    }
    
    /**
     * Carga la imagen base del mapa de Kanto.
     */
    private fun loadBaseMap() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Cargar el mapa base desde los recursos
                val inputStream = resources.openRawResource(R.drawable.map)
                baseMapBitmap = BitmapFactory.decodeStream(inputStream)
                
                // Mostrar el mapa base inicialmente
                withContext(Dispatchers.Main) {
                    _binding?.let { binding ->
                        Glide.with(requireContext())
                            .asBitmap()
                            .load(baseMapBitmap)
                            .apply(RequestOptions()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                            .transition(BitmapTransitionOptions.withCrossFade())
                            .into(binding.mapImage)
                    }
                }
            } catch (e: Exception) {
                Log.e("PokemonLocationsFragment", "Error al cargar el mapa base: ${e.message}")
                // Cargar el mapa normalmente si falla
                withContext(Dispatchers.Main) {
                    _binding?.let { binding ->
                        Glide.with(requireContext())
                            .load(R.drawable.map)
                            .apply(RequestOptions().fitCenter())
                            .into(binding.mapImage)
                    }
                }
            }
        }
    }
    
    /**
     * Actualiza el mapa resaltando las ubicaciones donde aparece el Pokémon.
     */
    private fun updateMapWithLocations(locationNames: List<String>) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (baseMapBitmap != null) {
                    // Usar PokemonLocationsUtil para resaltar las ubicaciones en el mapa
                    val highlightedMap = baseMapBitmap?.let {
                        PokemonLocationsUtil.highlightLocations(
                            requireContext(),
                            it,
                            locationNames
                        )
                    }
                    
                    // Mostrar el mapa actualizado en la UI
                    withContext(Dispatchers.Main) {
                        _binding?.let { binding ->
                            if (highlightedMap != null) {
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(highlightedMap)
                                    .apply(RequestOptions()
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true))
                                    .transition(BitmapTransitionOptions.withCrossFade())
                                    .into(binding.mapImage)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("PokemonLocationsFragment", "Error al resaltar ubicaciones: ${e.message}")
            }
        }
    }

    /**
     * Configura el fondo con un gradiente basado en los tipos del Pokémon.
     */
    private fun setupGradientBackground(types: List<String>) {
        val typeColors = types.take(2).map { typeName ->
            ContextCompat.getColor(requireContext(), PokemonTypeUtil.getTypeColorResId(typeName))
        }

        val gradientColors = when {
            typeColors.isEmpty() -> intArrayOf(Color.LTGRAY, Color.LTGRAY)
            typeColors.size == 1 -> intArrayOf(typeColors[0], typeColors[0])
            else -> typeColors.toIntArray()
        }

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            gradientColors
        )
        gradientDrawable.cornerRadius = 0f

        _binding?.pokemonEncountersGradientBg?.background = gradientDrawable
    }

    /**
     * Libera recursos y el binding al destruir la vista.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Liberar recursos
        baseMapBitmap?.recycle()
        baseMapBitmap = null
        _binding = null
    }

    companion object {
        /**
         * Crea una nueva instancia del fragmento con el ID del Pokémon.
         */
        fun newInstance(pokemonId: Int) = PokemonLocationsFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
