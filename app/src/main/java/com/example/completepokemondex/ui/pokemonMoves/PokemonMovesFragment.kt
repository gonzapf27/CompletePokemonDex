package com.example.completepokemondex.ui.pokemonMoves

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonMovesBinding
import com.example.completepokemondex.ui.adapters.PokemonMoveAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento encargado de mostrar la lista de movimientos de un Pokémon.
 * Utiliza un RecyclerView y observa el ViewModel para actualizar la UI.
 */
@AndroidEntryPoint
class PokemonMovesFragment : Fragment() {
    companion object {
        /**
         * Crea una nueva instancia del fragmento con el ID del Pokémon.
         */
        fun newInstance(pokemonId: Int) = PokemonMovesFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }

    private lateinit var binding: FragmentPokemonMovesBinding
    private val viewModel: PokemonMovesViewModel by viewModels()
    private lateinit var moveAdapter: PokemonMoveAdapter

    /**
     * Inicializa el binding y la vista del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonMovesBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura el RecyclerView y observa los cambios del ViewModel.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        val pokemonId = arguments?.getInt("pokemon_id") ?: 0
        viewModel.setPokemonId(pokemonId)
        // Fondo gradiente según los tipos del Pokémon
        viewModel.pokemonTypes.observe(viewLifecycleOwner) { types ->
            val typeColors = types.take(2).map {
                val type = com.example.completepokemondex.util.PokemonTypeUtil.getTypeByName(it)
                ContextCompat.getColor(requireContext(), type.colorRes)
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
            binding.movesFragmentGradientBg.background = gradientDrawable
        }
    }

    /**
     * Configura el RecyclerView para mostrar los movimientos y manejar la paginación.
     */
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

    /**
     * Observa los LiveData del ViewModel para actualizar la UI según los cambios de datos, carga y errores.
     */
    private fun observeViewModel() {
        viewModel.moves.observe(viewLifecycleOwner) { moves ->
            moveAdapter.submitList(moves)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading && moveAdapter.currentList.isEmpty()) {
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.contentContainer.visibility = View.GONE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading_pokeball)
                    .into(binding.loadingIndicator)
            } else {
                binding.loadingIndicator.visibility = View.GONE
                binding.contentContainer.visibility = View.VISIBLE
            }
        }
    }
}
