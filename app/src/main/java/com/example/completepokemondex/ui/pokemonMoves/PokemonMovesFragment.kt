package com.example.completepokemondex.ui.pokemonMoves

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonMovesBinding
import com.example.completepokemondex.databinding.ItemMoveBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

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

            // Configurar el fondo gradiente basado en los tipos de Pokémon
            state.pokemonTypes?.let { types ->
                setupGradientBackground(types)
            }

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
                        val moveItemView = LayoutInflater.from(requireContext())
                            .inflate(R.layout.item_move, movesList, false)
                        
                        // Configurar la tarjeta del movimiento
                        val moveCard = moveItemView.findViewById<MaterialCardView>(R.id.move_card)
                        val moveTypeChip = moveItemView.findViewById<Chip>(R.id.move_type_chip)
                        val moveName = moveItemView.findViewById<TextView>(R.id.move_name)
                        val movePowerContainer = moveItemView.findViewById<LinearLayout>(R.id.move_power_container)
                        val movePower = moveItemView.findViewById<TextView>(R.id.move_power)
                        
                        // Establecer el nombre del movimiento
                        moveName.text = move.name
                        
                        // Configurar el tipo y colores
                        val typeString = move.type ?: "normal"
                        moveTypeChip.text = typeString.uppercase()
                        
                        // Configurar el color del tipo
                        val typeColorResId = getTypeColorResId(typeString)
                        val typeColor = ContextCompat.getColor(requireContext(), typeColorResId)
                        moveTypeChip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(typeColor)
                        
                        // Aplicar un borde de color al movCard
                        moveCard.strokeColor = typeColor
                        
                        // Mostrar el poder del movimiento si está disponible
                        if (move.power != null && move.power > 0) {
                            movePowerContainer.visibility = View.VISIBLE
                            movePower.text = move.power.toString()
                        } else {
                            movePowerContainer.visibility = View.GONE
                        }
                        
                        movesList.addView(moveItemView)
                    }
                    container.addView(movesList)
                }
            }
        }
    }
    
    private fun setupGradientBackground(types: List<String>) {
        val typeColors = types.take(2).map { typeName ->
            ContextCompat.getColor(requireContext(), getTypeColorResId(typeName))
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
        
        binding.pokemonMovesGradientBg.background = gradientDrawable
    }
    
    private fun getTypeColorResId(type: String): Int {
        return when (type.lowercase()) {
            "normal" -> R.color.type_normal
            "fire" -> R.color.type_fire
            "water" -> R.color.type_water
            "electric" -> R.color.type_electric
            "grass" -> R.color.type_grass
            "ice" -> R.color.type_ice
            "fighting" -> R.color.type_fighting
            "poison" -> R.color.type_poison
            "ground" -> R.color.type_ground
            "flying" -> R.color.type_flying
            "psychic" -> R.color.type_psychic
            "bug" -> R.color.type_bug
            "rock" -> R.color.type_rock
            "ghost" -> R.color.type_ghost
            "dragon" -> R.color.type_dragon
            "dark" -> R.color.type_dark
            "steel" -> R.color.type_steel
            "fairy" -> R.color.type_fairy
            else -> R.color.type_normal
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
