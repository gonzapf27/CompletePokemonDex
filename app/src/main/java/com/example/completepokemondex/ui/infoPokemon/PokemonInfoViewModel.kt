package com.example.completepokemondex.ui.infoPokemon

import androidx.lifecycle.*
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.domain.model.EvolutionChainDomain
import com.example.completepokemondex.util.PokemonTypeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * Representa la información visual de un tipo de Pokémon para la UI.
 */
data class PokemonTypeUi(
    val name: String,
    val color: Int,
    val stringRes: Int
)

/**
 * Representa una habilidad de Pokémon para mostrar en la UI.
 * @param nombre Nombre de la habilidad.
 * @param descripcion Descripción localizada de la habilidad.
 * @param isOculta Indica si la habilidad es oculta.
 */
data class HabilidadUi(
    val nombre: String,
    val descripcion: String,
    val isOculta: Boolean? = false
)

/**
 * Estado de la UI para la pantalla de información de Pokémon.
 * Contiene todos los datos necesarios para mostrar la información detallada de un Pokémon.
 */
data class PokemonInfoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val id: String = "",
    val nombre: String = "",
    val height: String = "",
    val weight: String = "",
    val imageUrl: String? = null,
    val types: List<PokemonTypeUi> = emptyList(),
    val descripcion: String = "",
    val isFavorite: Boolean = false,
    val habilidades: List<HabilidadUi> = emptyList(),
    val captureRate: Int? = null,
    val genderRate: Int? = null,
    val genderMalePercent: Double? = null,
    val genderFemalePercent: Double? = null,
    val speciesGenus: String = "",
    val evolutionChain: EvolutionChainDomain? = null,
    val evolutionChainDetails: List<PokemonDetailsDomain> = emptyList()
)

/**
 * ViewModel encargado de gestionar la lógica y el estado de la pantalla de información de Pokémon.
 * Recupera detalles, especie, habilidades y cadena evolutiva del Pokémon.
 */
@HiltViewModel
class PokemonInfoViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    // ID del Pokémon seleccionado
    private val _pokemonId = MutableLiveData<Int>()
    // Estado de la UI observable
    private val _uiState = MutableLiveData(PokemonInfoUiState())
    val uiState: LiveData<PokemonInfoUiState> = _uiState

    /**
     * Establece el ID del Pokémon y dispara la carga de datos si cambia.
     */
    fun setPokemonId(id: Int) {
        if (_pokemonId.value == id) return
        _pokemonId.value = id
        fetchPokemon(id)
    }

    /**
     * Recupera todos los datos necesarios del Pokémon (detalles, especie, habilidades, cadena evolutiva).
     */
    private fun fetchPokemon(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            var descripcion: String
            var pokemon: PokemonDetailsDomain

            pokemonRepository.getPokemonDetailsById(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        pokemon = result.data
                        pokemonRepository.getPokemonSpeciesById(id).collect { speciesResult ->
                            when (speciesResult) {
                                is Resource.Success -> {
                                    descripcion = extractFlavorText(speciesResult.data)
                                    val isFavorite = pokemonRepository.isPokemonFavorite(id)
                                    // Obtener nombres y descripciones localizadas de habilidades
                                    val habilidades = mutableListOf<HabilidadUi>()
                                    val abilityList = pokemon.abilities?.mapNotNull { it?.ability } ?: emptyList()
                                    val abilitySlotList = pokemon.abilities ?: emptyList()
                                    for ((idx, ability) in abilityList.withIndex()) {
                                        val isOculta = abilitySlotList.getOrNull(idx)?.is_hidden ?: false
                                        val abilityId = ability.url?.trimEnd('/')?.split("/")?.lastOrNull()?.toIntOrNull()
                                        if (abilityId != null) {
                                            val abilityResult = pokemonRepository.getAbilityById(abilityId)
                                            abilityResult.collect { abRes ->
                                                if (abRes is Resource.Success) {
                                                    val abilityData = abRes.data
                                                    val localizedName = abilityData.names?.firstOrNull { n ->
                                                        val langName = if (Locale.getDefault().language == "es") "es" else "en"
                                                        n?.language?.name == langName
                                                    }?.name
                                                        ?: abilityData.names?.firstOrNull { n -> n?.language?.name == "en" }?.name
                                                        ?: ability.name?.replaceFirstChar { it.uppercase() }
                                                    val localizedFlavor = abilityData.flavor_text_entries?.firstOrNull {
                                                        val langName = if (Locale.getDefault().language == "es") "es" else "en"
                                                        it?.language?.name == langName
                                                    }?.flavor_text
                                                        ?: abilityData.flavor_text_entries?.firstOrNull { it?.language?.name == "en" }?.flavor_text
                                                    val descText = (localizedFlavor ?: "")
                                                        .replace("\n", " ")
                                                        .replace("\u000c", " ")
                                                        .trim()
                                                    if (localizedName != null) {
                                                        habilidades.add(
                                                            HabilidadUi(
                                                                nombre = localizedName.replaceFirstChar { it.uppercase() },
                                                                descripcion = descText,
                                                                isOculta = isOculta
                                                            )
                                                        )
                                                    }
                                                } else if (abRes is Resource.Error) {
                                                    habilidades.add(
                                                        HabilidadUi(
                                                            nombre = ability.name?.replaceFirstChar { it.uppercase() } ?: "",
                                                            descripcion = "",
                                                            isOculta = isOculta
                                                        )
                                                    )
                                                }
                                            }
                                        } else {
                                            habilidades.add(
                                                HabilidadUi(
                                                    nombre = ability.name?.replaceFirstChar { it.uppercase() } ?: "",
                                                    descripcion = "",
                                                    isOculta = isOculta
                                                )
                                            )
                                        }
                                    }
                                    // Calcular porcentajes de género
                                    val genderRate = speciesResult.data.gender_rate
                                    val (malePercent, femalePercent) = calculateGenderPercents(genderRate)
                                    // Obtener genus/especie en el idioma adecuado
                                    val genus = speciesResult.data.genera
                                        ?.firstOrNull { it?.language?.name == (if (Locale.getDefault().language == "es") "es" else "en") }
                                        ?.genus
                                        ?: speciesResult.data.genera?.firstOrNull { it?.language?.name == "en" }?.genus
                                        ?: ""
                                    // Obtener evolution chain si existe
                                    val evolutionChainId = speciesResult.data.evolution_chain?.url
                                        ?.trimEnd('/')?.split("/")?.lastOrNull()?.toIntOrNull()
                                    var evolutionChain: EvolutionChainDomain? = null
                                    var evolutionChainDetails: List<PokemonDetailsDomain> = emptyList()
                                    if (evolutionChainId != null) {
                                        pokemonRepository.getEvolutionChainById(evolutionChainId).collect { evoResult ->
                                            if (evoResult is Resource.Success) {
                                                evolutionChain = evoResult.data
                                                val pokemonNames = evolutionChain?.getPokemonNames() ?: emptyList()
                                                val detailsList = mutableListOf<PokemonDetailsDomain>()
                                                for (name in pokemonNames) {
                                                    pokemonRepository.getPokemonDetailsByName(name).collect { detailsResult ->
                                                        if (detailsResult is Resource.Success) {
                                                            detailsList.add(detailsResult.data)
                                                        }
                                                    }
                                                }
                                                evolutionChainDetails = detailsList
                                            }
                                        }
                                    }
                                    _uiState.value = PokemonInfoUiState(
                                        isLoading = false,
                                        id = pokemon.id?.toString() ?: "",
                                        nombre = pokemon.name?.replaceFirstChar { it.uppercase() } ?: "",
                                        height = formatHeight(pokemon.height),
                                        weight = formatWeight(pokemon.weight),
                                        imageUrl = pokemon.sprites?.other?.`official-artwork`?.front_default
                                            ?: pokemon.sprites?.front_default,
                                        types = pokemon.types?.mapNotNull { typeInfo ->
                                            typeInfo?.type?.name?.let { typeName ->
                                                val type = PokemonTypeUtil.getTypeByName(typeName)
                                                PokemonTypeUi(
                                                    name = type.name,
                                                    color = type.colorRes,
                                                    stringRes = type.stringRes
                                                )
                                            }
                                        } ?: emptyList(),
                                        descripcion = descripcion,
                                        isFavorite = isFavorite,
                                        habilidades = habilidades,
                                        captureRate = speciesResult.data.capture_rate,
                                        genderRate = genderRate,
                                        genderMalePercent = malePercent,
                                        genderFemalePercent = femalePercent,
                                        speciesGenus = genus,
                                        evolutionChain = evolutionChain,
                                        evolutionChainDetails = evolutionChainDetails
                                    )
                                }
                                is Resource.Error -> {
                                    _uiState.value = _uiState.value?.copy(
                                        isLoading = false,
                                        error = speciesResult.message ?: "Error al cargar especie"
                                    )
                                }
                                is Resource.Loading -> {
                                    _uiState.value = _uiState.value?.copy(isLoading = true)
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            error = result.message ?: "Error al cargar Pokémon"
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
     * Marca o desmarca un Pokémon como favorito.
     */
    fun toggleFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            pokemonRepository.updatePokemonFavorite(id, isFavorite)
            _uiState.value = _uiState.value?.copy(isFavorite = isFavorite)
        }
    }
}

/**
 * Extrae el texto descriptivo (flavor text) de la especie de Pokémon en el idioma actual.
 */
fun extractFlavorText(species: PokemonSpeciesDomain?): String {
    if (species == null) return ""
    val lang = Locale.getDefault().language
    val entries = species.flavor_text_entries ?: return ""
    val flavor = entries.firstOrNull {
        val l = it?.language?.name
        l == lang
    }?.flavor_text
    val fallback = entries.firstOrNull { it?.language?.name == "en" }?.flavor_text
    return (flavor ?: fallback ?: "").replace("\n", " ").replace("\u000c", " ").trim()
}

/**
 * Formatea la altura del Pokémon según el idioma (metros o pulgadas).
 */
fun formatHeight(heightInDecimeters: Int?): String {
    if (heightInDecimeters == null) return ""
    val isEnglish = Locale.getDefault().language == "en"
    return if (isEnglish) {
        val heightInInches = heightInDecimeters * 3.93701
        String.format("%.1f \"", heightInInches)
    } else {
        val heightInMeters = heightInDecimeters * 0.1
        String.format("%.1f m", heightInMeters)
    }
}

/**
 * Formatea el peso del Pokémon según el idioma (kg o libras).
 */
fun formatWeight(weightInHectograms: Int?): String {
    if (weightInHectograms == null) return ""
    val isEnglish = Locale.getDefault().language == "en"
    return if (isEnglish) {
        val weightInPounds = weightInHectograms * 0.22046
        String.format("%.1f lbs", weightInPounds)
    } else {
        val weightInKg = weightInHectograms * 0.1
        String.format("%.1f kg", weightInKg)
    }
}

/**
 * Calcula el porcentaje de género masculino y femenino a partir del gender_rate.
 */
fun calculateGenderPercents(genderRate: Int?): Pair<Double?, Double?> {
    return when (genderRate) {
        null -> Pair(null, null)
        -1 -> Pair(0.0, 0.0)
        0 -> Pair(100.0, 0.0)
        8 -> Pair(0.0, 100.0)
        in 1..7 -> {
            val female = genderRate / 8.0 * 100.0
            val male = 100.0 - female
            Pair(male, female)
        }
        else -> Pair(null, null)
    }
}

/**
 * Obtiene todos los nombres de Pokémon en la cadena evolutiva, recorriendo todos los niveles de anidamiento.
 */
fun EvolutionChainDomain?.getPokemonNames(): List<String> {
    if (this == null) return emptyList()
    val result = mutableListOf<String>()

    // Función recursiva única que acepta cualquier nodo de la cadena evolutiva
    fun traverse(node: Any?) {
        when (node) {
            is EvolutionChainDomain.Chain -> {
                node.species?.name?.let { result.add(it) }
                node.evolves_to?.forEach { traverse(it) }
            }
            is EvolutionChainDomain.Chain.EvolvesTo -> {
                node.species?.name?.let { result.add(it) }
                node.evolves_to?.forEach { traverse(it) }
            }
            is EvolutionChainDomain.Chain.EvolvesTo.EvolvesTo -> {
                node.species?.name?.let { result.add(it) }
                node.evolves_to?.forEach { traverse(it) }
            }
        }
    }

    traverse(this.chain)
    return result
}