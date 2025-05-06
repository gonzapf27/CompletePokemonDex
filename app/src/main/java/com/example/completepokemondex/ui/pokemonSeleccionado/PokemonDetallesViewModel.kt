package com.example.completepokemondex.ui.pokemonSeleccionado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que gestiona la navegación y acciones principales en la pantalla de detalles del Pokémon.
 */
@HiltViewModel
class PokemonDetallesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    /**
     * Enum que representa los destinos de navegación posibles en la pantalla de detalles.
     */
    enum class NavDestination {
        INFO, STATS, SPRITES, LOCATIONS, MOVES
    }

    private val _navState = MutableLiveData<NavDestination>()
    val navState: LiveData<NavDestination> = _navState

    private var currentPokemonId: Int = 0

    /**
     * Establece el ID inicial del Pokémon y navega a la pestaña INFO por defecto.
     */
    fun setInitialPokemonId(pokemonId: Int) {
        currentPokemonId = pokemonId
        _navState.value = NavDestination.INFO // Por defecto mostramos la pestaña INFO
    }

    /**
     * Cambia el destino de navegación.
     */
    fun navigateTo(destination: NavDestination) {
        _navState.value = destination
    }

    /**
     * Marca o desmarca el Pokémon como favorito.
     */
    fun markAsFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updatePokemonFavorite(currentPokemonId, isFavorite)
        }
    }
}