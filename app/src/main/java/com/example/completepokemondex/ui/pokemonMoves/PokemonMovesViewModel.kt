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
import java.util.Locale
import javax.inject.Inject

data class MovesSectionUi(
    val title: String,
    val moves: List<String>
)

data class PokemonMovesUiState(
    val isLoading: Boolean = false,
    val isLoadingMoveDetails: Boolean = false,
    val sections: List<MovesSectionUi> = emptyList()
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
                        
                        // Mostrar las secciones pero mantener el estado de carga
                        _uiState.value = PokemonMovesUiState(
                            isLoading = true,
                            isLoadingMoveDetails = true,
                            sections = sections
                        )
                        
                        // Cargar detalles de cada movimiento
                        totalMovesToLoad = moves.size
                        loadedMovesCount = 0
                        
                        // Si no hay movimientos, terminar la carga
                        if (moves.isEmpty()) {
                            _uiState.value = PokemonMovesUiState(
                                isLoading = false,
                                isLoadingMoveDetails = false,
                                sections = sections
                            )
                            return@collect
                        }
                        
                        loadMovesDetails(moves)
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

    private fun loadMovesDetails(moves: List<PokemonDetailsDomain.Move?>) {
        viewModelScope.launch {
            val moveIds = moves.mapNotNull { move ->
                move?.move?.url?.let { url ->
                    extractMoveIdFromUrl(url)
                }
            }
            
            // Contador para seguir el progreso de carga
            var completedMoves = 0
            
            // Obtener el locale actual de la aplicación
            val currentLocale = Locale.getDefault().language
            
            // Cargar todos los movimientos y esperar a que terminen
            moveIds.forEach { moveId ->
                if (moveId != null) {
                    repository.getMoveById(moveId).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                loadedMoveDetails[moveId] = result.data
                                completedMoves++
                                
                                // Actualizar el contador de movimientos cargados
                                loadedMovesCount = completedMoves
                                
                                // Verificar si todos los movimientos se han cargado
                                if (completedMoves >= moveIds.size) {
                                    val updatedSections = getLocalizedMoveSections(currentLocale)
                                    _uiState.value = PokemonMovesUiState(
                                        isLoading = false,
                                        isLoadingMoveDetails = false,
                                        sections = updatedSections
                                    )
                                }
                            }
                            is Resource.Error -> {
                                completedMoves++
                                
                                // Actualizar el contador incluso si hay error
                                loadedMovesCount = completedMoves
                                
                                // Verificar si todos los movimientos se han cargado
                                if (completedMoves >= moveIds.size) {
                                    val updatedSections = getLocalizedMoveSections(currentLocale)
                                    _uiState.value = PokemonMovesUiState(
                                        isLoading = false,
                                        isLoadingMoveDetails = false,
                                        sections = updatedSections
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                // No hacer nada durante la carga individual
                            }
                        }
                    }
                }
            }
        }
    }
    
    private fun getLocalizedMoveSections(locale: String): List<MovesSectionUi> {
        val currentState = _uiState.value ?: return emptyList()
        val sections = currentState.sections
        
        // Para cada sección, actualizar los nombres de los movimientos con los localizados
        return sections.map { section ->
            val updatedMoves = section.moves.map { moveName ->
                // Encontrar el ID del movimiento basado en el nombre por defecto
                val moveId = findMoveIdByDefaultName(moveName)
                
                if (moveId != null) {
                    // Obtener los detalles del movimiento si están disponibles
                    val moveDetails = loadedMoveDetails[moveId]
                    
                    // Buscar el nombre localizado en base al idioma actual
                    val localizedName = moveDetails?.names?.find { nameEntry -> 
                        nameEntry?.language?.name == locale 
                    }?.name
                    
                    // Si encontramos un nombre localizado, usarlo; de lo contrario, usar el nombre por defecto
                    localizedName ?: moveName
                } else {
                    // Si no podemos encontrar el ID del movimiento, usar el nombre por defecto
                    moveName
                }
            }
            
            MovesSectionUi(section.title, updatedMoves.sorted())
        }
    }
    
    private fun findMoveIdByDefaultName(defaultName: String): Int? {
        // Convertir el defaultName a formato de API (por ejemplo, "Mega Punch" a "mega-punch")
        val apiStyleName = defaultName.lowercase().replace(" ", "-")
        
        // Buscar el ID correspondiente
        for ((id, moveDetails) in loadedMoveDetails) {
            if (moveDetails.name == apiStyleName) {
                return id
            }
        }
        return null
    }
    
    private fun extractMoveIdFromUrl(url: String): Int? {
        // URL ejemplo: https://pokeapi.co/api/v2/move/5/
        return url.trim('/').split('/').lastOrNull()?.toIntOrNull()
    }

    private fun groupMovesByMethod(moves: List<PokemonDetailsDomain.Move?>): List<MovesSectionUi> {
        // Agrupa los movimientos por método de aprendizaje (solo nombres, sin lógica avanzada)
        val sectionsMap = linkedMapOf<String, MutableList<String>>()
        for (move in moves) {
            val moveName = move?.move?.name?.replaceFirstChar { it.uppercase() }?.replace("-", " ") ?: continue
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

