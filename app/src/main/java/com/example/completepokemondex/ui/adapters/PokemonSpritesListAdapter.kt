package com.example.completepokemondex.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.ItemPokemonSpriteBinding

data class PokemonSpriteItem(
    val label: String,
    val url: String?
)

class PokemonSpritesListAdapter : ListAdapter<PokemonSpriteItem, PokemonSpritesListAdapter.SpriteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpriteViewHolder {
        val binding = ItemPokemonSpriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpriteViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.scaleX = 0.8f
        holder.itemView.scaleY = 0.8f
        holder.itemView.alpha = 0f
        // Animación en cascada: cada tarjeta espera un poco más según su posición
        val delay = (position * 100L).coerceAtMost(800L) // máximo 800ms de delay
        Handler(Looper.getMainLooper()).postDelayed({
            holder.itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setInterpolator(OvershootInterpolator())
                .start()
        }, delay)
    }

    class SpriteViewHolder(private val binding: ItemPokemonSpriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PokemonSpriteItem) {
            binding.spriteLabel.text = item.label
            Glide.with(binding.root.context)
                .load(item.url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.spriteImage)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PokemonSpriteItem>() {
        override fun areItemsTheSame(oldItem: PokemonSpriteItem, newItem: PokemonSpriteItem): Boolean =
            oldItem.label == newItem.label

        override fun areContentsTheSame(oldItem: PokemonSpriteItem, newItem: PokemonSpriteItem): Boolean =
            oldItem == newItem
    }
}
