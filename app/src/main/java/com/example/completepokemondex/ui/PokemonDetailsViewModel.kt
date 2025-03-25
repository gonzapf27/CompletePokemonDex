package com.example.completepokemondex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.domain.model.PokemonDetailsDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class PokemonDetailsViewModel(private val repository: PokemonRepository) : ViewModel() {

    // Definir el estado de UI como sealed class
    sealed class UiState {
        data object Loading : UiState()
        data class Success(val pokemon: PokemonDetailsDomain) : UiState()
        data class Error(val message: String, val pokemon: PokemonDetailsDomain? = null) : UiState()
    }

    // Estado de UI como StateFlow para ser observado por el Fragment
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {

    }

    



}