package com.example.completepokemondex.ui.pokemonList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.completepokemondex.R
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonListBinding
import com.example.completepokemondex.ui.pokemonList.PokemonListViewModel
import com.example.completepokemondex.ui.adapters.PokemonListAdapter
import com.example.completepokemondex.ui.adapters.PokemonTypeAdapter
import com.example.completepokemondex.ui.pokemonSeleccionado.PokemonDetallesMainFragment
import com.example.completepokemondex.util.PokemonTypeUtil
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
        PokemonListViewModel.Factory(PokedexDatabase.Companion.getDatabase(requireContext()))
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
    private lateinit var pokemonAdapter: PokemonListAdapter

    /**
     * Adaptador para el RecyclerView que muestra los tipos de Pokémon.
     */
    private lateinit var typeAdapter: PokemonTypeAdapter

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

        setupPokemonRecyclerView()
        setupTypeRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    /**
     * Configura el RecyclerView con el adaptador y el layout manager.
     * Define el comportamiento cuando se selecciona un Pokémon.
     */
    private fun setupPokemonRecyclerView() {
        pokemonAdapter = PokemonListAdapter(
            onItemClicked = { pokemon ->
                val fragmentoDetalles =
                    PokemonDetallesMainFragment.Companion.newInstance(pokemon.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragmentoDetalles)
                    .addToBackStack(null)
                    .commit()
            },
            onFavoriteClicked = { pokemon ->
                viewModel.toggleFavorite(pokemon)
            }
        )

        binding.pokemonListRecyclerView.adapter = pokemonAdapter
        val layoutManager = LinearLayoutManager(context)
        binding.pokemonListRecyclerView.layoutManager = layoutManager

        // Añadir listener para detectar el scroll al final de la lista
        binding.pokemonListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Si está scrolleando hacia abajo (dy > 0)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    // Si está cerca del final de la lista (últimos 5 elementos)
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 5) {
                        // Verificar que no esté actualmente cargando y que pueda cargar más
                        lifecycleScope.launch {
                            if (!viewModel.isLoadingMore.value && viewModel.canLoadMore.value) {
                                viewModel.loadMorePokemon()
                            }
                        }
                    }
                }
            }
        })
    }

    /**
     * Configura el RecyclerView de tipos de Pokémon.
     */
    private fun setupTypeRecyclerView() {
        typeAdapter = PokemonTypeAdapter(
            types = PokemonTypeUtil.allTypes,
            onTypeSelected = { typeName ->
                viewModel.updateSelectedType(typeName)
            }
        )
        binding.typeFilterRecyclerView.adapter = typeAdapter
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
                            pokemonAdapter.submitList(state.pokemons)
                            // Actualizar tipos en el adaptador
                            val typesMap = viewModel.getPokemonTypesMap()
                            pokemonAdapter.updatePokemonTypes(typesMap)
                        }
                        is PokemonListViewModel.UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.pokemonListRecyclerView.visibility = View.VISIBLE
                            Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                            // Si hay datos en caché, los mostramos
                            state.pokemons?.let {
                                if (it.isNotEmpty()) {
                                    pokemonAdapter.submitList(it)
                                    val typesMap = viewModel.getPokemonTypesMap()
                                    pokemonAdapter.updatePokemonTypes(typesMap)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Observar el estado de carga de más pokémon
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingMore.collect { isLoading ->
                    // Mostrar/ocultar indicador de carga en la parte inferior
                    binding.loadMoreProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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