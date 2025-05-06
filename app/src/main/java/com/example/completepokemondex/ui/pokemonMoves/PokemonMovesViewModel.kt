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
import android.util.Log

/**
 * ViewModel encargado de gestionar la lógica y el estado de la pantalla de movimientos de un Pokémon.
 */
@HiltViewModel
class PokemonMovesViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonId = MutableLiveData<Int>()

    private val _moves = MutableLiveData<List<PokemonMoveDomain>>(emptyList())
    val moves: LiveData<List<PokemonMoveDomain>> = _moves

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _pokemonTypes = MutableLiveData<List<String>>(emptyList())
    val pokemonTypes: LiveData<List<String>> = _pokemonTypes

    private var currentOffset = 0
    private val pageSize = 20
    private var isLastPage = false
    private var isLoadingMore = false

    /**
     * Representa un movimiento junto con el método de aprendizaje y el nivel.
     */
    data class MoveWithLearnMethod(
        val moveId: Int,
        val learnMethod: String?,
        val level: Int?
    )

    private var moveLearnMethods: List<MoveWithLearnMethod> = emptyList()

    /**
     * Representa un ítem de sección para agrupar los movimientos por método de aprendizaje.
     */
    sealed class MoveSectionItem {
        data class Header(val title: String) : MoveSectionItem()
        data class MoveItem(val move: PokemonMoveDomain, val learnMethod: String?, val level: Int?) : MoveSectionItem()
    }

    private val _sectionedMoves = MutableLiveData<List<MoveSectionItem>>(emptyList())
    val sectionedMoves: LiveData<List<MoveSectionItem>> = _sectionedMoves

    /**
     * Establece el ID del Pokémon y carga los movimientos.
     */
    fun setPokemonId(pokemonId: Int) {
        if (_pokemonId.value != pokemonId) {
            _pokemonId.value = pokemonId
            resetMoves()
            // Obtener tipos del Pokémon
            viewModelScope.launch {
                repository.getPokemonDetailsById(pokemonId).collect { resource ->
                    if (resource is Resource.Success) {
                        val types = resource.data.types?.mapNotNull { it?.type?.name } ?: emptyList()
                        _pokemonTypes.value = types
                    }
                }
            }
            loadMoreMoves()
        }
    }

    /**
     * Reinicia la lista de movimientos y el estado de paginación.
     */
    private fun resetMoves() {
        _moves.value = emptyList()
        currentOffset = 0
        isLastPage = false
        isLoadingMore = false
        _error.value = null
    }

    /**
     * Carga más movimientos paginados para el Pokémon actual.
     */
    fun loadMoreMoves() {
        if (isLoadingMore || isLastPage || _pokemonId.value == null) return
        isLoadingMore = true
        _isLoading.value = true
        val pokemonId = _pokemonId.value ?: return
        viewModelScope.launch {
            repository.getPokemonDetailsById(pokemonId).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val allMoves = resource.data.moves?.mapNotNull { move ->
                            val url = move?.move?.url
                            val moveId = url?.trimEnd('/')?.split("/")?.lastOrNull()?.toIntOrNull()
                            val method = move?.version_group_details?.firstOrNull()?.move_learn_method?.name
                            val level = move?.version_group_details?.firstOrNull()?.level_learned_at
                            if (moveId != null) MoveWithLearnMethod(moveId, method, level) else null
                        } ?: emptyList()
                        moveLearnMethods = allMoves
                        val pagedMoves = allMoves.drop(currentOffset).take(pageSize)
                        if (pagedMoves.isEmpty()) {
                            isLastPage = true
                            _isLoading.value = false
                            isLoadingMore = false
                            return@collect
                        }
                        val moveIds = pagedMoves.map { it.moveId }
                        val movesDetails = moveIds.mapIndexed { idx, moveId ->
                            async {
                                var move: PokemonMoveDomain? = null
                                repository.getMoveById(moveId).collect { moveResource ->
                                    if (moveResource is Resource.Success) {
                                        move = moveResource.data
                                    }
                                }
                                move
                            }
                        }.awaitAll().filterNotNull()
                        val currentList = _moves.value ?: emptyList()
                        val newMoves = currentList + movesDetails
                        _moves.value = newMoves
                        currentOffset += pagedMoves.size
                        isLastPage = pagedMoves.size < pageSize
                        _isLoading.value = false
                        isLoadingMore = false
                        // Agrupar y crear secciones
                        val grouped = newMoves.mapNotNull { move ->
                            val (method, level) = getLearnMethodForMove(move.id ?: -1)
                            MoveSectionItem.MoveItem(move, method, level)
                        }.groupBy { it.learnMethodKey() }
                        val sectionList = mutableListOf<MoveSectionItem>()
                        val order = listOf("level-up", "machine", "tutor", "egg")
                        val methodTitles = mapOf(
                            "level-up" to "Por Nivel",
                            "machine" to "Por MT/MO",
                            "tutor" to "Por Tutor",
                            "egg" to "Por Huevo"
                        )
                        for (key in order) {
                            val items = grouped[key]
                            if (!items.isNullOrEmpty()) {
                                sectionList.add(MoveSectionItem.Header(methodTitles[key] ?: key))
                                sectionList.addAll(
                                    if (key == "level-up") items.sortedBy { it.level ?: 0 } else items
                                )
                            }
                        }
                        // Otros métodos
                        val otherKeys = grouped.keys - order.toSet()
                        for (key in otherKeys) {
                            val items = grouped[key]
                            if (!items.isNullOrEmpty()) {
                                sectionList.add(MoveSectionItem.Header(methodTitles[key] ?: key.replaceFirstChar { it.uppercase() }))
                                sectionList.addAll(items)
                            }
                        }
                        _sectionedMoves.value = sectionList
                    }
                    is Resource.Error -> {
                        _error.value = resource.message
                        _isLoading.value = false
                        isLoadingMore = false
                    }
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    private fun MoveSectionItem.MoveItem.learnMethodKey(): String =
        when (learnMethod) {
            "level-up" -> "level-up"
            "machine" -> "machine"
            "tutor" -> "tutor"
            "egg" -> "egg"
            else -> learnMethod ?: "otro"
        }

    /**
     * Obtiene el método de aprendizaje y el nivel para un movimiento dado.
     */
    fun getLearnMethodForMove(moveId: Int): Pair<String?, Int?> {
        val found = moveLearnMethods.firstOrNull { it.moveId == moveId }
        return found?.let { it.learnMethod to it.level } ?: (null to null)
    }
}
