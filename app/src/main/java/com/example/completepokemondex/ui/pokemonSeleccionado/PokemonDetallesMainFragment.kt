package com.example.completepokemondex.ui.pokemonSeleccionado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.ui.statsPokemon.PokemonStatsFragment
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonDetallesMainBinding
import com.example.completepokemondex.ui.infoPokemon.PokemonInfoFragment
import com.example.completepokemondex.ui.pokemonLocations.PokemonLocationsFragment
import com.example.completepokemondex.ui.pokemonLocations.PokemonLocationsVIewModel
import com.example.completepokemondex.ui.spritesPokemon.PokemonSpritesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetallesMainFragment : Fragment() {
    private var _binding: FragmentPokemonDetallesMainBinding? = null
    private val binding get() = _binding!!

    private var pokemonId: Int = 0
    private var currentDestination: PokemonDetallesViewModel.NavDestination? = null

    private val viewModel: PokemonDetallesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonId = arguments?.getInt("pokemon_id") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetallesMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.setInitialPokemonId(pokemonId)
        }
        binding.pokemonDetallesBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_info -> {
                    viewModel.navigateTo(PokemonDetallesViewModel.NavDestination.INFO)
                    true
                }
                R.id.nav_stats -> {
                    viewModel.navigateTo(PokemonDetallesViewModel.NavDestination.STATS)
                    true
                }
                R.id.nav_sprites -> {
                    viewModel.navigateTo(PokemonDetallesViewModel.NavDestination.SPRITES)
                    true
                }
                R.id.nav_encounters -> {
                    viewModel.navigateTo(PokemonDetallesViewModel.NavDestination.LOCATIONS)
                    true
                }
                else -> false
            }
        }

        viewModel.navState.observe(viewLifecycleOwner) { newDestination ->
            if (newDestination == null) return@observe
            
            // Determina la dirección de la animación
            val (enterAnim, exitAnim) = getAnimations(currentDestination, newDestination)
            
            val fragment = when (newDestination) {
                PokemonDetallesViewModel.NavDestination.INFO -> 
                    PokemonInfoFragment.newInstance(pokemonId)
                PokemonDetallesViewModel.NavDestination.STATS ->
                    PokemonStatsFragment.newInstance(pokemonId)
                PokemonDetallesViewModel.NavDestination.SPRITES ->
                    PokemonSpritesFragment.newInstance(pokemonId)
                PokemonDetallesViewModel.NavDestination.LOCATIONS ->
                    PokemonLocationsFragment.newInstance(pokemonId)
            }

            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    enterAnim,
                    exitAnim,
                    R.anim.slide_in_left,  // Para cuando se pulse "back"
                    R.anim.slide_out_right  // Para cuando se pulse "back"
                )
                .replace(
                    binding.pokemonDetallesFragmentContainer.id,
                    fragment
                )
                .commit()
            
            // Actualizar el destino actual después de la transición
            currentDestination = newDestination
        }
    }
    
    /**
     * Determina las animaciones adecuadas basadas en la dirección de navegación.
     * @return Par de (animación de entrada, animación de salida)
     */
    private fun getAnimations(
        from: PokemonDetallesViewModel.NavDestination?, 
        to: PokemonDetallesViewModel.NavDestination
    ): Pair<Int, Int> {
        if (from == null) {
            // Primera carga, usar animación predeterminada
            return Pair(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Orden de pestañas: INFO(0) -> STATS(1) -> SPRITES(2)
        val fromIndex = getDestinationIndex(from)
        val toIndex = getDestinationIndex(to)

        return if (fromIndex < toIndex) {
            // Movimiento hacia la derecha (siguiente)
            Pair(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            // Movimiento hacia la izquierda (anterior)
            Pair(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    /**
     * Obtiene el índice numérico para un destino de navegación.
     * Esto nos permite determinar la dirección de navegación.
     */
    private fun getDestinationIndex(destination: PokemonDetallesViewModel.NavDestination): Int {
        return when (destination) {
            PokemonDetallesViewModel.NavDestination.INFO -> 0
            PokemonDetallesViewModel.NavDestination.STATS -> 1
            PokemonDetallesViewModel.NavDestination.SPRITES -> 2
            PokemonDetallesViewModel.NavDestination.LOCATIONS -> 3
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pokemonId: Int) = PokemonDetallesMainFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
