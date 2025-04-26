package com.example.completepokemondex.ui.pokemonMoves

import androidx.lifecycle.*
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonMoveDomain
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.Locale
import javax.inject.Inject

data class MoveUi(
    val name: String,
    val power: Int? = null,
    val type: String? = null,
    val moveId: Int? = null,
    val accuracy: Int? = null
)

data class MovesSectionUi(
    val title: String,
    val moves: List<MoveUi>
)

data class PokemonMovesUiState(
    val isLoading: Boolean = false,
    val isLoadingMoveDetails: Boolean = false,
    val sections: List<MovesSectionUi> = emptyList(),
    val pokemonTypes: List<String>? = null
)

@HiltViewModel
class PokemonMovesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonId = MutableLiveData<Int>()
    private val _uiState = MutableLiveData(PokemonMovesUiState())
    val uiState: LiveData<PokemonMovesUiState> = _uiState

    private val loadedMoveDetails = mutableMapOf<Int, PokemonMoveDomain>()
    private var totalMovesToLoad = 0
    private var loadedMovesCount = 0
    
    // Mapa para relacionar nombres de movimientos con sus IDs
    private val moveNameToIdMap = mutableMapOf<String, Int>()

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
                        
                        // Extraer los tipos de Pokémon para el gradiente
                        val pokemonTypes = result.data.types?.mapNotNull { it?.type?.name } ?: emptyList()
                        
                        // Llenar el mapa de nombres a ids
                        moves.forEach { move ->
                            move?.move?.url?.let { url ->
                                val moveId = extractMoveIdFromUrl(url)
                                val moveName = move.move?.name?.replaceFirstChar { it.uppercase() }?.replace("-", " ")
                                if (moveId != null && moveName != null) {
                                    moveNameToIdMap[moveName] = moveId
                                }
                            }
                        }
                        
                        val sections = groupMovesByMethod(moves)
                        
                        // Mostrar las secciones pero mantener el estado de carga
                        _uiState.value = PokemonMovesUiState(
                            isLoading = true,
                            isLoadingMoveDetails = true,
                            sections = sections,
                            pokemonTypes = pokemonTypes
                        )
                        
                        // Cargar detalles de cada movimiento
                        totalMovesToLoad = moves.size
                        loadedMovesCount = 0
                        
                        // Si no hay movimientos, terminar la carga
                        if (moves.isEmpty()) {
                            _uiState.value = PokemonMovesUiState(
                                isLoading = false,
                                isLoadingMoveDetails = false,
                                sections = sections,
                                pokemonTypes = pokemonTypes
                            )
                            return@collect
                        }
                        
                        loadMovesDetails(moves, pokemonTypes)
                    }
                    is Resource.Error -> {
                        _uiState.value = PokemonMovesUiState(
                            isLoading = false,
                            isLoadingMoveDetails = false
                        )
                    }
                    is Resource.Loading -> {
                        _uiState.value = PokemonMovesUiState(isLoading = true)
                    }
                }
            }
        }
    }

    /**
     * Obtiene los detalles de un movimiento por su ID
     */
    fun getMoveDetails(moveId: Int?): PokemonMoveDomain? {
        return moveId?.let { loadedMoveDetails[it] }
    }

    private fun loadMovesDetails(moves: List<PokemonDetailsDomain.Move?>, pokemonTypes: List<String>) {
        viewModelScope.launch {
            val moveIds = moves.mapNotNull { move ->
                move?.move?.url?.let { url ->
                    extractMoveIdFromUrl(url)
                }
            }

            val currentLocale = Locale.getDefault().language

            // Lanzar todas las peticiones en paralelo
            coroutineScope {
                moveIds.map { moveId ->
                    async {
                        if (moveId != null) {
                            repository.getMoveById(moveId).collect { result ->
                                if (result is Resource.Success) {
                                    loadedMoveDetails[moveId] = result.data
                                }
                            }
                        }
                    }
                }.awaitAll()
            }

            // Cuando todas terminan, actualiza el estado
            val updatedSections = getEnhancedMoveSections(currentLocale)
            _uiState.value = PokemonMovesUiState(
                isLoading = false,
                isLoadingMoveDetails = false,
                sections = updatedSections,
                pokemonTypes = pokemonTypes
            )
        }
    }
    
    private fun getEnhancedMoveSections(locale: String): List<MovesSectionUi> {
        val currentState = _uiState.value ?: return emptyList()
        val sections = currentState.sections
        
        return sections.map { section ->
            val enhancedMoves = section.moves.map { moveUi ->
                val moveId = moveUi.moveId ?: moveNameToIdMap[moveUi.name]
                
                if (moveId != null) {
                    val moveDetails = loadedMoveDetails[moveId]
                    
                    // Intentamos obtener el nombre localizado
                    val localizedName = moveDetails?.names?.find { nameEntry -> 
                        nameEntry?.language?.name == locale 
                    }?.name ?: moveUi.name
                    
                    MoveUi(
                        name = localizedName,
                        power = moveDetails?.power,
                        type = moveDetails?.type?.name,
                        moveId = moveId,
                        accuracy = moveDetails?.accuracy
                    )
                } else {
                    // Si no encontramos detalles, usamos el movimiento original
                    moveUi
                }
            }
            
            MovesSectionUi(section.title, enhancedMoves.sortedBy { it.name })
        }
    }
    
    private fun extractMoveIdFromUrl(url: String): Int? {
        // URL ejemplo: https://pokeapi.co/api/v2/move/5/
        return url.trim('/').split('/').lastOrNull()?.toIntOrNull()
    }

    private fun groupMovesByMethod(moves: List<PokemonDetailsDomain.Move?>): List<MovesSectionUi> {
        // Agrupa los movimientos por método de aprendizaje
        val sectionsMap = linkedMapOf<String, MutableList<MoveUi>>()
        for (move in moves) {
            val moveName = move?.move?.name?.replaceFirstChar { it.uppercase() }?.replace("-", " ") ?: continue
            val moveId = move.move?.url?.let { extractMoveIdFromUrl(it) }
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
            sectionsMap[sectionTitle]?.add(MoveUi(name = moveName, moveId = moveId))
        }
        return sectionsMap.map { MovesSectionUi(it.key, it.value.sortedBy { move -> move.name }) }
    }
}
