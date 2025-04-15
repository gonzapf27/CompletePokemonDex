package com.example.completepokemondex.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.data.domain.model.PokemonDomain
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

    // Límite fijo para la cantidad de Pokémon a cargar
    private val limit = 150 // Cargar 150 Pokémon de una vez

    init {
        loadPokemonList()
    }

    /** Carga la lista de Pokémon */
    fun loadPokemonList() {
        _uiState.value = UiState.Loading
        fetchPokemonList(limit, 0)
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
        
        _uiState.value = UiState.Success(filteredList)
    }

    // Método antiguo de filtrado solo por nombre (obsoleto)
    private fun filterPokemon(query: String) {
        updateSearchQuery(query)
    }

    /** Obtiene la lista de Pokémon del repositorio */
    private fun fetchPokemonList(limit: Int, offset: Int) {
        viewModelScope.launch {
            repository.getPokemonList(limit, offset).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = UiState.Loading
                    }

                    is Resource.Success -> {
                        val pokemonList = result.data
                        loadPokemonImages(pokemonList)
                    }

                    is Resource.Error -> {
                        _uiState.value = UiState.Error(result.message, result.data)
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
                                            allPokemonList = sortedList
                                            
                                            // Aplicar los filtros actuales
                                            applyFilters()
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
                                            allPokemonList = sortedList
                                            
                                            // Aplicar los filtros actuales
                                            applyFilters()
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
                                allPokemonList = sortedList
                                
                                // Aplicar los filtros actuales
                                applyFilters()
                            }
                        }
                        Log.e("PokemonListViewModel", "Error al cargar detalles para ${pokemon.name}: ${e.message}")
                    }
                }
            }
        }
    }

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
                val repository = PokemonRepository(pokemonDao, pokemonDetailsDao,  pokemonSpeciesDao, abilityDao, remoteDataSource)
                return PokemonListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
