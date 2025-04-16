package com.example.completepokemondex.ui.pokemonSeleccionado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetallesMainViewModel @Inject constructor() : ViewModel() {

    enum class NavDestination { INFO, STATS }

    private val _navState = MutableLiveData<NavDestination?>()
    val navState: LiveData<NavDestination?> = _navState

    private var pokemonId: Int = 0

    fun setInitialPokemonId(id: Int) {
        pokemonId = id
        _navState.value = NavDestination.INFO
    }

    fun navigateTo(destination: NavDestination) {
        _navState.value = destination
    }
}
