package com.example.completepokemondex.ui.statsPokemon

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.databinding.FragmentPokemonStatsBinding
import com.example.completepokemondex.util.PokemonTypeUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento encargado de mostrar las estadísticas de un Pokémon.
 * Observa el estado del ViewModel y actualiza la UI en consecuencia.
 */
@AndroidEntryPoint
class PokemonStatsFragment : Fragment() {
    private var _binding: FragmentPokemonStatsBinding? = null
    private val binding get() = _binding!!

    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }
    private val viewModel: PokemonStatsViewModel by viewModels()

    /**
     * Se llama al crear el fragmento.
     * Inicializa el ViewModel con el ID del Pokémon.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPokemonId(pokemonId)
    }

    /**
     * Infla el layout del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se llama cuando la vista ha sido creada.
     * Observa el estado de la UI y muestra las estadísticas cuando están disponibles.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (!state.isLoading && state.pokemon != null) {
                showStats(state.pokemon)
                // Gradiente igual que en fragment_pokemon_info.xml
                val gradientBg = binding.root.findViewById<View>(R.id.pokemon_stats_gradient_bg)
                val typeColors = state.pokemon.types?.take(2)?.mapNotNull { typeInfo ->
                    typeInfo?.type?.name?.let { typeName ->
                        // Usa el utilitario para obtener el color del tipo
                        val type = PokemonTypeUtil.getTypeByName(typeName)
                        ContextCompat.getColor(requireContext(), type.colorRes)
                    }
                } ?: listOf(ContextCompat.getColor(requireContext(), R.color.type_normal))
                val gradientColors = when {
                    typeColors.isEmpty() -> intArrayOf(Color.LTGRAY, Color.LTGRAY)
                    typeColors.size == 1 -> intArrayOf(typeColors[0], typeColors[0])
                    else -> typeColors.toIntArray()
                }
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    gradientColors
                )
                gradientDrawable.cornerRadius = 0f
                gradientBg?.background = gradientDrawable
            }
        }
    }

    /**
     * Limpia el binding al destruir la vista.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Muestra las estadísticas del Pokémon recibido.
     * Construye dinámicamente las filas de estadísticas y actualiza el total.
     *
     * @param pokemon Detalles del Pokémon a mostrar.
     */
    fun showStats(pokemon: PokemonDetailsDomain) {
        val statsContainer = binding.statsContainer
        statsContainer.removeAllViews()

        val stats = pokemon.stats?.filterNotNull() ?: emptyList()
        var total = 0
        val maxStat = 255 // Máximo base para una barra de progreso

        for (stat in stats) {
            val statName = stat.stat?.name?.replace("-", " ")?.replaceFirstChar { it.uppercase() } ?: "?"
            val statValue = stat.base_stat ?: 0
            total += statValue

            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setPadding(8)
                gravity = Gravity.CENTER_VERTICAL
            }

            val nameView = TextView(requireContext()).apply {
                text = statName
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            val valueView = TextView(requireContext()).apply {
                text = statValue.toString()
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setPadding(8, 0, 8, 0)
            }
            val progressBar = ProgressBar(
                requireContext(),
                null,
                android.R.attr.progressBarStyleHorizontal
            ).apply {
                max = maxStat
                progress = statValue
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f)
            }

            row.addView(nameView)
            row.addView(progressBar)
            row.addView(valueView)
            statsContainer.addView(row)
        }

        // Asegurarse de que el campo Total esté fuera del statsContainer
        val parent = statsContainer.parent as? ViewGroup
        val totalLabel = parent?.findViewById<TextView>(R.id.text_stats_total)
        totalLabel?.text = if (stats.isNotEmpty()) total.toString() else "-"
    }

    companion object {
        /**
         * Crea una nueva instancia del fragmento con el ID del Pokémon.
         *
         * @param pokemonId ID del Pokémon.
         */
        fun newInstance(pokemonId: Int) = PokemonStatsFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}