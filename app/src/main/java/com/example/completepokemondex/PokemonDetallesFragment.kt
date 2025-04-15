package com.example.completepokemondex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonDetallesBinding
import com.google.android.material.chip.Chip

/**
 * [PokemonDetallesFragment] muestra información detallada sobre un Pokémon específico.
 *
 * Este fragmento recupera y presenta datos como el ID del Pokémon, su nombre, altura, peso, imagen y tipos.
 * Utiliza un [PokemonDetallesViewModel] para gestionar la recuperación de datos y el estado de la interfaz de usuario.
 *
 * El fragmento obtiene el ID del Pokémon de sus argumentos para recuperar los datos correctos del Pokémon.
 *
 * @constructor Crea una nueva instancia de [PokemonDetallesFragment]. Se recomienda usar [newInstance]
 *              para inicializar correctamente el fragmento con un ID de Pokémon.
 */
class PokemonDetallesFragment : Fragment() {
    private var _binding: FragmentPokemonDetallesBinding? = null
    private val binding get() = _binding!!

    /**
     * Instancia del `ViewModel` asociada al fragmento.
     *
     * Se utiliza el delegado `by viewModels` para crear y asociar el `ViewModel` al ciclo de vida
     * del fragmento. El `ViewModel` se inicializa utilizando una fábrica personalizada que requiere
     * la base de datos de la aplicación.
     */
    private val viewModel: PokemonDetallesViewModel by viewModels {
        PokemonDetallesViewModel.Factory(PokedexDatabase.getDatabase(requireContext()))
    }

    /**
     * Objeto companion que contiene constantes y métodos estáticos relacionados con el fragmento.
     *
     * Proporciona una forma de crear nuevas instancias del fragmento con los argumentos necesarios,
     * como el ID del Pokémon.
     */
    companion object {
        private const val ARG_POKEMON_ID = "pokemon_id"
        fun newInstance(pokemonId: Int) = PokemonDetallesFragment().apply {
            arguments = Bundle().apply { putInt(ARG_POKEMON_ID, pokemonId) }
        }
    }

    /**
     * Se llama cuando el fragmento se crea por primera vez.
     *
     * Este método se utiliza para inicializar el estado del fragmento, incluyendo
     * la recuperación del ID del Pokémon desde los argumentos y su configuración
     * en el `ViewModel`.
     *
     * @param savedInstanceState Estado guardado del fragmento, puede ser null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt(ARG_POKEMON_ID)?.let { viewModel.setPokemonId(it) }
    }

    /**
     * Crea y devuelve la vista jerárquica asociada a este fragmento.
     *
     * Este método infla el diseño del fragmento utilizando el objeto `FragmentPokemonDetallesBinding`.
     *
     * @param inflater Objeto [LayoutInflater] para inflar vistas en el fragmento.
     * @param container Contenedor padre al que se añadirá la vista del fragmento.
     * @param savedInstanceState Estado guardado del fragmento, puede ser null.
     * @return La vista raíz del diseño inflado para este fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetallesBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se llama cuando la vista del fragmento ha sido creada y está lista para ser configurada.
     *
     * Implementa la lógica principal para observar el estado de la UI y actualizar los elementos
     * visuales del fragmento, incluyendo:
     * - Indicador de carga
     * - Detalles del Pokémon (ID, nombre, altura, peso)
     * - Imagen del Pokémon
     * - Chips de tipos del Pokémon
     *
     * @param view La vista del fragmento que fue creada
     * @param savedInstanceState Estado guardado del fragmento, puede ser null
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.loadingIndicator.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            binding.pokemonDetailsId.text = state.id
            binding.pokemonDetailsNombre.text = state.nombre
            binding.pokemonDetailsHeight.text = state.height
            binding.pokemonDetailsWeight.text = state.weight

            // Imagen
            state.imageUrl?.let {
                com.bumptech.glide.Glide.with(requireContext())
                    .load(it)
                    .into(binding.pokemonImage)
            }

            // Chips de tipos
            binding.pokemonTypeChips.removeAllViews()
            state.types.forEach { typeUi ->
                val chip = Chip(context).apply {
                    text = if (typeUi.stringRes != 0) getString(typeUi.stringRes)
                    else typeUi.name.replaceFirstChar { it.uppercase() }
                    isCheckable = false
                    chipBackgroundColor = android.content.res.ColorStateList.valueOf(typeUi.color)
                    setTextColor(android.graphics.Color.WHITE)
                }
                binding.pokemonTypeChips.addView(chip)
            }
        }
    }

    /**
     * Se llama cuando la vista del fragmento se destruye.
     *
     * Este método se utiliza para limpiar referencias a la vista enlazada
     * (_binding) y evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}