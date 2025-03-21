package com.example.completepokemondex.ui.pokemondetail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.completepokemondex.R

/**
 * Fragmento para mostrar detalles de un Pokémon
 */
class PokemonDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PokemonDetailFragment()
    }

    private val viewModel: PokemonDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pokemon_detail, container, false)
    }
}