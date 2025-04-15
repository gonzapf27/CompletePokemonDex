package com.example.completepokemondex

import androidx.lifecycle.*
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.domain.model.PokemonSpeciesDomain
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
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
    val isFavorite: Boolean = false
)

data class PokemonTypeUi(
    val name: String,
    val color: Int,
    val stringRes: Int
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

            // Primero obtenemos detalles del Pokémon
            pokemonRepository.getPokemonDetailsById(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        pokemon = result.data
                        // Ahora obtenemos la especie para la descripción
                        pokemonRepository.getPokemonSpeciesById(id).collect { speciesResult ->
                            when (speciesResult) {
                                is Resource.Success -> {
                                    descripcion = extractFlavorText(speciesResult.data)
                                    // Obtener si es favorito desde el repositorio/base de datos
                                    val isFavorite = pokemonRepository.isPokemonFavorite(id)
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
                                                PokemonTypeUi(
                                                    name = typeName,
                                                    color = getColorForPokemonType(typeName),
                                                    stringRes = getStringResourceForPokemonType(typeName)
                                                )
                                            }
                                        } ?: emptyList(),
                                        descripcion = descripcion,
                                        isFavorite = isFavorite
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
     * Devuelve el recurso de cadena (String Resource) correspondiente al tipo de Pokémon.
     * Se utiliza para mostrar el nombre del tipo en la UI.
     *
     * @param type El nombre del tipo de Pokémon (ej. "fire", "water").
     * @return El ID del recurso de cadena correspondiente al tipo de Pokémon.
     */
    private fun getStringResourceForPokemonType(type: String): Int {
        return when (type.lowercase()) {
            "normal" -> R.string.type_normal
            "fire" -> R.string.type_fire
            "water" -> R.string.type_water
            "electric" -> R.string.type_electric
            "grass" -> R.string.type_grass
            "ice" -> R.string.type_ice
            "fighting" -> R.string.type_fighting
            "poison" -> R.string.type_poison
            "ground" -> R.string.type_ground
            "flying" -> R.string.type_flying
            "psychic" -> R.string.type_psychic
            "bug" -> R.string.type_bug
            "rock" -> R.string.type_rock
            "ghost" -> R.string.type_ghost
            "dragon" -> R.string.type_dragon
            "dark" -> R.string.type_dark
            "steel" -> R.string.type_steel
            "fairy" -> R.string.type_fairy
            else -> 0
        }
    }

    /**
     * Devuelve el color asociado al tipo de Pokémon.
     * Se utiliza para mostrar el color del tipo en la UI.
     *
     * @param type El nombre del tipo de Pokémon (ej. "fire", "water").
     * @return El color correspondiente al tipo de Pokémon.
     */
    private fun getColorForPokemonType(type: String): Int {
        return when (type.lowercase()) {
            "normal" -> android.graphics.Color.parseColor("#A8A878")
            "fire" -> android.graphics.Color.parseColor("#F08030")
            "water" -> android.graphics.Color.parseColor("#6890F0")
            "electric" -> android.graphics.Color.parseColor("#F8D030")
            "grass" -> android.graphics.Color.parseColor("#78C850")
            "ice" -> android.graphics.Color.parseColor("#98D8D8")
            "fighting" -> android.graphics.Color.parseColor("#C03028")
            "poison" -> android.graphics.Color.parseColor("#A040A0")
            "ground" -> android.graphics.Color.parseColor("#E0C068")
            "flying" -> android.graphics.Color.parseColor("#A890F0")
            "psychic" -> android.graphics.Color.parseColor("#F85888")
            "bug" -> android.graphics.Color.parseColor("#A8B820")
            "rock" -> android.graphics.Color.parseColor("#B8A038")
            "ghost" -> android.graphics.Color.parseColor("#705898")
            "dragon" -> android.graphics.Color.parseColor("#7038F8")
            "dark" -> android.graphics.Color.parseColor("#705848")
            "steel" -> android.graphics.Color.parseColor("#B8B8D0")
            "fairy" -> android.graphics.Color.parseColor("#EE99AC")
            else -> android.graphics.Color.GRAY
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
