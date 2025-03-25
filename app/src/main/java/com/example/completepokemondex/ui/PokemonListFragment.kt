package com.example.completepokemondex.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonListBinding
import com.example.completepokemondex.ui.adapters.PokemonListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonListFragment : Fragment() {

    companion object {
        fun newInstance() = PokemonListFragment()
    }

    private val viewModel: PokemonListViewModel by viewModels { 
        PokemonListViewModel.Factory(PokedexDatabase.getDatabase(requireContext())) 
    }
    
    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = PokemonListAdapter { pokemon ->
            // Manejo de clic en un Pokémon (para implementar navegación al detalle)
            Toast.makeText(context, "Pokémon seleccionado: ${pokemon.name}, ${pokemon.id}", Toast.LENGTH_SHORT).show()
            // fetch pokemon details
            viewModel.fetchPokemonDetails(pokemon.id)
        }

        binding.pokemonListRecyclerView.adapter = adapter
        binding.pokemonListRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        is PokemonListViewModel.UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.pokemonListRecyclerView.visibility = View.GONE
                        }
                        is PokemonListViewModel.UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.pokemonListRecyclerView.visibility = View.VISIBLE
                            adapter.submitList(state.pokemons)
                        }
                        is PokemonListViewModel.UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.pokemonListRecyclerView.visibility = View.VISIBLE
                            Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                            // Si hay datos en caché, los mostramos
                            state.pokemons?.let {
                                if (it.isNotEmpty()) {
                                    adapter.submitList(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}