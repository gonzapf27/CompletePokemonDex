package com.example.completepokemondex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonDetallesBinding

/**
 * Fragmento que muestra los detalles de un Pokémon específico.
 * Este fragmento recibe el ID numérico de un Pokémon a través de los argumentos
 * y muestra información detallada sobre él.
 */
class PokemonDetallesFragment : Fragment() {
    private var _binding: FragmentPokemonDetallesBinding? = null

    /** Propiedad que provee acceso seguro al binding mientras la vista existe */
    private val binding get() = _binding!!

    /** ViewModel que maneja la lógica de presentación y los datos del Pokémon */
    private val viewModel: PokemonDetallesViewModel by viewModels {
        PokemonDetallesViewModel.Factory(PokedexDatabase.getDatabase(requireContext()))
    }

    companion object {
        private const val ARG_POKEMON_ID = "pokemon_id"

        /**
         * Crea una nueva instancia del fragmento con el ID numérico del Pokémon como argumento.
         *
         * @param pokemonId El ID numérico del Pokémon cuyos detalles se mostrarán.
         *
         * @return Una nueva instancia de [PokemonDetallesFragment].
         */
        fun newInstance(pokemonId: Int) = PokemonDetallesFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POKEMON_ID, pokemonId)
            }
        }
    }

    /**
     * Se ejecuta cuando se crea el fragmento.
     * Obtiene el ID numérico del Pokémon de los argumentos y lo establece en el ViewModel.
     *
     * @param savedInstanceState Estado guardado del fragmento, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            args.getInt(ARG_POKEMON_ID).let { viewModel.setPokemonId(it) }
        }
    }

    /**
     * Crea y configura la vista del fragmento.
     * Inicializa el binding para acceder a los elementos de la interfaz de usuario.
     *
     * @param inflater Inflador utilizado para inflar la vista.
     * @param container Contenedor donde se colocará la vista.
     * @param savedInstanceState Estado guardado del fragmento, si existe.
     * @return La vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetallesBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se ejecuta después de que la vista ha sido creada.
     *
     * @param view La vista raíz del fragmento.
     * @param savedInstanceState Estado guardado del fragmento, si existe.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeViewModel()
        // Obtenemos los detalles del Pokémon
        viewModel.fetchPokemon()
    }

    private fun observeViewModel() {
        // Observar el POkemon details y actualizarlo
        viewModel.pokemonDetails.observe(viewLifecycleOwner) { pokemon ->
            // Actualizar ID
            binding.pokemonDetailsId.text = pokemon.id.toString()
            // Actualizar Height
            binding.pokemonDetailsHeight.text = pokemon.height.toString()
            // Actualizar Weight
            binding.pokemonDetailsWeight.text = pokemon.weight.toString()
            
            // Actualizar los tipos de Pokémon con chips
            updatePokemonTypes(pokemon)
            
            // Actualizar imagen
            // Usar operadores de navegación segura para evitar NullPointerException
            val imageUrl = pokemon.sprites?.other?.`official-artwork`?.front_default ?: pokemon.sprites?.front_default
            // Mostrar los sprites del pokemon por el log
            Log.d("PokemonDetallesFragment", "Sprites: ${pokemon.sprites}")
            
            // Si imageUrl no es nulo, cargar la imagen
            imageUrl?.let {
                // Usar Glide para cargar la imagen desde la URL
                com.bumptech.glide.Glide.with(requireContext())
                    .load(it)
                    .into(binding.pokemonImage)
            }
        }
    }
    
    /**
     * Actualiza los chips con los tipos del Pokémon
     * @param pokemon El modelo de dominio del Pokémon con sus detalles
     */
    private fun updatePokemonTypes(pokemon: PokemonDetailsDomain) {
        // Limpiar tipos anteriores
        binding.pokemonTypeChips.removeAllViews()
        
        // Verificar que los tipos no sean nulos
        pokemon.types?.forEach { typeInfo ->
            typeInfo?.type?.name?.let { typeName ->
                // Obtener la string del recurso correspondiente al tipo
                val typeStringRes = getStringResourceForPokemonType(typeName)
                
                // Crear un nuevo chip para cada tipo
                val chip = com.google.android.material.chip.Chip(context).apply {
                    // Usar el recurso de string traducido si existe, o capitalizar el nombre original
                    text = if (typeStringRes != 0) {
                        getString(typeStringRes)
                    } else {
                        typeName.replaceFirstChar { it.uppercase() }
                    }
                    isCheckable = false
                    
                    // Establecer colores según el tipo
                    val typeColor = getColorForPokemonType(typeName)
                    chipBackgroundColor = android.content.res.ColorStateList.valueOf(typeColor)
                    setTextColor(android.graphics.Color.WHITE)
                }
                
                // Añadir el chip al grupo
                binding.pokemonTypeChips.addView(chip)
            }
        }
    }
    
    /**
     * Retorna el recurso de string correspondiente a cada tipo de Pokémon
     * @param type El nombre del tipo de Pokémon
     * @return El ID del recurso de string correspondiente al tipo, o 0 si no existe
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
            else -> 0 // Devuelve 0 si no existe un recurso para este tipo
        }
    }
    
    /**
     * Retorna el color correspondiente a cada tipo de Pokémon
     * @param type El nombre del tipo de Pokémon
     * @return El color asociado al tipo
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
     * Se ejecuta cuando la vista del fragmento está siendo destruida.
     * Limpia la referencia al binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}