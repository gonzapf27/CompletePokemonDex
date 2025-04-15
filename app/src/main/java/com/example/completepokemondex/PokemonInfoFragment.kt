package com.example.completepokemondex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.databinding.FragmentPokemonInfoBinding
import com.google.android.material.chip.Chip

class PokemonInfoFragment : Fragment() {
    private var _binding: FragmentPokemonInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetallesViewModel by viewModels {
        PokemonDetallesViewModel.Factory(PokedexDatabase.getDatabase(requireContext()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("pokemon_id")?.let { viewModel.setPokemonId(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var currentPokemonId: Int? = null
        var currentIsFavorite: Boolean = false
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.loadingIndicator.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            binding.pokemonDetailsId.text = state.id
            binding.pokemonDetailsNombre.text = state.nombre
            binding.pokemonDetailsHeight.text = state.height
            binding.pokemonDetailsWeight.text = state.weight
            // Imagen
            state.imageUrl?.let {
                binding.pokemonImage.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                com.bumptech.glide.Glide.with(requireContext())
                    .load(it)
                    .into(binding.pokemonImage)
            }
            // Cambiar icono de favorito según el estado
            if (state.isFavorite) {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_filled_red)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_outline)
            }
            // Animación al cambiar favorito
            val context = binding.btnFavorite.context
            val anim = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.favorite_pop)
            binding.btnFavorite.startAnimation(anim)
            // Guardar id y estado actual para el click
            currentPokemonId = state.id.toIntOrNull()
            currentIsFavorite = state.isFavorite
            // Fondo gradiente
            val gradientBg = binding.pokemonFragmentGradientBg
            val mainTypeColor = state.types.firstOrNull()?.color ?: android.graphics.Color.LTGRAY
            val isDark = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
            val endColor = if (isDark) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            val gradientDrawable = android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(mainTypeColor, endColor)
            )
            gradientDrawable.cornerRadius = 0f
            gradientBg.background = gradientDrawable
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
            // Descripción
            binding.pokemonDetailsDescription.text = state.descripcion
        }
        // Listener para marcar/desmarcar favorito
        binding.btnFavorite.setOnClickListener {
            val id = currentPokemonId
            if (id != null) {
                viewModel.toggleFavorite(id, !currentIsFavorite)
                // Opcional: puedes mostrar un feedback visual aquí
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pokemonId: Int) = PokemonInfoFragment().apply {
            arguments = Bundle().apply { putInt("pokemon_id", pokemonId) }
        }
    }
}
