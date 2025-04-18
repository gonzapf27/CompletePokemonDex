package com.example.completepokemondex.ui.spritesPokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.databinding.FragmentPokemonSpritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonSpritesFragment : Fragment() {
    private var _binding: FragmentPokemonSpritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonSpritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonSpritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pokemonId: Int) = PokemonSpritesFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
