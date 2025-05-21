package com.example.completepokemondex.ui.pokemonSeleccionado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.completepokemondex.util.NetworkStatusLiveData
import com.example.completepokemondex.ui.statsPokemon.PokemonStatsFragment
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonDetallesMainBinding
import com.example.completepokemondex.ui.infoPokemon.PokemonInfoFragment
import com.example.completepokemondex.ui.pokemonLocations.PokemonLocationsFragment
import com.example.completepokemondex.ui.pokemonMoves.PokemonMovesFragment
import com.example.completepokemondex.ui.spritesPokemon.PokemonSpritesFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento principal que contiene la navegación inferior y muestra los diferentes fragmentos
 * de información, estadísticas, sprites, localizaciones y movimientos de un Pokémon.
 */
@AndroidEntryPoint
class PokemonDetallesMainFragment : Fragment() {
    private var _binding: FragmentPokemonDetallesMainBinding? = null
    private val binding get() = _binding!!

    private var pokemonId: Int = 0
    private var currentDestination: PokemonDetallesViewModel.NavDestination? = null

    private val viewModel: PokemonDetallesViewModel by viewModels()

    private lateinit var networkStatusLiveData: NetworkStatusLiveData
    private var noInternetView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonId = arguments?.getInt("pokemon_id") ?: 0
    }

    /**
     * Inicializa el binding y la vista del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetallesMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la navegación y observa los cambios de destino para mostrar el fragmento correspondiente.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.setInitialPokemonId(pokemonId)
        }

        // --- INICIO: Observador de red ---
        networkStatusLiveData = NetworkStatusLiveData(requireContext().applicationContext)
        networkStatusLiveData.observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                showNoInternetView()
            } else {
                hideNoInternetView()
            }
        })
        // --- FIN: Observador de red ---

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
                R.id.nav_moves ->{
                    viewModel.navigateTo(PokemonDetallesViewModel.NavDestination.MOVES)
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
                PokemonDetallesViewModel.NavDestination.MOVES ->
                    PokemonMovesFragment.newInstance(pokemonId)
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
            PokemonDetallesViewModel.NavDestination.MOVES -> 4
        }
    }

    /**
     * Muestra la vista de "sin internet" y oculta el contenido principal.
     */
    private fun showNoInternetView() {
        if (noInternetView == null) {
            val container = binding.root as ViewGroup
            noInternetView = LayoutInflater.from(requireContext()).inflate(R.layout.view_no_internet, container, false)
            container.addView(noInternetView)
        }
        noInternetView?.visibility = View.VISIBLE
        binding.pokemonDetallesFragmentContainer.visibility = View.GONE
        binding.pokemonDetallesBottomNav.visibility = View.GONE
    }

    /**
     * Oculta la vista de "sin internet" y restaura el contenido principal.
     */
    private fun hideNoInternetView() {
        noInternetView?.visibility = View.GONE
        binding.pokemonDetallesFragmentContainer.visibility = View.VISIBLE
        binding.pokemonDetallesBottomNav.visibility = View.VISIBLE
    }

    /**
     * Libera el binding al destruir la vista.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Crea una nueva instancia del fragmento principal con el ID del Pokémon.
         */
        fun newInstance(pokemonId: Int) = PokemonDetallesMainFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
