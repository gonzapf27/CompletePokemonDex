package com.example.completepokemondex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.api.Resource
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import kotlinx.coroutines.launch

/**
 * ViewModel responsable de gestionar los datos para la vista de detalles de un Pokémon.
 * Maneja la carga de información detallada de un Pokémon específico y el estado de UI asociado.
 *
 * @property pokemonRepository Repositorio que proporciona acceso a los datos de Pokémon.
 */
class PokemonDetallesViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    /**
     * ID del Pokémon actual que se está visualizando.
     * Expuesto como LiveData inmutable para observación externa.
     */
    private val _idPokemon = MutableLiveData<Int>()
    val idPokemon: LiveData<Int> = _idPokemon

    /**
     * Detalles del Pokémon cargados.
     * Expuesto como LiveData inmutable para observación externa.
     */
    private val _pokemonDetails = MutableLiveData<PokemonDetailsDomain>()
    val pokemonDetails: LiveData<PokemonDetailsDomain> = _pokemonDetails

    /**
     * Estado de carga para mostrar indicadores visuales al usuario.
     * Expuesto como LiveData inmutable para observación externa.
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Mensaje de error en caso de fallo en la carga.
     * Expuesto como LiveData inmutable para observación externa.
     */
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Establece el ID del Pokémon del que se desean obtener los detalles.
     *
     * @param id El identificador único del Pokémon.
     */
    fun setPokemonId(id: Int) {
        _idPokemon.value = id
    }

    /**
     * Obtiene los detalles del Pokémon actual desde el repositorio.
     * Actualiza los estados de carga y error durante el proceso.
     */
    fun fetchPokemon() {
        val currentId = idPokemon.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            pokemonRepository.getPokemonDetailsById(currentId).collect { result ->
                when (result) {
                    is Resource.Success -> _pokemonDetails.value = result.data
                    is Resource.Error -> _error.value = result.message
                    is Resource.Loading -> _isLoading.value = true
                }
            }
            _isLoading.value = false
        }
    }

    /**
     * Factory para crear instancias del ViewModel con el repositorio necesario.
     * Proporciona el contexto adecuado para la creación del ViewModel.
     *
     * @property database Base de datos local que se utilizará para acceder a los DAO.
     */
    class Factory(private val database: PokedexDatabase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PokemonDetallesViewModel::class.java)) {
                val pokemonDao = database.pokemonDao()
                val remoteDataSource = PokemonRemoteDataSource()
                val repository = PokemonRepository(pokemonDao, remoteDataSource)
                return PokemonDetallesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
