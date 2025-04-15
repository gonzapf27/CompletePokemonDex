package com.example.completepokemondex.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.completepokemondex.PokemonDetallesMainFragment
import com.example.completepokemondex.R
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonListBinding
import com.example.completepokemondex.ui.adapters.PokemonListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragmento que muestra una lista de Pokémon.
 * Este fragmento recupera y presenta los datos de los Pokémon en un RecyclerView,
 * permitiendo la selección de un Pokémon específico para ver sus detalles.
 */
class PokemonListFragment : Fragment() {

    companion object {
        /**
         * Crea una nueva instancia de PokemonListFragment.
         * @return Una nueva instancia del fragmento.
         */
        fun newInstance() = PokemonListFragment()
    }

    /**
     * ViewModel que gestiona los datos y la lógica de negocio para este fragmento.
     * Se inicializa con la base de datos de Pokédex.
     */
    private val viewModel: PokemonListViewModel by viewModels {
        PokemonListViewModel.Factory(PokedexDatabase.getDatabase(requireContext())) 
    }
    
    /**
     * Binding para acceder a las vistas del layout.
     * Se inicializa en onCreateView y se limpia en onDestroyView.
     */
    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!

    /**
     * Adaptador para el RecyclerView que muestra la lista de Pokémon.
     */
    private lateinit var adapter: PokemonListAdapter

    /**
     * Infla el layout del fragmento y configura el binding.
     * @param inflater Utilizado para inflar el layout.
     * @param container Contenedor donde se añadirá la vista.
     * @param savedInstanceState Estado guardado del fragmento.
     * @return La vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se llama después de que la vista se haya creado.
     * Configura el RecyclerView y observa los cambios en el ViewModel.
     * @param view La vista creada por onCreateView.
     * @param savedInstanceState Estado guardado del fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    /**
     * Configura el RecyclerView con el adaptador y el layout manager.
     * Define el comportamiento cuando se selecciona un Pokémon.
     */
    private fun setupRecyclerView() {
        adapter = PokemonListAdapter { pokemon ->
            val fragmentoDetalles = PokemonDetallesMainFragment.newInstance(pokemon.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragmentoDetalles)
                .addToBackStack(null)
                .commit()
        }

        binding.pokemonListRecyclerView.adapter = adapter
        binding.pokemonListRecyclerView.layoutManager = LinearLayoutManager(context)
    }
    
    /**
     * Configura la barra de búsqueda para filtrar la lista de Pokémon por nombre.
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateSearchQuery(newText ?: "")
                return true
            }
        })
        
        // Botón de cierre para limpiar la búsqueda
        binding.searchView.setOnCloseListener {
            viewModel.updateSearchQuery("")
            false
        }
    }

    /**
     * Observa los cambios en el estado de la UI del ViewModel y actualiza la interfaz
     * de acuerdo al estado actual (cargando, éxito o error).
     */
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

    /**
     * Se llama cuando la vista del fragmento está siendo destruida.
     * Limpia la referencia al binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}