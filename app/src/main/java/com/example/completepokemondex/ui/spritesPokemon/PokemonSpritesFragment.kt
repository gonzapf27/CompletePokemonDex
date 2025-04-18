package com.example.completepokemondex.ui.spritesPokemon

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.FragmentPokemonSpritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonSpritesFragment : Fragment() {
    private var _binding: FragmentPokemonSpritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonSpritesViewModel by viewModels()
    private val pokemonId: Int by lazy { arguments?.getInt("pokemon_id") ?: 0 }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonSpritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar datos en el ViewModel usando el pokemonId
        viewModel.loadPokemonSprites(pokemonId)

        // Observar cambios en el estado de la UI
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.spritesScrollView.visibility = View.GONE

                Glide.with(this)
                    .asGif()
                    .load(R.drawable.loading_pokeball)
                    .into(binding.loadingIndicator)
                return@observe
            } else {
                binding.loadingIndicator.visibility = View.GONE
                binding.spritesScrollView.visibility = View.VISIBLE

                // Configurar el gradiente según los tipos
                val gradientBg = binding.spriteFragmentGradientBg
                val typeColors = state.types.take(2).map {
                    ContextCompat.getColor(requireContext(), it.color)
                }
                val gradientColors = when {
                    typeColors.isEmpty() -> intArrayOf(Color.LTGRAY, Color.LTGRAY)
                    typeColors.size == 1 -> intArrayOf(typeColors[0], typeColors[0])
                    else -> typeColors.toIntArray()
                }
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, // vertical
                    gradientColors
                )
                gradientDrawable.cornerRadius = 0f
                gradientBg.background = gradientDrawable
            }

            // Actualizar la cuadrícula de sprites
            displaySprites(state.sprites)
        }
    }

    private fun displaySprites(sprites: List<Pair<String, String>>) {
        val context = requireContext()
        val container = binding.spritesContainer
        container.removeAllViews()

        if (sprites.isEmpty()) {
            val tv = TextView(context).apply {
                text = "No hay sprites disponibles"
                gravity = Gravity.CENTER
                textSize = 18f
            }
            container.addView(tv)
            return
        }

        val grid = GridLayout(context).apply {
            columnCount = 2
            rowCount = GridLayout.UNDEFINED
            setPadding(24, 24, 24, 24)
        }
        container.addView(grid)

        sprites.forEachIndexed { idx, (label, url) ->
            val itemLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                setPadding(12, 18, 12, 18)
            }
            val image = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(180, 180)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)

            val text = TextView(context).apply {
                text = label.replace('_', ' ').replace('-', ' ').replace('/', '\n')
                gravity = Gravity.CENTER
                textSize = 13f
                setPadding(0, 8, 0, 0)
            }
            itemLayout.addView(image)
            itemLayout.addView(text)
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(idx % 2, 1f)
                setMargins(8, 8, 8, 8)
            }
            grid.addView(itemLayout, params)
        }
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
