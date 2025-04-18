package com.example.completepokemondex.ui.pokemonSeleccionado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetallesMainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    enum class NavDestination {
        INFO, STATS, SPRITES
    }

    private val _navState = MutableLiveData<NavDestination>()
    val navState: LiveData<NavDestination> = _navState

    private var currentPokemonId: Int = 0

    fun setInitialPokemonId(pokemonId: Int) {
        currentPokemonId = pokemonId
        _navState.value = NavDestination.INFO // Por defecto mostramos la pestaña INFO
    }

    fun navigateTo(destination: NavDestination) {
        _navState.value = destination
    }

    fun markAsFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updatePokemonFavorite(currentPokemonId, isFavorite)
        }
    }
}