package com.example.completepokemondex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.model.Pokemon
import com.example.completepokemondex.data.PokemonRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * ViewModel para gestionar la lógica de negocio relacionada con los Pokémon.
 * @property repository Repositorio de Pokémon para obtener datos.
 */
class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> = _pokemon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _error.value = "An error occurred: ${throwable.message}"
        _isLoading.value = false
    }

    /**
     * Obtiene la información de un Pokémon por su nombre.
     * @param name Nombre del Pokémon a buscar.
     */
    fun fetchPokemon(name: String) {
        _isLoading.value = true

        viewModelScope.launch(exceptionHandler) {
            try {
                val result = repository.getPokemon(name)
                result.onSuccess {
                    _pokemon.value = it
                }
                result.onFailure {
                    _error.value = it.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}