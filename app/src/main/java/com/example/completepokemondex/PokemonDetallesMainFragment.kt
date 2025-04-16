package com.example.completepokemondex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.databinding.FragmentPokemonDetallesMainBinding

class PokemonDetallesMainFragment : Fragment() {
    private var _binding: FragmentPokemonDetallesMainBinding? = null
    private val binding get() = _binding!!

    private var pokemonId: Int = 0

    private val viewModel: PokemonDetallesMainViewModel by viewModels()

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
                    viewModel.navigateTo(PokemonDetallesMainViewModel.NavDestination.INFO)
                    true
                }
                R.id.nav_stats -> {
                    viewModel.navigateTo(PokemonDetallesMainViewModel.NavDestination.STATS)
                    true
                }
                else -> false
            }
        }

        viewModel.navState.observe(viewLifecycleOwner) { nav ->
            when (nav) {
                PokemonDetallesMainViewModel.NavDestination.INFO -> {
                    childFragmentManager.beginTransaction()
                        .replace(
                            binding.pokemonDetallesFragmentContainer.id,
                            PokemonInfoFragment.newInstance(pokemonId)
                        )
                        .commit()
                }
                PokemonDetallesMainViewModel.NavDestination.STATS -> {
                    childFragmentManager.beginTransaction()
                        .replace(
                            binding.pokemonDetallesFragmentContainer.id,
                            PokemonStatsFragment.newInstance(pokemonId)
                        )
                        .commit()
                }
                null -> {}
            }
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
