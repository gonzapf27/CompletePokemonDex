package com.example.completepokemondex.ui.pokemonMoves

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonMoveDomain
import com.example.completepokemondex.databinding.FragmentPokemonMovesBinding
import com.example.completepokemondex.ui.adapters.PokemonMoveAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonMovesFragment : Fragment() {
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

    companion object {
        fun newInstance(pokemonId: Int) = PokemonMovesFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }

    private lateinit var binding: FragmentPokemonMovesBinding
    private val viewModel: PokemonMovesViewModel by viewModels()
    private lateinit var moveAdapter: PokemonMoveAdapter
    private var firstLoadDone = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonMovesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        val pokemonId = arguments?.getInt("pokemon_id") ?: 0
        viewModel.setPokemonId(pokemonId)
    }

    private fun setupRecyclerView() {
        moveAdapter = PokemonMoveAdapter(viewModel)
        binding.movesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moveAdapter
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (totalItemCount <= lastVisibleItem + 5) {
                        viewModel.loadMoreMoves()
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.moves.observe(viewLifecycleOwner) { moves ->
            Log.d("MovesFragment", "moves observer: moves.size=${moves.size}, isLoading=${viewModel.isLoading.value}, firstLoadDone=$firstLoadDone")
            moveAdapter.submitList(moves)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("MovesFragment", "isLoading observer: isLoading=$isLoading, firstLoadDone=$firstLoadDone, moves.size=${moveAdapter.currentList.size}")
            if (isLoading && moveAdapter.currentList.isEmpty()) {
                Log.d("MovesFragment", "Mostrando loading de pantalla completa")
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.movesRecyclerView.visibility = View.GONE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading_pokeball)
                    .into(binding.loadingIndicator)
            } else {
                Log.d("MovesFragment", "Ocultando loading de pantalla completa")
                binding.loadingIndicator.visibility = View.GONE
                binding.movesRecyclerView.visibility = View.VISIBLE
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                Log.d("MovesFragment", "Error: $errorMsg")
                // Puedes mostrar un Toast o Snackbar
            }
        }
    }
}
