package com.example.completepokemondex.ui.pokemonlist

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.completepokemondex.R

/**
 *  Fragmento para mostrar la lista
 */
class PokemonListFragment : Fragment() {
    companion object {
        fun newInstance() = PokemonListFragment()
    }

    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }
}