package com.example.completepokemondex

import androidx.lifecycle.*
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
import com.example.completepokemondex.util.PokemonTypeUtil
import kotlinx.coroutines.launch
import java.util.Locale

data class PokemonDetallesUiState(
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
    val habilidades: List<HabilidadUi> = emptyList(), // Nombres y descripciones de habilidades
    val captureRate: Int? = null, // Tasa de captura
    val genderRate: Int? = null, // Nuevo: gender_rate crudo
    val genderMalePercent: Double? = null, // Nuevo: % macho
    val genderFemalePercent: Double? = null, // Nuevo: % hembra
    val speciesGenus: String = "" // Nuevo: especie (genera)
)

data class PokemonTypeUi(
    val name: String,
    val color: Int,
    val stringRes: Int
)

data class HabilidadUi(
    val nombre: String,
    val descripcion: String,
    val isOculta: Boolean? = false
)

class PokemonDetallesViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemonId = MutableLiveData<Int>()

    private val _uiState = MutableLiveData(PokemonDetallesUiState())
    val uiState: LiveData<PokemonDetallesUiState> = _uiState

    private var currentPokemonDetails: PokemonDetailsDomain? = null
    fun getCurrentPokemonDetailsDomain() = currentPokemonDetails

    /**
     * Establece el ID del Pokémon para buscar sus detalles.
     *
     * @param id El ID del Pokémon.
     */
    fun setPokemonId(id: Int) {
        if (_pokemonId.value == id) return
        _pokemonId.value = id
        fetchPokemon(id)
    }

    /**
     * Busca y recupera los detalles del Pokémon por su ID.
     * Actualiza el estado de la UI con los detalles del Pokémon,
     * incluyendo datos como el nombre, altura, peso, imagen, tipos y descripción.
     *
     * @param id El ID del Pokémon a buscar.
     */
    fun fetchPokemon(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            var descripcion: String
            var pokemon: PokemonDetailsDomain

            pokemonRepository.getPokemonDetailsById(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        pokemon = result.data
                        currentPokemonDetails = pokemon // Guardar referencia
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
                                                    // Buscamos flavor text
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
                                    _uiState.value = PokemonDetallesUiState(
                                        isLoading = false,
                                        id = pokemon.id?.toString() ?: "",
                                        nombre = pokemon.name?.replaceFirstChar { it.uppercase() } ?: "",
                                        height = formatHeight(pokemon.height),
                                        weight = formatWeight(pokemon.weight),
                                        imageUrl = pokemon.sprites?.other?.`official-artwork`?.front_default
                                            ?: pokemon.sprites?.front_default,
                                        types = pokemon.types?.mapNotNull { typeInfo ->
                                            typeInfo?.type?.name?.let { typeName ->
                                                val type = com.example.completepokemondex.util.PokemonTypeUtil.getTypeByName(typeName)
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
                                        speciesGenus = genus // Nuevo campo
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
     *
     * @param id El ID del Pokémon a modificar.
     * @param isFavorite Si el Pokémon debe ser marcado como favorito (true) o no (false).
     */
    fun toggleFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            pokemonRepository.updatePokemonFavorite(id, isFavorite)
            // Actualiza el estado localmente para que la UI reaccione sin recargar todo
            _uiState.value = _uiState.value?.copy(isFavorite = isFavorite)
        }
    }

    /**
     * Factory para crear instancias de [PokemonDetallesViewModel].
     * Se encarga de la creación del ViewModel con sus dependencias.
     *
     * @param database La base de datos de Pokedex.
     * @property database La instancia de la base de datos [PokedexDatabase].
     * @suppress UNCHECKED_CAST Para evitar advertencias de tipo durante la creación del ViewModel.
     */
    class Factory(private val database: PokedexDatabase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PokemonDetallesViewModel::class.java)) {
                val pokemonDao = database.pokemonDao()
                val remoteDataSource = PokemonRemoteDataSource()
                val pokemonDetailsDao = database.pokemonDetailsDao()
                val pokemonSpeciesDao = database.pokemonSpeciesDao()
                val abilityDao = database.abilityDao()
                val repository = PokemonRepository(pokemonDao, pokemonDetailsDao, pokemonSpeciesDao, abilityDao, remoteDataSource)
                return PokemonDetallesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

// --- FUNCIONES AUXILIARES FUERA DE LA CLASE ---

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
