package com.example.completepokemondex.ui.spritesPokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.repository.PokemonRepository
import com.example.completepokemondex.databinding.FragmentPokemonSpritesBinding
import com.example.completepokemondex.data.domain.model.PokemonDetailsDomain
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        loadSprites()
    }

    private fun loadSprites() {
        val context = requireContext()
        binding.root.removeAllViews()

        val grid = GridLayout(context).apply {
            columnCount = 2
            rowCount = GridLayout.UNDEFINED
            setPadding(24, 24, 24, 24)
        }
        binding.root.addView(grid)

        val database = PokedexDatabase.getDatabase(context)
        val repo = PokemonRepository(
            database.pokemonDao(),
            database.pokemonDetailsDao(),
            database.pokemonSpeciesDao(),
            database.abilityDao(),
            PokemonRemoteDataSource(),
            database.evolutionChainDao()
        )

        lifecycleScope.launch {
            repo.getPokemonDetailsById(pokemonId).collect { result ->
                val pokemon = (result as? com.example.completepokemondex.data.remote.api.Resource.Success)?.data
                if (pokemon?.sprites != null) {
                    val spriteList = getAllSprites(pokemon.sprites)
                    if (spriteList.isEmpty()) {
                        val tv = TextView(context).apply {
                            text = "No hay sprites disponibles."
                            gravity = Gravity.CENTER
                            textSize = 18f
                        }
                        binding.root.removeAllViews()
                        binding.root.addView(tv)
                    } else {
                        spriteList.forEachIndexed { idx, (label, url) ->
                            val itemLayout = LinearLayout(context).apply {
                                orientation = LinearLayout.VERTICAL
                                gravity = Gravity.CENTER_HORIZONTAL
                                setPadding(12, 18, 12, 18)
                            }
                            val image = ImageView(context).apply {
                                layoutParams = LinearLayout.LayoutParams(180, 180)
                                scaleType = ImageView.ScaleType.FIT_CENTER
                            }
                            Glide.with(context).load(url).into(image)
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
                }
            }
        }
    }

    private fun getAllSprites(sprites: PokemonDetailsDomain.Sprites): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        // Sprites principales
        fun add(label: String, value: Any?) {
            val url = value as? String
            if (!url.isNullOrBlank()) result.add(label to url)
        }
        add("front_default", sprites.front_default)
        add("front_shiny", sprites.front_shiny)
        add("front_female", sprites.front_female)
        add("front_shiny_female", sprites.front_shiny_female)
        add("back_default", sprites.back_default)
        add("back_shiny", sprites.back_shiny)
        add("back_female", sprites.back_female)
        add("back_shiny_female", sprites.back_shiny_female)

        // Sprites "other"
        sprites.other?.let { other ->
            add("dream_world/front_default", other.dream_world?.front_default)
            add("home/front_default", other.home?.front_default)
            add("home/front_shiny", other.home?.front_shiny)
            add("official-artwork/front_default", other.`official-artwork`?.front_default)
            add("official-artwork/front_shiny", other.`official-artwork`?.front_shiny)
            add("showdown/back_default", other.showdown?.back_default)
            add("showdown/back_shiny", other.showdown?.back_shiny)
            add("showdown/front_default", other.showdown?.front_default)
            add("showdown/front_shiny", other.showdown?.front_shiny)
        }

        // Sprites "versions"
        sprites.versions?.let { v ->
            // Generation I
            v.`generation-i`?.let { gen ->
                gen.`red-blue`?.let {
                    add("gen1/red-blue/back_default", it.back_default)
                    add("gen1/red-blue/back_gray", it.back_gray)
                    add("gen1/red-blue/back_transparent", it.back_transparent)
                    add("gen1/red-blue/front_default", it.front_default)
                    add("gen1/red-blue/front_gray", it.front_gray)
                    add("gen1/red-blue/front_transparent", it.front_transparent)
                }
                gen.`yellow`?.let {
                    add("gen1/yellow/back_default", it.back_default)
                    add("gen1/yellow/back_gray", it.back_gray)
                    add("gen1/yellow/back_transparent", it.back_transparent)
                    add("gen1/yellow/front_default", it.front_default)
                    add("gen1/yellow/front_gray", it.front_gray)
                    add("gen1/yellow/front_transparent", it.front_transparent)
                }
            }
            // Generation II
            v.`generation-ii`?.let { gen ->
                gen.crystal?.let {
                    add("gen2/crystal/back_default", it.back_default)
                    add("gen2/crystal/back_shiny", it.back_shiny)
                    add("gen2/crystal/back_shiny_transparent", it.back_shiny_transparent)
                    add("gen2/crystal/back_transparent", it.back_transparent)
                    add("gen2/crystal/front_default", it.front_default)
                    add("gen2/crystal/front_shiny", it.front_shiny)
                    add("gen2/crystal/front_shiny_transparent", it.front_shiny_transparent)
                    add("gen2/crystal/front_transparent", it.front_transparent)
                }
                gen.gold?.let {
                    add("gen2/gold/back_default", it.back_default)
                    add("gen2/gold/back_shiny", it.back_shiny)
                    add("gen2/gold/front_default", it.front_default)
                    add("gen2/gold/front_shiny", it.front_shiny)
                    add("gen2/gold/front_transparent", it.front_transparent)
                }
                gen.silver?.let {
                    add("gen2/silver/back_default", it.back_default)
                    add("gen2/silver/back_shiny", it.back_shiny)
                    add("gen2/silver/front_default", it.front_default)
                    add("gen2/silver/front_shiny", it.front_shiny)
                    add("gen2/silver/front_transparent", it.front_transparent)
                }
            }
            // Generation III
            v.`generation-iii`?.let { gen ->
                gen.emerald?.let {
                    add("gen3/emerald/front_default", it.front_default)
                    add("gen3/emerald/front_shiny", it.front_shiny)
                }
                gen.`firered-leafgreen`?.let {
                    add("gen3/firered-leafgreen/back_default", it.back_default)
                    add("gen3/firered-leafgreen/back_shiny", it.back_shiny)
                    add("gen3/firered-leafgreen/front_default", it.front_default)
                    add("gen3/firered-leafgreen/front_shiny", it.front_shiny)
                }
                gen.`ruby-sapphire`?.let {
                    add("gen3/ruby-sapphire/back_default", it.back_default)
                    add("gen3/ruby-sapphire/back_shiny", it.back_shiny)
                    add("gen3/ruby-sapphire/front_default", it.front_default)
                    add("gen3/ruby-sapphire/front_shiny", it.front_shiny)
                }
            }
            // Generation IV
            v.`generation-iv`?.let { gen ->
                gen.`diamond-pearl`?.let {
                    add("gen4/diamond-pearl/back_default", it.back_default)
                    add("gen4/diamond-pearl/back_shiny", it.back_shiny)
                    add("gen4/diamond-pearl/front_default", it.front_default)
                    add("gen4/diamond-pearl/front_shiny", it.front_shiny)
                }
                gen.`heartgold-soulsilver`?.let {
                    add("gen4/heartgold-soulsilver/back_default", it.back_default)
                    add("gen4/heartgold-soulsilver/back_shiny", it.back_shiny)
                    add("gen4/heartgold-soulsilver/front_default", it.front_default)
                    add("gen4/heartgold-soulsilver/front_shiny", it.front_shiny)
                }
                gen.`platinum`?.let {
                    add("gen4/platinum/back_default", it.back_default)
                    add("gen4/platinum/back_shiny", it.back_shiny)
                    add("gen4/platinum/front_default", it.front_default)
                    add("gen4/platinum/front_shiny", it.front_shiny)
                }
            }
            // Generation V
            v.`generation-v`?.`black-white`?.let { bw ->
                add("gen5/black-white/back_default", bw.back_default)
                add("gen5/black-white/back_shiny", bw.back_shiny)
                add("gen5/black-white/front_default", bw.front_default)
                add("gen5/black-white/front_shiny", bw.front_shiny)
                bw.animated?.let { anim ->
                    add("gen5/black-white/animated/back_default", anim.back_default)
                    add("gen5/black-white/animated/back_shiny", anim.back_shiny)
                    add("gen5/black-white/animated/front_default", anim.front_default)
                    add("gen5/black-white/animated/front_shiny", anim.front_shiny)
                }
            }
            // Generation VI
            v.`generation-vi`?.`omegaruby-alphasapphire`?.let {
                add("gen6/omegaruby-alphasapphire/front_default", it.front_default)
                add("gen6/omegaruby-alphasapphire/front_shiny", it.front_shiny)
            }
            v.`generation-vi`?.`x-y`?.let {
                add("gen6/x-y/front_default", it.front_default)
                add("gen6/x-y/front_shiny", it.front_shiny)
            }
            // Generation VII
            v.`generation-vii`?.icons?.let {
                add("gen7/icons/front_default", it.front_default)
            }
            v.`generation-vii`?.`ultra-sun-ultra-moon`?.let {
                add("gen7/ultra-sun-ultra-moon/front_default", it.front_default)
                add("gen7/ultra-sun-ultra-moon/front_shiny", it.front_shiny)
            }
            // Generation VIII
            v.`generation-viii`?.icons?.let {
                add("gen8/icons/front_default", it.front_default)
            }
        }
        return result
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
