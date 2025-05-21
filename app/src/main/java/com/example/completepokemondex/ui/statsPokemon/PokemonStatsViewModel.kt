package com.example.completepokemondex.ui.statsPokemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.data.domain.model.TypeDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa el estado de la UI para la pantalla de estadísticas de Pokémon.
 *
 * @property isLoading Indica si los datos están cargando.
 * @property error Mensaje de error si ocurre alguno.
 * @property pokemon Detalles del Pokémon a mostrar.
 * @property resistencias Tipos contra los que el Pokémon tiene resistencia.
 * @property inmunidades Tipos contra los que el Pokémon es inmune.
 * @property efectividades Tipos contra los que el Pokémon es efectivo.
 * @property resistenciasRaw Tipos contra los que el Pokémon tiene resistencia (en inglés).
 * @property inmunidadesRaw Tipos contra los que el Pokémon es inmune (en inglés).
 * @property efectividadesRaw Tipos contra los que el Pokémon es efectivo (en inglés).
 */
data class StatsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val pokemon: PokemonDetailsDomain? = null,
    val resistencias: List<String> = emptyList(),
    val inmunidades: List<String> = emptyList(),
    val efectividades: List<String> = emptyList(),
    val resistenciasRaw: List<String> = emptyList(),
    val inmunidadesRaw: List<String> = emptyList(),
    val efectividadesRaw: List<String> = emptyList()
)

/**
 * ViewModel para la pantalla de estadísticas de Pokémon.
 * Gestiona la obtención de datos y el estado de la UI.
 *
 * @property repository Repositorio de Pokémon para acceder a los datos.
 */
@HiltViewModel
class PokemonStatsViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData(StatsUiState())
    val uiState: LiveData<StatsUiState> = _uiState

    // LiveData para exponer los tipos del Pokémon (en inglés)
    private val _pokemonTypes = MutableLiveData<List<String>>()
    val pokemonTypes: LiveData<List<String>> = _pokemonTypes

    // Mapper para internacionalizar nombres de tipos
    private var typeNameMapper: ((String) -> String)? = null

    fun setTypeNameMapper(mapper: (String) -> String) {
        typeNameMapper = mapper
    }
    
    /**
     * Traduce un nombre de tipo utilizando el mapper definido
     * @param typeName Nombre del tipo en inglés
     * @return Nombre traducido del tipo
     */
    fun translateTypeName(typeName: String): String {
        return typeNameMapper?.invoke(typeName) ?: typeName.replaceFirstChar { it.uppercase() }
    }
    
    /**
     * Establece el ID del Pokémon y obtiene sus detalles.
     * Actualiza el estado de la UI según el resultado.
     *
     * @param pokemonId ID del Pokémon a consultar.
     */
    fun setPokemonId(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)

            repository.getPokemonDetailsById(pokemonId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val pokemon = result.data
                        if (pokemon != null) {
                            val typeNames = pokemon.types?.mapNotNull { it?.type?.name } ?: emptyList()
                            // Actualiza el LiveData de tipos
                            _pokemonTypes.value = typeNames
                            val typeDomains = typeNames.map { typeName ->
                                async {
                                    // Usar getTypeByName en vez de getTypeById
                                    repository.getTypeByName(typeName)
                                }
                            }.awaitAll()

                            val allDoubleDamageFrom = mutableSetOf<String>()
                            val allHalfDamageFrom = mutableSetOf<String>()
                            val allNoDamageFrom = mutableSetOf<String>()

                            typeDomains.forEach { flow ->
                                flow.collect { typeResult ->
                                    if (typeResult is Resource.Success && typeResult.data != null) {
                                        val rel = typeResult.data.damageRelations
                                        allDoubleDamageFrom += rel.doubleDamageFrom.map { it.name }
                                        allHalfDamageFrom += rel.halfDamageFrom.map { it.name }
                                        allNoDamageFrom += rel.noDamageFrom.map { it.name }
                                    }
                                }
                            }

                            val inmunidades = allNoDamageFrom
                            val resistencias = allHalfDamageFrom - inmunidades - allDoubleDamageFrom
                            val efectividades = allDoubleDamageFrom - inmunidades - allHalfDamageFrom

                            // Internacionalizar los nombres de tipos
                            val mapper = typeNameMapper ?: { it.replaceFirstChar { c -> c.uppercase() } }
                            val resistenciasIntl = resistencias.map(mapper).sorted()
                            val inmunidadesIntl = inmunidades.map(mapper).sorted()
                            val efectividadesIntl = efectividades.map(mapper).sorted()

                            // Mostrar por consola los resultados
                            println("Resistencias: ${resistenciasIntl.joinToString(", ").ifEmpty { "-" }}")
                            println("Inmunidades: ${inmunidadesIntl.joinToString(", ").ifEmpty { "-" }}")
                            println("Efectividades: ${efectividadesIntl.joinToString(", ").ifEmpty { "-" }}")

                            _uiState.value = StatsUiState(
                                isLoading = false,
                                pokemon = pokemon,
                                resistencias = resistenciasIntl,
                                inmunidades = inmunidadesIntl,
                                efectividades = efectividadesIntl,
                                resistenciasRaw = resistencias.sorted(),
                                inmunidadesRaw = inmunidades.sorted(),
                                efectividadesRaw = efectividades.sorted()
                            )
                        } else {
                            _uiState.value = StatsUiState(isLoading = false, error = "No se encontraron datos del Pokémon")
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            error = result.message ?: "Error al cargar estadísticas"
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
