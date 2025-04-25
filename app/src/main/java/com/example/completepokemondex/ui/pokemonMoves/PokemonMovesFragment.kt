package com.example.completepokemondex.ui.pokemonMoves

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonMovesBinding
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
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.loadingIndicator.isVisible = state.isLoading
            binding.movesEmpty.isVisible = state.sections.isEmpty() && !state.isLoading

            val container = binding.movesSectionsContainer
            container.removeAllViews()

            for (section in state.sections) {
                val sectionTitle = TextView(requireContext()).apply {
                    text = section.title
                    setPadding(0, 24, 0, 8)
                }
                container.addView(sectionTitle)

                val movesList = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                }
                for (move in section.moves) {
                    val moveView = TextView(requireContext()).apply {
                        text = move
                        setPadding(16, 8, 16, 8)
                    }
                    movesList.addView(moveView)
                }
                container.addView(movesList)
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
