package com.example.completepokemondex.ui.statsPokemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val pokemon: PokemonDetailsDomain? = null
)

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData(StatsUiState())
    val uiState: LiveData<StatsUiState> = _uiState
    
    fun setPokemonId(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            repository.getPokemonDetailsById(pokemonId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.value = StatsUiState(
                            isLoading = false,
                            pokemon = result.data
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            error = result.message ?: "Error al cargar estadísticas"
                        )
                    }
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value?.copy(isLoading = true)
                    }
                }
            }
        }
    }
}
