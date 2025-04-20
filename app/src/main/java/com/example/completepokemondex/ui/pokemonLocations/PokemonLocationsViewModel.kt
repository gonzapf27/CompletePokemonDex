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
    val hasEncounters: Boolean = false
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
                                        hasEncounters = encounters.isNotEmpty()
                                    )
                                }
                                is Resource.Error -> {
                                    _uiState.value = _uiState.value?.copy(
                                        isLoading = false,
                                        error = encountersResult.message ?: "Error al cargar localizaciones",
                                        nombre = pokemonName,
                                        imageUrl = imageUrl,
                                        hasEncounters = false
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
        
        // Iterar sobre cada área de localización
        encounters.items.forEach { encounter ->
            val locationName = formatLocationName(encounter.location_area?.name ?: "")
            
            // Agrupar por versión/juego, pero solo para Rojo y Azul
            val gameEncounters = mutableMapOf<String, MutableList<PokemonEncountersDomain.LocationAreaEncounter.VersionDetail.EncounterDetail>>()
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
                
                // Calcular rango de niveles combinando todos los detalles de encuentro
                val allLevels = mutableSetOf<Int>()
                gameEncounters.values.flatten().forEach { detail ->
                    detail.min_level?.let { min -> allLevels.add(min) }
                    detail.max_level?.let { max -> allLevels.add(max) }
                }
                val minLevel = allLevels.minOrNull() ?: 0
                val maxLevel = allLevels.maxOrNull() ?: 0
                val levels = if (minLevel == maxLevel) "Nivel $minLevel" else "Niveles $minLevel-$maxLevel"
                
                // Calcular probabilidad promedio de encuentro
                val chances = gameEncounters.values.flatten().mapNotNull { it.chance }
                val avgChance = if (chances.isNotEmpty()) chances.average() else 0.0
                val chanceText = "${avgChance.toInt()}%"
                
                // Extraer métodos de encuentro
                val methods = gameEncounters.values.flatten()
                    .mapNotNull { it.method?.name }
                    .map { formatMethodName(it) }
                    .distinct()
                
                result.add(
                    LocationEncounterUi(
                        locationName = locationName,
                        games = games,
                        levels = levels,
                        chance = chanceText,
                        methods = methods
                    )
                )
            }
        }
        
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
            "black" -> "Negro"
            "white" -> "Blanco"
            "black-2" -> "Negro 2"
            "white-2" -> "Blanco 2"
            "x" -> "X"
            "y" -> "Y"
            "omega-ruby" -> "Rubí Omega"
            "alpha-sapphire" -> "Zafiro Alfa"
            "sun" -> "Sol"
            "moon" -> "Luna"
            "ultra-sun" -> "Ultra Sol"
            "ultra-moon" -> "Ultra Luna"
            "lets-go-pikachu" -> "Let's Go Pikachu"
            "lets-go-eevee" -> "Let's Go Eevee"
            "sword" -> "Espada"
            "shield" -> "Escudo"
            "scarlet" -> "Escarlata"
            "violet" -> "Púrpura"
            else -> name.replace("-", " ").replaceFirstChar { it.uppercase() }
        }
    }
    
    /**
     * Formatea el nombre del método de encuentro para ser más legible
     */
    private fun formatMethodName(method: String): String {
        return when (method) {
            "walk" -> "Caminando"
            "surf" -> "Surfeando"
            "old-rod" -> "Caña Vieja"
            "good-rod" -> "Caña Buena"
            "super-rod" -> "Super Caña"
            "rock-smash" -> "Rompiendo Rocas"
            "headbutt" -> "Cabezazo"
            "grass-spots" -> "En la hierba"
            "cave-spots" -> "En cuevas"
            "bridge-spots" -> "En puentes"
            "super-rod-spots" -> "Pescando"
            "dark-grass" -> "Hierba alta"
            "rough-terrain" -> "Terreno rocoso"
            "gift" -> "Regalo"
            "gift-egg" -> "Huevo"
            else -> method.replace("-", " ").replaceFirstChar { it.uppercase() }
        }
    }
}
