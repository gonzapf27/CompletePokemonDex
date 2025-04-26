package com.example.completepokemondex.ui.pokemonMoves

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonMoveDomain
import com.example.completepokemondex.databinding.FragmentPokemonMovesBinding
import com.example.completepokemondex.ui.adapters.PokemonMoveListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PokemonMovesFragment : Fragment() {
    private var _binding: FragmentPokemonMovesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonMovesViewModel by viewModels()
    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }
    
    @Inject
    lateinit var moveListAdapter: PokemonMoveListAdapter

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

        setupRecyclerView()

        Glide.with(this)
            .asGif()
            .load(R.drawable.loading_pokeball)
            .into(binding.loadingIndicator)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.loadingIndicator.isVisible = state.isLoading || state.isLoadingMoveDetails

            binding.movesCard.isVisible = !state.isLoading

            binding.movesEmpty.isVisible =
                state.sections.isEmpty() && !state.isLoading && !state.isLoadingMoveDetails

            state.pokemonTypes?.let { types ->
                setupGradientBackground(types)
            }

            if (!state.isLoading && !state.isLoadingMoveDetails) {
                processMoveSections(state.sections)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.movesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moveListAdapter
        }
        
        moveListAdapter.setOnMoveClickListener { move ->
            // Implementar acción cuando se hace clic en un movimiento
        }
    }

    private fun processMoveSections(sections: List<MovesSectionUi>) {
        val items = mutableListOf<PokemonMoveListAdapter.ListItem>()
        sections.forEach { section ->
            items.add(PokemonMoveListAdapter.ListItem.SectionHeader(section.title))
            section.moves.forEach { moveUi ->
                val moveDomain = viewModel.getMoveDetails(moveUi.moveId)
                if (moveDomain != null) {
                    items.add(
                        PokemonMoveListAdapter.ListItem.MoveItem(
                            move = moveDomain,
                            learnMethod = section.title
                        )
                    )
                }
            }
        }
        moveListAdapter.submitList(items)
        binding.movesEmpty.isVisible = items.none { it is PokemonMoveListAdapter.ListItem.MoveItem }
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
