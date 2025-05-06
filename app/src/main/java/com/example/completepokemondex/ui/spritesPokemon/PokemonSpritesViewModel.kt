package com.example.completepokemondex.ui.spritesPokemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.ui.infoPokemon.PokemonTypeUi
import com.example.completepokemondex.util.PokemonTypeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado de la UI para la pantalla de sprites de Pokémon.
 * @property isLoading Indica si se están cargando los datos.
 * @property error Mensaje de error si ocurre alguno.
 * @property pokemon Detalles del Pokémon.
 * @property sprites Lista de pares (etiqueta, URL) de sprites.
 * @property types Lista de tipos del Pokémon para la UI.
 */
data class SpritesUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val pokemon: PokemonDetailsDomain? = null,
    val sprites: List<Pair<String, String>> = emptyList(),
    val types: List<PokemonTypeUi> = emptyList()
)

/**
 * ViewModel encargado de gestionar la lógica y el estado de la pantalla de sprites de un Pokémon.
 * Utiliza Hilt para la inyección de dependencias.
 */
@HiltViewModel
class PokemonSpritesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    private val _uiState = MutableLiveData(SpritesUiState())
    val uiState: LiveData<SpritesUiState> = _uiState
    
    /**
     * Carga los sprites y tipos del Pokémon dado su ID.
     * @param pokemonId ID del Pokémon a cargar.
     */
    fun loadPokemonSprites(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            repository.getPokemonDetailsById(pokemonId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val pokemon = result.data
                        val spritesList = getAllSprites(pokemon.sprites)
                        val typesList = pokemon.types?.mapNotNull { typeInfo ->
                            typeInfo?.type?.name?.let { typeName ->
                                val type = PokemonTypeUtil.getTypeByName(typeName)
                                PokemonTypeUi(
                                    name = type.name,
                                    color = type.colorRes,
                                    stringRes = type.stringRes
                                )
                            }
                        } ?: emptyList()
                        
                        _uiState.value = SpritesUiState(
                            isLoading = false,
                            pokemon = pokemon,
                            sprites = spritesList,
                            types = typesList
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            error = result.message ?: "Error al cargar sprites"
                        )
                    }
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value?.copy(isLoading = true)
                    }
                }
            }
        }
    }
    
    /**
     * Extrae todos los sprites disponibles de la estructura de sprites del dominio.
     * @param sprites Objeto de sprites del dominio.
     * @return Lista de pares (etiqueta, URL) de sprites.
     */
    fun getAllSprites(sprites: PokemonDetailsDomain.Sprites?): List<Pair<String, String>> {
        if (sprites == null) return emptyList()
        
        val result = mutableListOf<Pair<String, String>>()
        // Sprites principales
        fun add(label: String, value: Any?) {
            val url = value as? String
            if (!url.isNullOrBlank()) result.add(label to url)
        }
        add("front_default", sprites.front_default)
        add("front_shiny", sprites.front_shiny)
        add("front_female", sprites.front_female)
        add("front_shiny_female", sprites.front_shiny_female)
        add("back_default", sprites.back_default)
        add("back_shiny", sprites.back_shiny)
        add("back_female", sprites.back_female)
        add("back_shiny_female", sprites.back_shiny_female)

        // Sprites "other"
        sprites.other?.let { other ->
            add("dream_world/front_default", other.dream_world?.front_default)
            add("home/front_default", other.home?.front_default)
            add("home/front_shiny", other.home?.front_shiny)
            add("official-artwork/front_default", other.`official-artwork`?.front_default)
            add("official-artwork/front_shiny", other.`official-artwork`?.front_shiny)
            add("showdown/back_default", other.showdown?.back_default)
            add("showdown/back_shiny", other.showdown?.back_shiny)
            add("showdown/front_default", other.showdown?.front_default)
            add("showdown/front_shiny", other.showdown?.front_shiny)
        }

        // Sprites "versions"
        sprites.versions?.let { v ->
            // Generation I
            v.`generation-i`?.let { gen ->
                gen.`red-blue`?.let {
                    add("gen1/red-blue/back_default", it.back_default)
                    add("gen1/red-blue/back_gray", it.back_gray)
                    add("gen1/red-blue/back_transparent", it.back_transparent)
                    add("gen1/red-blue/front_default", it.front_default)
                    add("gen1/red-blue/front_gray", it.front_gray)
                    add("gen1/red-blue/front_transparent", it.front_transparent)
                }
                gen.`yellow`?.let {
                    add("gen1/yellow/back_default", it.back_default)
                    add("gen1/yellow/back_gray", it.back_gray)
                    add("gen1/yellow/back_transparent", it.back_transparent)
                    add("gen1/yellow/front_default", it.front_default)
                    add("gen1/yellow/front_gray", it.front_gray)
                    add("gen1/yellow/front_transparent", it.front_transparent)
                }
            }
            // ...existing code...
        }
        return result
    }
}
