package com.example.completepokemondex.ui.pokemonMoves

import androidx.lifecycle.*
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovesSectionUi(
    val title: String,
    val moves: List<String>
)

data class PokemonMovesUiState(
    val isLoading: Boolean = false,
    val sections: List<MovesSectionUi> = emptyList()
)

@HiltViewModel
class PokemonMovesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonId = MutableLiveData<Int>()
    private val _uiState = MutableLiveData(PokemonMovesUiState())
    val uiState: LiveData<PokemonMovesUiState> = _uiState

    fun setPokemonId(id: Int) {
        if (_pokemonId.value == id) return
        _pokemonId.value = id
        fetchMoves(id)
    }

    private fun fetchMoves(id: Int) {
        viewModelScope.launch {
            _uiState.value = PokemonMovesUiState(isLoading = true)
            repository.getPokemonDetailsById(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val moves = result.data.moves ?: emptyList()
                        val sections = groupMovesByMethod(moves)
                        _uiState.value = PokemonMovesUiState(
                            isLoading = false,
                            sections = sections
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = PokemonMovesUiState(isLoading = false)
                    }
                    is Resource.Loading -> {
                        _uiState.value = PokemonMovesUiState(isLoading = true)
                    }
                }
            }
        }
    }

    private fun groupMovesByMethod(moves: List<PokemonDetailsDomain.Move?>): List<MovesSectionUi> {
        // Agrupa los movimientos por método de aprendizaje (solo nombres, sin lógica avanzada)
        val sectionsMap = linkedMapOf<String, MutableList<String>>()
        for (move in moves) {
            val moveName = move?.move?.name?.replaceFirstChar { it.uppercase() } ?: continue
            val method = move.version_group_details?.firstOrNull()?.move_learn_method?.name ?: "unknown"
            val sectionTitle = when (method) {
                "level-up" -> "Por nivel"
                "machine" -> "Por MT/MO"
                "tutor" -> "Por tutor"
                "egg" -> "Por crianza"
                else -> "Otros"
            }
            if (!sectionsMap.containsKey(sectionTitle)) {
                sectionsMap[sectionTitle] = mutableListOf()
            }
            sectionsMap[sectionTitle]?.add(moveName)
        }
        return sectionsMap.map { MovesSectionUi(it.key, it.value.sorted()) }
    }
}
