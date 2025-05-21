package com.example.completepokemondex.ui.adapters

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.example.completepokemondex.databinding.ItemPokemonBinding
import com.example.completepokemondex.util.PokemonTypeUtil
import javax.inject.Inject

/**
 * Adaptador para mostrar la lista de Pokémon en un RecyclerView.
 * Permite manejar clics en el ítem y en el botón de favorito.
 */
class PokemonListAdapter @Inject constructor() : 
    ListAdapter<PokemonDomain, PokemonListAdapter.PokemonViewHolder>(PokemonDiffCallback) {

    private var onItemClicked: ((PokemonDomain) -> Unit)? = null
    private var onFavoriteClicked: ((PokemonDomain) -> Unit)? = null
    private var pokemonTypes: Map<Int, List<String>> = emptyMap()
    
    /**
     * Establece el listener para clics en el ítem.
     */
    fun setOnItemClickListener(listener: (PokemonDomain) -> Unit) {
        onItemClicked = listener
    }
    
    /**
     * Establece el listener para clics en el botón de favorito.
     */
    fun setOnFavoriteClickListener(listener: (PokemonDomain) -> Unit) {
        onFavoriteClicked = listener
    }

    /**
     * Actualiza el mapa de tipos de Pokémon para cada ítem.
     */
    fun updatePokemonTypes(newTypes: Map<Int, List<String>>) {
        pokemonTypes = newTypes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        val types = pokemonTypes[pokemon.id] ?: emptyList()
        holder.bind(pokemon, types, onItemClicked, onFavoriteClicked)
    }

    /**
     * ViewHolder para cada tarjeta de Pokémon usando ViewBinding.
     */
    class PokemonViewHolder(
        private val binding: ItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Vincula los datos del Pokémon a la vista y aplica el fondo según los tipos.
         */
        fun bind(
            pokemon: PokemonDomain, 
            types: List<String>?,
            onItemClicked: ((PokemonDomain) -> Unit)?,
            onFavoriteClicked: ((PokemonDomain) -> Unit)?
        ) {
            binding.pokemonName.text = pokemon.name
            binding.pokemonId.text = "#${pokemon.id.toString().padStart(3, '0')}"
            
            // Cargar la imagen del Pokémon si está disponible
            pokemon.imageUrl?.let { url ->
                Glide.with(binding.root.context)
                    .load(url)
                    .apply(RequestOptions()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground))
                    .into(binding.pokemonListImage)
            } ?: run {
                binding.pokemonListImage.setImageResource(R.drawable.ic_launcher_foreground)
            }

            // Fondo gradiente según tipos
            val context = binding.root.context
            val colors = types?.map {
                val type = PokemonTypeUtil.getTypeByName(it)
                ContextCompat.getColor(context, type.colorRes)
            } ?: listOf(ContextCompat.getColor(context, R.color.type_normal))
            val gradientColors = if (colors.size == 1) intArrayOf(colors[0], colors[0]) else colors.toIntArray()
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                gradientColors
            )
            gradientDrawable.cornerRadius = (binding.root as CardView).radius
            binding.root.background = gradientDrawable
            
            // Estado del icono de favorito
            if (pokemon.favorite) {
                binding.favoriteButton.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.favoriteButton.setImageResource(R.drawable.ic_star_outline)
            }
            binding.favoriteButton.setOnClickListener {
                onFavoriteClicked?.invoke(pokemon)
            }

            binding.root.setOnClickListener {
                onItemClicked?.invoke(pokemon)
            }
        }
    }

    object PokemonDiffCallback : DiffUtil.ItemCallback<PokemonDomain>() {
        override fun areItemsTheSame(oldItem: PokemonDomain, newItem: PokemonDomain): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokemonDomain, newItem: PokemonDomain): Boolean {
            return oldItem == newItem && oldItem.imageUrl == newItem.imageUrl
        }
    }
}
