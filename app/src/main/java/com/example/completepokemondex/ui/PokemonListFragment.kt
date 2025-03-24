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
import androidx.recyclerview.widget.RecyclerView
import com.example.completepokemondex.R
import com.example.completepokemondex.data.local.database.PokedexDatabase
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
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonListAdapter
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView(view)
        observeViewModel()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.pokemon_list_recycler_view)
        adapter = PokemonListAdapter { pokemon ->
            // Manejo de clic en un Pokémon (para implementar navegación al detalle)
            Toast.makeText(context, "Pokémon seleccionado: ${pokemon.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        // Configurar paginación al hacer scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                if (!isLoading) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        loadMorePokemon()
                    }
                }
            }
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        is PokemonListViewModel.UiState.Loading -> {
                            isLoading = true
                            // Se podría mostrar un indicador de carga aquí
                        }
                        is PokemonListViewModel.UiState.Success -> {
                            isLoading = false
                            adapter.submitList(state.pokemons)
                        }
                        is PokemonListViewModel.UiState.Error -> {
                            isLoading = false
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

    private fun loadMorePokemon() {
        isLoading = true
        viewModel.loadMorePokemon()
    }
}