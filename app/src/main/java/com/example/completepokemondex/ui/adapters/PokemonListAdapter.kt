package com.example.completepokemondex.ui.adapters

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonDomain
import com.example.completepokemondex.util.PokemonTypeUtil

/**
 * Adaptador para mostrar la lista de Pokémon en un RecyclerView
 */
class PokemonListAdapter(
    private val onItemClicked: (PokemonDomain) -> Unit,
    private val onFavoriteClicked: (PokemonDomain) -> Unit, // Nuevo callback
    private var pokemonTypes: Map<Int, List<String>> = emptyMap()
) : 
    ListAdapter<PokemonDomain, PokemonListAdapter.PokemonViewHolder>(PokemonDiffCallback) {

    fun updatePokemonTypes(newTypes: Map<Int, List<String>>) {
        pokemonTypes = newTypes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view, onItemClicked, onFavoriteClicked)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        val types = pokemonTypes[pokemon.id] ?: emptyList()
        holder.bind(pokemon, types)
    }

    class PokemonViewHolder(
        itemView: View,
        private val onItemClicked: (PokemonDomain) -> Unit,
        private val onFavoriteClicked: (PokemonDomain) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        private val idTextView: TextView = itemView.findViewById(R.id.pokemon_id)
        private val imageView: ImageView = itemView.findViewById(R.id.pokemon_list_image)
        private val cardView: CardView = itemView as CardView
        private val favoriteButton: ImageView = itemView.findViewById(R.id.favorite_button)

        fun bind(pokemon: PokemonDomain, types: List<String>?) {
            nameTextView.text = pokemon.name
            idTextView.text = "#${pokemon.id.toString().padStart(3, '0')}"
            
            // Cargar la imagen del Pokémon si está disponible
            pokemon.imageUrl?.let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .apply(RequestOptions()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground))
                    .into(imageView)
            } ?: run {
                // Si no hay URL de imagen, mostrar un placeholder
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }

            // Fondo gradiente según tipos
            val context = itemView.context
            val colors = types?.map {
                val type = PokemonTypeUtil.getTypeByName(it)
                ContextCompat.getColor(context, type.colorRes)
            } ?: listOf(ContextCompat.getColor(context, R.color.type_normal))
            val gradientColors = if (colors.size == 1) intArrayOf(colors[0], colors[0]) else colors.toIntArray()
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                gradientColors
            )
            gradientDrawable.cornerRadius = cardView.radius
            cardView.background = gradientDrawable
            
            // Estado del icono de favorito
            if (pokemon.favorite) {
                favoriteButton.setImageResource(R.drawable.ic_star_filled)
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_outline)
            }
            favoriteButton.setOnClickListener {
                onFavoriteClicked(pokemon)
            }

            itemView.setOnClickListener {
                onItemClicked(pokemon)
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
