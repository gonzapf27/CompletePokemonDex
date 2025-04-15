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
    val habilidades: List<HabilidadUi> = emptyList() // Nombres y descripciones de habilidades
)

data class PokemonTypeUi(
    val name: String,
    val color: Int,
    val stringRes: Int
)

data class HabilidadUi(
    val nombre: String,
    val descripcion: String
)

class PokemonDetallesViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemonId = MutableLiveData<Int>()

    private val _uiState = MutableLiveData(PokemonDetallesUiState())
    val uiState: LiveData<PokemonDetallesUiState> = _uiState

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
    private fun fetchPokemon(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            var descripcion = ""
            var pokemon: PokemonDetailsDomain? = null

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
                                    val lang = Locale.getDefault().language
                                    val isSpanish = lang == "es"
                                    val abilityList = pokemon?.abilities?.mapNotNull { it?.ability } ?: emptyList()
                                    for (ability in abilityList) {
                                        val abilityId = ability.url?.trimEnd('/')?.split("/")?.lastOrNull()?.toIntOrNull()
                                        if (abilityId != null) {
                                            val abilityResult = pokemonRepository.getAbilityById(abilityId)
                                            abilityResult.collect { abRes ->
                                                if (abRes is Resource.Success) {
                                                    val abilityData = abRes.data
                                                    val isSpanish = Locale.getDefault().language == "es"

                                                    val localizedName = abilityData.names?.firstOrNull { n ->
                                                        if (isSpanish) n?.language?.name == "es" else n?.language?.name == "en"
                                                    }?.name
                                                        ?: abilityData.names?.firstOrNull { n -> n?.language?.name == "en" }?.name
                                                        ?: ability.name?.replaceFirstChar { it.uppercase() }


                                                    // Buscamos flavor text
                                                    val localizedFlavor = abilityData.flavor_text_entries?.firstOrNull {
                                                        if (isSpanish) it?.language?.name == "es" else it?.language?.name == "en"
                                                    }?.flavor_text
                                                        ?: abilityData.flavor_text_entries?.firstOrNull { it?.language?.name == "en" }?.flavor_text

                                                    val descripcion = (localizedFlavor ?: "")
                                                        .replace("\n", " ")
                                                        .replace("\u000c", " ")
                                                        .trim()

                                                    if (localizedName != null) {
                                                        habilidades.add(
                                                            HabilidadUi(
                                                                nombre = localizedName.replaceFirstChar { it.uppercase() },
                                                                descripcion = descripcion
                                                            )
                                                        )
                                                    }
                                                } else if (abRes is Resource.Error) {
                                                    habilidades.add(
                                                        HabilidadUi(
                                                            nombre = ability.name?.replaceFirstChar { it.uppercase() } ?: "",
                                                            descripcion = ""
                                                        )
                                                    )
                                                }
                                                return@collect
                                            }
                                        } else {
                                            habilidades.add(
                                                HabilidadUi(
                                                    nombre = ability.name?.replaceFirstChar { it.uppercase() } ?: "",
                                                    descripcion = ""
                                                )
                                            )
                                        }
                                    }
                                    _uiState.value = PokemonDetallesUiState(
                                        isLoading = false,
                                        id = pokemon?.id?.toString() ?: "",
                                        nombre = pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "",
                                        height = formatHeight(pokemon?.height),
                                        weight = formatWeight(pokemon?.weight),
                                        imageUrl = pokemon?.sprites?.other?.`official-artwork`?.front_default
                                            ?: pokemon?.sprites?.front_default,
                                        types = pokemon?.types?.mapNotNull { typeInfo ->
                                            typeInfo?.type?.name?.let { typeName ->
                                                val type = PokemonTypeUtil.getTypeByName(typeName)
                                                PokemonTypeUi(
                                                    name = type.name,
                                                    color = type.color,
                                                    stringRes = type.stringRes
                                                )
                                            }
                                        } ?: emptyList(),
                                        descripcion = descripcion,
                                        isFavorite = isFavorite,
                                        habilidades = habilidades
                                    )
                                }
                                is Resource.Error -> {
                                    _uiState.value = _uiState.value?.copy(
                                        isLoading = false,
                                        error = speciesResult.message
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
                            error = result.message
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
     * Extrae el flavor text en el idioma del sistema (es, en, o en por defecto).
     */
    private fun extractFlavorText(species: PokemonSpeciesDomain?): String {
        if (species == null) return ""
        val lang = Locale.getDefault().language
        val entries = species.flavor_text_entries ?: return ""
        // Buscar primero en el idioma del sistema
        val flavor = entries.firstOrNull { 
            val l = it?.language?.name
            l == lang
        }?.flavor_text
        // Si no hay en el idioma del sistema, buscar en inglés
        val fallback = entries.firstOrNull { it?.language?.name == "en" }?.flavor_text
        // Si no hay ninguno, devolver vacío
        return (flavor ?: fallback ?: "").replace("\n", " ").replace("\u000c", " ").trim()
    }

    /**
     * Formatea la altura del Pokémon según el idioma del dispositivo.
     * Si el idioma es inglés, la altura se muestra en pulgadas, de lo contrario, en metros.
     *
     * @param heightInDecimeters La altura en decímetros.
     * @return La altura formateada como una cadena de texto.
     */
    private fun formatHeight(heightInDecimeters: Int?): String {
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
     * Formatea el peso del Pokémon según el idioma del dispositivo.
     * Si el idioma es inglés, el peso se muestra en libras, de lo contrario, en kilogramos.
     *
     * @param weightInHectograms El peso en hectogramos.
     * @return El peso formateado como una cadena de texto.
     */
    private fun formatWeight(weightInHectograms: Int?): String {
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
