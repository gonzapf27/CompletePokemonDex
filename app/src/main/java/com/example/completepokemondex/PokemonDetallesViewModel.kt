package com.example.completepokemondex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PokemonDetallesViewModel : ViewModel() {

    private val _idPokemon = MutableLiveData<Int>()
    val idPokemon: LiveData<Int> = _idPokemon

    private val _pokemonNombre = MutableLiveData<String>()
    val pokemonNombre: LiveData<String> = _pokemonNombre
    
    fun setPokemonId(id: Int){
        _idPokemon.value = id
    }

    fun setPokemonNombre(nombre: String){
        _pokemonNombre.value = nombre
    }

    fun showDetails(){
        // Ocultar barra de carga
        // Mostrar detalles del Pokémon

    }
}
