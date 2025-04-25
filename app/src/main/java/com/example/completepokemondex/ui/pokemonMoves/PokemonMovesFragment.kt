package com.example.completepokemondex.ui.pokemonMoves

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonMovesBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.card.MaterialCardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

@AndroidEntryPoint
class PokemonMovesFragment : Fragment() {
    private var _binding: FragmentPokemonMovesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonMovesViewModel by viewModels()
    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPokemonId(pokemonId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonMovesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Configurar la imagen de carga como GIF animado
        Glide.with(this)
            .asGif()
            .load(R.drawable.loading_pokeball)
            .into(binding.loadingIndicator)
        
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            // Mostrar/ocultar la imagen de carga mientras cualquier proceso de carga esté activo
            binding.loadingIndicator.isVisible = state.isLoading || state.isLoadingMoveDetails
            
            // Cuando está cargando los datos iniciales, ocultamos el contenido
            binding.movesCard.isVisible = !state.isLoading
            
            // Mostrar mensaje de vacío solo cuando no hay secciones y ha terminado de cargar
            binding.movesEmpty.isVisible = state.sections.isEmpty() && !state.isLoading && !state.isLoadingMoveDetails

            // Si ya tenemos secciones, actualizar la UI aunque estemos cargando detalles
            if (!state.isLoading && state.sections.isNotEmpty()) {
                val container = binding.movesSectionsContainer
                container.removeAllViews()

                for (section in state.sections) {
                    // Crear un título de sección
                    val sectionTitleView = TextView(requireContext()).apply {
                        text = section.title
                        textSize = 18f
                        setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                        setPadding(16, 32, 16, 12)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }
                    container.addView(sectionTitleView)

                    // Crear contenedor de movimientos
                    val movesList = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(16, 8, 16, 16)
                    }
                    
                    // Añadir cada movimiento a la lista
                    for (move in section.moves) {
                        val moveCardView = MaterialCardView(requireContext()).apply {
                            radius = resources.getDimension(R.dimen.card_corner_radius)
                            strokeWidth = 1
                            strokeColor = ContextCompat.getColor(context, R.color.divider)
                            cardElevation = resources.getDimension(R.dimen.card_elevation_small)
                            setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_bg))
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 8, 0, 8)
                            }
                        }
                        
                        val moveTextView = TextView(requireContext()).apply {
                            text = move
                            textSize = 16f
                            setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                            setPadding(24, 16, 24, 16)
                        }
                        
                        moveCardView.addView(moveTextView)
                        movesList.addView(moveCardView)
                    }
                    container.addView(movesList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pokemonId: Int) = PokemonMovesFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
