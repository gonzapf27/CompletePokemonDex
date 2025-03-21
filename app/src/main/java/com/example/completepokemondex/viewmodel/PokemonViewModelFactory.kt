package com.example.completepokemondex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.completepokemondex.data.PokemonRepository

/**
 * Factory para crear instancias de `PokemonViewModel`.
 *
 * @param repository El repositorio de Pokémon que se usará en el ViewModel.
 */
class PokemonViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory {

    /**
     * Crea una nueva instancia del ViewModel dado el tipo de clase.
     *
     * @param modelClass La clase del ViewModel a crear.
     * @return Una nueva instancia del ViewModel.
     * @throws IllegalArgumentException Si la clase del ViewModel no es reconocida.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}