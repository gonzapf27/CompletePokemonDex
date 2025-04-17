package com.example.completepokemondex.data.domain.model

data class EvolutionChainDomain(
    val baby_trigger_item: Any?,
    val chain: Chain?,
    val id: Int?
) {
    data class Chain(
        val evolution_details: List<Any?>?,
        val evolves_to: List<EvolvesTo?>?,
        val is_baby: Boolean?,
        val species: Species?
    ) {
        data class EvolvesTo(
            val evolution_details: List<EvolutionDetail?>?,
            val evolves_to: List<EvolvesTo?>?,
            val is_baby: Boolean?,
            val species: Species?
        ) {
            data class EvolutionDetail(
                val gender: Any?,
                val held_item: Any?,
                val item: Any?,
                val known_move: Any?,
                val known_move_type: Any?,
                val location: Any?,
                val min_affection: Any?,
                val min_beauty: Any?,
                val min_happiness: Any?,
                val min_level: Int?,
                val needs_overworld_rain: Boolean?,
                val party_species: Any?,
                val party_type: Any?,
                val relative_physical_stats: Any?,
                val time_of_day: String?,
                val trade_species: Any?,
                val trigger: Trigger?,
                val turn_upside_down: Boolean?
            ) {
                data class Trigger(
                    val name: String?,
                    val url: String?
                )
            }

            data class EvolvesTo(
                val evolution_details: List<EvolutionDetail?>?,
                val evolves_to: List<Any?>?,
                val is_baby: Boolean?,
                val species: Species?
            ) {
                data class EvolutionDetail(
                    val gender: Any?,
                    val held_item: Any?,
                    val item: Any?,
                    val known_move: Any?,
                    val known_move_type: Any?,
                    val location: Any?,
                    val min_affection: Any?,
                    val min_beauty: Any?,
                    val min_happiness: Any?,
                    val min_level: Int?,
                    val needs_overworld_rain: Boolean?,
                    val party_species: Any?,
                    val party_type: Any?,
                    val relative_physical_stats: Any?,
                    val time_of_day: String?,
                    val trade_species: Any?,
                    val trigger: Trigger?,
                    val turn_upside_down: Boolean?
                ) {
                    data class Trigger(
                        val name: String?,
                        val url: String?
                    )
                }

                data class Species(
                    val name: String?,
                    val url: String?
                )
            }

            data class Species(
                val name: String?,
                val url: String?
            )
        }

        data class Species(
            val name: String?,
            val url: String?
        )
    }

    /**
     * Obtiene todos los detalles de Pokémon de la cadena evolutiva de manera iterativa.
     *
     * @param getPokemonDetailsByName función suspendida que recibe el nombre de la especie y devuelve el PokemonDetailsDomain correspondiente.
     * @return Lista de PokemonDetailsDomain en orden evolutivo.
     */
    suspend fun EvolutionChainDomain.getAllPokemonDetails(
        getPokemonDetailsByName: suspend (String) -> PokemonDetailsDomain?
    ): List<PokemonDetailsDomain> {
        val result = mutableListOf<PokemonDetailsDomain>()
        val queue = ArrayDeque<Any?>()
        if (this.chain != null) queue.add(this.chain)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            when (current) {
                is Chain -> {
                    current.species?.name?.let { name ->
                        getPokemonDetailsByName(name)?.let { result.add(it) }
                    }
                    current.evolves_to?.forEach { queue.add(it) }
                }
                is Chain.EvolvesTo -> {
                    current.species?.name?.let { name ->
                        getPokemonDetailsByName(name)?.let { result.add(it) }
                    }
                    current.evolves_to?.forEach { queue.add(it) }
                }
            }
        }
        return result
    }
}