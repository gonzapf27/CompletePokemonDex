package com.example.completepokemondex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.domain.model.PokemonDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository) : ViewModel() {
    // Definir el estado de UI como sealed class
    sealed class UiState {
        data object Loading : UiState()
        data class Success(val pokemons: List<PokemonDomain>) : UiState()
        data class Error(val message: String, val pokemons: List<PokemonDomain>? = null) : UiState()
    }

    // Estado de UI como StateFlow para ser observado por el Fragment
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Límite fijo para la cantidad de Pokémon a cargar
    private val limit = 150 // Cargar 150 Pokémon de una vez

    init {
        loadPokemonList()
    }

    /**
     * Carga la lista de Pokémon
     */
    fun loadPokemonList() {
        _uiState.value = UiState.Loading
        fetchPokemonList(limit, 0)
    }

    /**
     * Obtiene la lista de Pokémon del repositorio
     */
    private fun fetchPokemonList(limit: Int, offset: Int) {
        viewModelScope.launch {
            repository.getPokemonList(limit, offset).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        _uiState.value = UiState.Success(result.data)
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.Error(result.message, result.data)
                    }
                }
            }
        }
    }

    /**
     * Factory para crear instancias del ViewModel con el repositorio necesario
     */
    class Factory(private val database: PokedexDatabase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) {
                val pokemonDao = database.pokemonDao()
                val remoteDataSource = PokemonRemoteDataSource()
                val repository = PokemonRepository(pokemonDao, remoteDataSource)
                return PokemonListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}