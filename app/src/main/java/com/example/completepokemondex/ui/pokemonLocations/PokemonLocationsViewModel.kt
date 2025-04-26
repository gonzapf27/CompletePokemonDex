package com.example.completepokemondex.ui.pokemonLocations

import androidx.lifecycle.*
import com.example.completepokemondex.data.domain.model.PokemonEncountersDomain
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * Clase que representa un encuentro de Pokémon para la UI
 */
data class LocationEncounterUi(
    val locationName: String,
    val locationAreaName: String, // Nombre original del área para usar con PokemonLocationsUtil
    val games: List<String>,
    val levels: String,
    val chance: String,
    val methods: List<String>
)

/**
 * Estado de la UI para el fragmento de localizaciones
 */
data class PokemonLocationsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val id: String = "",
    val nombre: String = "",
    val imageUrl: String? = null,
    val encounters: List<LocationEncounterUi> = emptyList(),
    val hasEncounters: Boolean = false,
    val locationNames: List<String> = emptyList(), // Lista de nombres de ubicación para marcar en el mapa
    val pokemonTypes: List<String>? = null // <-- NUEVO
)

@HiltViewModel
class PokemonLocationsVIewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemonId = MutableLiveData<Int>()
    private val _uiState = MutableLiveData(PokemonLocationsUiState())
    val uiState: LiveData<PokemonLocationsUiState> = _uiState

    // Lista de versiones que queremos filtrar (solo Red y Blue)
    private val targetVersions = listOf("red", "blue")

    fun setPokemonId(id: Int) {
        if (_pokemonId.value == id) return
        _pokemonId.value = id
        fetchPokemonEncounters(id)
    }

    private fun fetchPokemonEncounters(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)

            // Primero obtenemos los detalles básicos del Pokémon para mostrar nombre e imagen
            pokemonRepository.getPokemonDetailsById(id).collect { detailsResult ->
                when (detailsResult) {
                    is Resource.Success -> {
                        val pokemon = detailsResult.data
                        val pokemonName = pokemon.name?.replaceFirstChar { it.uppercase() } ?: ""
                        val imageUrl = pokemon.sprites?.other?.`official-artwork`?.front_default
                            ?: pokemon.sprites?.front_default
                        // Obtener tipos del Pokémon
                        val pokemonTypes = pokemon.types?.mapNotNull { it?.type?.name } ?: emptyList()

                        // Ahora obtenemos las localizaciones de encuentro
                        pokemonRepository.getPokemonEncountersById(id).collect { encountersResult ->
                            when (encountersResult) {
                                is Resource.Success -> {
                                    val encounters = processEncounters(encountersResult.data)
                                    _uiState.value = _uiState.value?.copy(
                                        isLoading = false,
                                        id = pokemon.id?.toString() ?: "",
                                        nombre = pokemonName,
                                        imageUrl = imageUrl,
                                        encounters = encounters,
                                        hasEncounters = encounters.isNotEmpty(),
                                        pokemonTypes = pokemonTypes // <-- NUEVO
                                    )
                                }

                                is Resource.Error -> {
                                    _uiState.value = _uiState.value?.copy(
                                        isLoading = false,
                                        error = encountersResult.message
                                            ?: "Error al cargar localizaciones",
                                        nombre = pokemonName,
                                        imageUrl = imageUrl,
                                        hasEncounters = false,
                                        pokemonTypes = pokemonTypes // <-- NUEVO
                                    )
                                }

                                is Resource.Loading -> {
                                    // Mantenemos el estado de carga
                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value?.copy(
                            isLoading = false,
                            error = detailsResult.message ?: "Error al cargar datos del Pokémon"
                        )
                    }

                    is Resource.Loading -> {
                        // Mantenemos el estado de carga
                    }
                }
            }
        }
    }

    /**
     * Procesa los datos de encuentros del dominio a un formato más amigable para la UI,
     * filtrando solo para los juegos Rojo y Azul
     */
    private fun processEncounters(encounters: PokemonEncountersDomain): List<LocationEncounterUi> {
        val result = mutableListOf<LocationEncounterUi>()
        val locationNames = mutableListOf<String>() // Para almacenar nombres de ubicación

        // Iterar sobre cada área de localización
        encounters.items.forEach { encounter ->
            val locationAreaName = encounter.location_area?.name ?: ""
            val locationName = formatLocationName(locationAreaName)

            // Agrupar por versión/juego, pero solo para Rojo y Azul
            val gameEncounters =
                mutableMapOf<String, MutableList<PokemonEncountersDomain.LocationAreaEncounter.VersionDetail.EncounterDetail>>()
            encounter.version_details?.filterNotNull()?.forEach { versionDetail ->
                val versionName = versionDetail.version?.name ?: ""
                // Solo procesamos Red y Blue
                if (targetVersions.contains(versionName)) {
                    val gameName = formatGameName(versionName)
                    versionDetail.encounter_details?.filterNotNull()?.forEach { detail ->
                        if (!gameEncounters.containsKey(gameName)) {
                            gameEncounters[gameName] = mutableListOf()
                        }
                        gameEncounters[gameName]?.add(detail)
                    }
                }
            }

            // Si hay encuentros para esta ubicación en Red o Azul
            if (gameEncounters.isNotEmpty()) {
                // Extraer los juegos
                val games = gameEncounters.keys.toList()

                // Añadir nombre de ubicación a la lista para el mapa
                locationNames.add(locationAreaName)

                // Calcular rango de niveles combinando todos los detalles de encuentro
                val allLevels = mutableSetOf<Int>()
                gameEncounters.values.flatten().forEach { detail ->
                    detail.min_level?.let { min -> allLevels.add(min) }
                    detail.max_level?.let { max -> allLevels.add(max) }
                }
                val minLevel = allLevels.minOrNull() ?: 0
                val maxLevel = allLevels.maxOrNull() ?: 0
                val levels =
                    if (minLevel == maxLevel) "Nivel $minLevel" else "Niveles $minLevel-$maxLevel"

                // Calcular probabilidad promedio de encuentro
                val chances = gameEncounters.values.flatten().mapNotNull { it.chance }
                val avgChance = if (chances.isNotEmpty()) chances.average() else 0.0
                val chanceText = "${avgChance.toInt()}%"

                // Extraer métodos de encuentro
                val methods = gameEncounters.values.flatten()
                    .mapNotNull { it.method?.name }
                    .map { (it) }
                    .distinct()

                result.add(
                    LocationEncounterUi(
                        locationName = locationName,
                        locationAreaName = locationAreaName, // Guardar el nombre original para usar con el mapa
                        games = games,
                        levels = levels,
                        chance = chanceText,
                        methods = methods
                    )
                )
            }
        }

        // Actualizar los nombres de ubicación en el estado UI
        val currentState = _uiState.value
        _uiState.value = currentState?.copy(locationNames = locationNames)

        return result
    }

    /**
     * Formatea el nombre de la localización para ser más legible
     */
    private fun formatLocationName(name: String): String {
        return name.replace("-", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
    }

    /**
     * Formatea el nombre del juego para ser más legible
     */
    private fun formatGameName(name: String): String {
        // Mapeo de nombres de versión a nombres más amigables
        return when (name) {
            "red" -> "Rojo"
            "blue" -> "Azul"
            "yellow" -> "Amarillo"
            "gold" -> "Oro"
            "silver" -> "Plata"
            "crystal" -> "Cristal"
            "ruby" -> "Rubí"
            "sapphire" -> "Zafiro"
            "emerald" -> "Esmeralda"
            "firered" -> "Rojo Fuego"
            "leafgreen" -> "Verde Hoja"
            "diamond" -> "Diamante"
            "pearl" -> "Perla"
            "platinum" -> "Platino"
            "heartgold" -> "Oro HeartGold"
            "soulsilver" -> "Plata SoulSilver"
            else -> name.replaceFirstChar { it.uppercase() }
        }
    }
}
