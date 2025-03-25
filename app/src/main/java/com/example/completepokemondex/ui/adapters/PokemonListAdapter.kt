package com.example.completepokemondex.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.completepokemondex.R
import com.example.completepokemondex.domain.model.PokemonDomain

/**
 * Adaptador para mostrar la lista de Pokémon en un RecyclerView
 */
class PokemonListAdapter(private val onItemClicked: (PokemonDomain) -> Unit) : 
    ListAdapter<PokemonDomain, PokemonListAdapter.PokemonViewHolder>(PokemonDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PokemonViewHolder(
        itemView: View,
        private val onItemClicked: (PokemonDomain) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.pokemon_name)
        private val idTextView: TextView = itemView.findViewById(R.id.pokemon_id)
        private val imageView: ImageView = itemView.findViewById(R.id.pokemon_list_image)

        fun bind(pokemon: PokemonDomain) {
            nameTextView.text = pokemon.name
            idTextView.text = "#${pokemon.id}"
            
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
