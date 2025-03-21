package com.example.completepokemondex.domain.usecases

import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.domain.models.Pokemon
import kotlinx.coroutines.flow.Flow

class GetPokemonListUseCase(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(page: Int = 0, offset: Int = 16): Flow<List<Pokemon>> {

        return pokemonRepository.getPokemonList(page, offset)
    }
}

