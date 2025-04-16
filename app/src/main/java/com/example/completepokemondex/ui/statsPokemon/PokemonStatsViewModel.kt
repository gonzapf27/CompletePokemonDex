package com.example.completepokemondex.ui.statsPokemon

import androidx.lifecycle.*
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import kotlinx.coroutines.launch

data class PokemonStatsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val pokemon: PokemonDetailsDomain? = null
)

class PokemonStatsViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonId = MutableLiveData<Int>()
    private val _uiState = MutableLiveData(PokemonStatsUiState())
    val uiState: LiveData<PokemonStatsUiState> = _uiState

    fun setPokemonId(id: Int) {
        if (_pokemonId.value == id) return
        _pokemonId.value = id
        fetchStats(id)
    }

    private fun fetchStats(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            repository.getPokemonDetailsById(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.value = PokemonStatsUiState(
                            isLoading = false,
                            pokemon = result.data
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            error = result.message ?: "Error al cargar stats"
                        )
                    }
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value?.copy(isLoading = true)
                    }
                }
            }
        }
    }

    class Factory(private val database: PokedexDatabase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PokemonStatsViewModel::class.java)) {
                val pokemonDao = database.pokemonDao()
                val remoteDataSource = PokemonRemoteDataSource()
                val pokemonDetailsDao = database.pokemonDetailsDao()
                val pokemonSpeciesDao = database.pokemonSpeciesDao()
                val abilityDao = database.abilityDao()
                val repository = PokemonRepository(pokemonDao, pokemonDetailsDao, pokemonSpeciesDao, abilityDao, remoteDataSource)
                return PokemonStatsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
