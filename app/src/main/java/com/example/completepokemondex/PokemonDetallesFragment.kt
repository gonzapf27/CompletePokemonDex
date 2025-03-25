package com.example.completepokemondex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonDetallesBinding
import com.example.completepokemondex.domain.model.PokemonDetailsDomain

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
        // Observar el ID del Pokémon y actualizarlo
        viewModel.idPokemon.observe(viewLifecycleOwner) { idPokemon ->
            binding.pokemonDetailsId.text = idPokemon.toString()
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