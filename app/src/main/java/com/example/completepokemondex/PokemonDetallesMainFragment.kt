package com.example.completepokemondex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.completepokemondex.databinding.FragmentPokemonDetallesMainBinding

class PokemonDetallesMainFragment : Fragment() {
    private var _binding: FragmentPokemonDetallesMainBinding? = null
    private val binding get() = _binding!!

    private var pokemonId: Int = 0

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
            childFragmentManager.beginTransaction()
                .replace(
                    binding.pokemonDetallesFragmentContainer.id,
                    PokemonInfoFragment.newInstance(pokemonId)
                )
                .commit()
        }
        binding.pokemonDetallesBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_info -> {
                    childFragmentManager.beginTransaction()
                        .replace(
                            binding.pokemonDetallesFragmentContainer.id,
                            PokemonInfoFragment.newInstance(pokemonId)
                        )
                        .commit()
                    true
                }
                R.id.nav_stats -> {
                    childFragmentManager.beginTransaction()
                        .replace(
                            binding.pokemonDetallesFragmentContainer.id,
                            PokemonStatsFragment.newInstance(pokemonId)
                        )
                        .commit()
                    true
                }
                else -> false
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
