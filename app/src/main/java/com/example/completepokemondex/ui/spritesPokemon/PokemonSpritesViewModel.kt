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
 * @property types Lista de tipos del Pokémon para la UI.
 */
data class SpritesUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val pokemon: PokemonDetailsDomain? = null,
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
     * Carga los tipos del Pokémon dado su ID.
     * @param pokemonId ID del Pokémon a cargar.
     */
    fun loadPokemonSprites(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            repository.getPokemonDetailsById(pokemonId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val pokemon = result.data
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
}
