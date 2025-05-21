package com.example.completepokemondex.ui.statsPokemon

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.databinding.FragmentPokemonStatsBinding
import com.example.completepokemondex.util.PokemonTypeUtil
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

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
        // Inyecta el mapper de nombres internacionalizados al ViewModel
        viewModel.setTypeNameMapper { typeName ->
            val type = PokemonTypeUtil.getTypeByName(typeName)
            if (type.stringRes != 0) requireContext().getString(type.stringRes)
            else typeName.replaceFirstChar { it.uppercase() }
        }
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
            }

            // Mostrar los tipos como chips de color correspondiente
            fun showTypeChips(
                chipGroup: com.google.android.material.chip.ChipGroup,
                typeNamesIntl: List<String>,
                typeNamesRaw: List<String>
            ) {
                chipGroup.removeAllViews()
                if (typeNamesRaw.isEmpty()) {
                    val chip = Chip(requireContext())
                    chip.text = "-"
                    chip.isClickable = false
                    chip.isCheckable = false
                    chip.chipCornerRadius = 24f
                    chip.textSize = 13f
                    chip.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelLarge)
                    chipGroup.addView(chip)
                } else {
                    typeNamesRaw.forEach { typeNameRaw ->
                        // Obtener el nombre internacionalizado correspondiente
                        val typeNameIntl = viewModel.translateTypeName(typeNameRaw)
                        
                        val chip = Chip(requireContext())
                        chip.text = typeNameIntl
                        chip.isClickable = false
                        chip.isCheckable = false
                        chip.chipCornerRadius = 24f
                        chip.textSize = 13f
                        chip.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelLarge)
                        
                        // Usar el objeto PokemonType correcto según el nombre original
                        val type = PokemonTypeUtil.getTypeByName(typeNameRaw)
                        chip.chipBackgroundColor = ColorStateList.valueOf(
                            ContextCompat.getColor(requireContext(), type.colorRes)
                        )
                        chip.setTextColor(Color.WHITE)
                        chipGroup.addView(chip)
                    }
                }
            }

            showTypeChips(binding.chipGroupResistencias, state.resistencias, state.resistenciasRaw)
            showTypeChips(binding.chipGroupInmunidades, state.inmunidades, state.inmunidadesRaw)
            showTypeChips(binding.chipGroupEfectividades, state.efectividades, state.efectividadesRaw)
        }

        // Gradiente de fondo según los tipos del Pokémon
        viewModel.pokemonTypes.observe(viewLifecycleOwner) { types ->
            val typeColors = types.take(2).map {
                val type = PokemonTypeUtil.getTypeByName(it)
                ContextCompat.getColor(requireContext(), type.colorRes)
            }
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
            val gradientBg = binding.root.findViewById<View>(R.id.pokemon_stats_gradient_bg)
            gradientBg?.background = gradientDrawable
        }
    }

    /**
     * Limpia el binding al destruir la vista.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun animateProgress(progressBar: ProgressBar, toProgress: Int, duration: Long = 2000L) {
        ObjectAnimator.ofInt(progressBar, "progress", 0, toProgress).apply {
            this.duration = duration
            start()
        }
    }

    /**
     * Muestra las estadísticas del Pokémon recibido.
     * Construye dinámicamente las filas de estadísticas y actualiza el total.
     *
     * @param pokemon Detalles del Pokémon a mostrar.
     */
    fun showStats(pokemon: PokemonDetailsDomain) {
        var total = 0
        pokemon.stats?.forEach { stat ->
            when (stat?.stat?.name) {
                "hp" -> {
                    stat.base_stat?.let { baseStat ->
                        animateProgress(binding.barHp, baseStat)
                        binding.lblHpNumber.text = baseStat.toString()
                        total += baseStat
                    }
                }

                "attack" -> {
                    stat.base_stat?.let { baseStat ->
                        animateProgress(binding.barAttack, baseStat)
                        binding.lblNumberAttack.text = baseStat.toString()
                        total += baseStat
                    }
                }

                "defense" -> {
                    stat.base_stat?.let { baseStat ->
                        animateProgress(binding.barDefense, baseStat)
                        binding.lblDefenseNumber.text = baseStat.toString()
                        total += baseStat
                    }
                }

                "special-attack" -> {
                    stat.base_stat?.let { baseStat ->
                        animateProgress(binding.barSpAttack, baseStat)
                        binding.lblSpAttackNumber.text = baseStat.toString()
                        total += baseStat
                    }
                }

                "special-defense" -> {
                    stat.base_stat?.let { baseStat ->
                        animateProgress(binding.barSpDefense, baseStat)
                        binding.lblSpDefenseNumber.text = baseStat.toString()
                        total += baseStat
                    }
                }

                "speed" -> {
                    stat.base_stat?.let { baseStat ->
                        animateProgress(binding.barSpeed, baseStat)
                        binding.lblSpeedNumber.text = baseStat.toString()
                        total += baseStat
                    }
                }
            }
        }
        animateProgress(binding.barTotal, total)

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
