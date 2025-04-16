package com.example.completepokemondex.ui.pokemonList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    // Definir el estado de UI como sealed class
    sealed class UiState {
        data object Loading : UiState()
        data class Success(val pokemons: List<PokemonDomain>) : UiState()
        data class Error(val message: String, val pokemons: List<PokemonDomain>? = null) :
            UiState()
    }

    // Estado de UI como StateFlow para ser observado por el Fragment
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Lista completa de Pokémon cargada
    private var allPokemonList: List<PokemonDomain> = emptyList()

    // Término de búsqueda actual
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtro de tipo actual
    private val _selectedType = MutableStateFlow("all")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()

    // Mapeo de cada pokémon a sus tipos
    private val pokemonTypes = mutableMapOf<Int, List<String>>()

    // Parámetros de paginación
    private val pageSize = 30 // Tamaño de página
    private var currentOffset = 0 // Offset actual
    private val maxPokemonCount = 150 // Máximo número de Pokémon a cargar
    private var isLoading = false // Flag para controlar múltiples cargas
    private val _isLoadingMore =
        MutableStateFlow(false) // Estado específico para cargas adicionales
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()
    private val _canLoadMore = MutableStateFlow(true) // Indica si se pueden cargar más Pokémon
    val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    init {
        loadPokemonList()
    }

    /** Carga la lista inicial de Pokémon */
    fun loadPokemonList() {
        _uiState.value = UiState.Loading
        fetchPokemonList(pageSize, currentOffset)
    }

    /** Carga más Pokémon si es posible */
    fun loadMorePokemon() {
        if (isLoading || currentOffset >= maxPokemonCount || !_canLoadMore.value) return

        Log.d("PokemonListViewModel", "Cargando más pokémon desde offset: $currentOffset")
        isLoading = true
        _isLoadingMore.value = true
        fetchPokemonList(pageSize, currentOffset)
    }

    /** Actualiza el término de búsqueda y filtra la lista */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    /** Actualiza el tipo seleccionado y filtra la lista */
    fun updateSelectedType(type: String) {
        _selectedType.value = type
        applyFilters()
    }

    /** Aplica los filtros actuales (búsqueda y tipo) a la lista de Pokémon */
    private fun applyFilters() {
        if (allPokemonList.isEmpty()) return

        val searchQuery = _searchQuery.value
        val selectedType = _selectedType.value

        // Filtrar por nombre
        var filteredList = if (searchQuery.isEmpty()) {
            allPokemonList
        } else {
            allPokemonList.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }

        // Filtrar por tipo si no es "all"
        if (selectedType != "all") {
            filteredList = filteredList.filter { pokemon ->
                pokemonTypes[pokemon.id]?.contains(selectedType) == true
            }
        }

        // Si la lista filtrada está casi vacía y podemos cargar más Pokémon, intentar cargar más
        if (filteredList.size < 10 && _canLoadMore.value && !isLoading && currentOffset < maxPokemonCount) {
            loadMorePokemon()
        }

        _uiState.value = UiState.Success(filteredList)
    }

    /** Obtiene la lista de Pokémon del repositorio */
    private fun fetchPokemonList(limit: Int, offset: Int) {
        viewModelScope.launch {
            repository.getPokemonList(limit, offset).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        if (offset == 0) {
                            _uiState.value = UiState.Loading
                        }
                    }

                    is Resource.Success -> {
                        val pokemonList = result.data
                        Log.d("PokemonListViewModel", "Obtenidos ${pokemonList.size} pokémon desde offset: $offset")

                        // Actualizar el offset para la próxima carga
                        currentOffset += pokemonList.size

                        // Verificar si se ha alcanzado el límite máximo
                        if (currentOffset >= maxPokemonCount || pokemonList.isEmpty()) {
                            _canLoadMore.value = false
                            Log.d("PokemonListViewModel", "No se pueden cargar más pokémon: offset=$currentOffset, max=$maxPokemonCount")
                        }

                        loadPokemonImages(pokemonList)
                    }

                    is Resource.Error -> {
                        _uiState.value = UiState.Error(result.message, result.data)
                        isLoading = false
                        _isLoadingMore.value = false
                        Log.e("PokemonListViewModel", "Error al cargar pokémon: ${result.message}")
                    }
                }
            }
        }
    }

    /** Carga las imágenes para la lista de Pokémon */
    private fun loadPokemonImages(pokemons: List<PokemonDomain>) {
        viewModelScope.launch {
            val pokemonWithImages = mutableListOf<PokemonDomain>()
            var loadedCount = 0

            if (pokemons.isEmpty()) {
                isLoading = false
                _isLoadingMore.value = false
                return@launch
            }

            for (pokemon in pokemons) {
                viewModelScope.launch {
                    try {
                        // Obtener detalles del Pokémon para obtener la URL de la imagen
                        repository.getPokemonDetailsById(pokemon.id).collect { detailsResource ->
                            when (detailsResource) {
                                is Resource.Success -> {
                                    // Usar el sprite front_default de manera segura con Elvis operator
                                    val imageUrl = detailsResource.data.sprites?.front_default ?: ""

                                    // Extraer los tipos del pokémon
                                    val types = detailsResource.data.types?.mapNotNull {
                                        it?.type?.name
                                    } ?: emptyList()

                                    // Guardar los tipos en el mapa
                                    pokemonTypes[pokemon.id] = types

                                    // Crear un nuevo objeto PokemonDomain con la URL de la imagen
                                    val pokemonWithImage = pokemon.copy(imageUrl = imageUrl)

                                    synchronized(pokemonWithImages) {
                                        pokemonWithImages.add(pokemonWithImage)
                                        loadedCount++

                                        // Cuando se han cargado todas las imágenes, actualizar el estado
                                        if (loadedCount == pokemons.size) {
                                            // Ordenar por ID para mantener el orden correcto
                                            val sortedList = pokemonWithImages.sortedBy { it.id }

                                            // Agregar los nuevos pokémon a la lista existente
                                            allPokemonList = (allPokemonList + sortedList).distinctBy { it.id }
                                            Log.d("PokemonListViewModel", "Pokémon totales cargados: ${allPokemonList.size}")

                                            // Aplicar los filtros actuales
                                            applyFilters()

                                            // Indicar que la carga ha terminado
                                            isLoading = false
                                            _isLoadingMore.value = false
                                        }
                                    }
                                }
                                is Resource.Error -> {
                                    synchronized(pokemonWithImages) {
                                        // Si hay un error, agregar el Pokémon con la imagen front_default
                                        val defaultImageUrl = detailsResource.data?.sprites?.front_default
                                        val pokemonWithDefaultImage = pokemon.copy(imageUrl = defaultImageUrl)
                                        pokemonWithImages.add(pokemonWithDefaultImage)
                                        loadedCount++

                                        if (loadedCount == pokemons.size) {
                                            val sortedList = pokemonWithImages.sortedBy { it.id }
                                            allPokemonList = (allPokemonList + sortedList).distinctBy { it.id }

                                            // Aplicar los filtros actuales
                                            applyFilters()

                                            // Indicar que la carga ha terminado
                                            isLoading = false
                                            _isLoadingMore.value = false
                                        }
                                    }
                                    Log.e("PokemonListViewModel", "Error al cargar detalles para ${pokemon.name}: ${detailsResource.message}")
                                }
                                is Resource.Loading -> {
                                    // Ignorar estado de carga
                                }
                            }
                        }
                    } catch (e: Exception) {
                        synchronized(pokemonWithImages) {
                            // Si hay una excepción, agregar el Pokémon con una imagen predeterminada
                            val defaultImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
                            val pokemonWithDefaultImage = pokemon.copy(imageUrl = defaultImageUrl)
                            pokemonWithImages.add(pokemonWithDefaultImage)
                            loadedCount++

                            if (loadedCount == pokemons.size) {
                                val sortedList = pokemonWithImages.sortedBy { it.id }
                                allPokemonList = (allPokemonList + sortedList).distinctBy { it.id }

                                // Aplicar los filtros actuales
                                applyFilters()

                                // Indicar que la carga ha terminado
                                isLoading = false
                                _isLoadingMore.value = false
                            }
                        }
                        Log.e("PokemonListViewModel", "Error al cargar detalles para ${pokemon.name}: ${e.message}")
                    }
                }
            }
        }
    }

    /** Marca o desmarca un Pokémon como favorito */
    fun toggleFavorite(pokemon: PokemonDomain) {
        viewModelScope.launch {
            val newFavorite = !pokemon.favorite
            repository.updatePokemonFavorite(pokemon.id, newFavorite)
            // Actualizar la lista en memoria
            allPokemonList = allPokemonList.map {
                if (it.id == pokemon.id) it.copy(favorite = newFavorite) else it
            }
            applyFilters()
        }
    }

    /** Exponer el mapa de tipos de Pokémon */
    fun getPokemonTypesMap(): Map<Int, List<String>> = pokemonTypes.toMap()

    /** Factory para crear instancias del ViewModel con el repositorio necesario */
    class Factory(private val database: PokedexDatabase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) {
                val pokemonDao = database.pokemonDao()
                val pokemonDetailsDao = database.pokemonDetailsDao()
                val remoteDataSource = PokemonRemoteDataSource()
                val pokemonSpeciesDao = database.pokemonSpeciesDao()
                val abilityDao = database.abilityDao()
                val repository = PokemonRepository(
                    pokemonDao,
                    pokemonDetailsDao,
                    pokemonSpeciesDao,
                    abilityDao,
                    remoteDataSource
                )
                return PokemonListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}