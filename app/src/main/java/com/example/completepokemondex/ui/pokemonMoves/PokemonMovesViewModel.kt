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

    private var currentOffset = 0
    private val pageSize = 20
    private var isLastPage = false
    private var isLoadingMore = false

    data class MoveWithLearnMethod(
        val moveId: Int,
        val learnMethod: String?,
        val level: Int?
    )

    private var moveLearnMethods: List<MoveWithLearnMethod> = emptyList()

    fun setPokemonId(pokemonId: Int) {
        if (_pokemonId.value != pokemonId) {
            _pokemonId.value = pokemonId
            resetMoves()
            loadMoreMoves()
        }
    }

    private fun resetMoves() {
        _moves.value = emptyList()
        currentOffset = 0
        isLastPage = false
        isLoadingMore = false
        _error.value = null
    }

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
                        _moves.value = currentList + movesDetails
                        currentOffset += pagedMoves.size
                        isLastPage = pagedMoves.size < pageSize
                        _isLoading.value = false
                        isLoadingMore = false
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

    fun getLearnMethodForMove(moveId: Int): Pair<String?, Int?> {
        val found = moveLearnMethods.firstOrNull { it.moveId == moveId }
        return found?.let { it.learnMethod to it.level } ?: (null to null)
    }
}
