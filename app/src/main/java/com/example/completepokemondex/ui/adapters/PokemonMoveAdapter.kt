package com.example.completepokemondex.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonMoveDomain
import com.example.completepokemondex.databinding.ItemPokemonMoveBinding

/**
 * Adaptador para mostrar la lista de movimientos de un Pokémon.
 * Utiliza DiffUtil para optimizar actualizaciones.
 */
class PokemonMoveAdapter(private val viewModel: com.example.completepokemondex.ui.pokemonMoves.PokemonMovesViewModel) : ListAdapter<PokemonMoveDomain, PokemonMoveAdapter.MoveViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val binding = ItemPokemonMoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder para un movimiento de Pokémon.
     */
    inner class MoveViewHolder(private val binding: ItemPokemonMoveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(move: PokemonMoveDomain) {
            // Nombre del movimiento (en español si existe, si no, el nombre base)
            val nameEs = move.names?.firstOrNull { it?.language?.name == "es" }?.name
            binding.moveName.text = nameEs ?: move.name?.replace("-", " ")?.replaceFirstChar { it.uppercase() } ?: "-"

            // Tipo del movimiento
            val typeName = move.type?.name?.replaceFirstChar { it.uppercase() } ?: "-"
            binding.moveTypeChip.text = typeName
            val typeColorRes = getTypeColorResId(move.type?.name)
            binding.moveTypeChip.chipBackgroundColor = ContextCompat.getColorStateList(binding.root.context, typeColorRes)

            // Poder
            binding.movePower.text = move.power?.toString() ?: "-"
            // Precisión
            binding.moveAccuracy.text = if (move.accuracy != null) "${move.accuracy}%" else "-"
            // PP
            binding.movePp.text = "PP: ${move.pp ?: "-"}"
            // Descripción (en español si existe)
            val descEs = move.flavor_text_entries?.firstOrNull { it?.language?.name == "es" }?.flavor_text
            binding.moveDescription.text = descEs?.replace("\\n", " ")?.replace("\\f", " ") ?: ""
            // Método de aprendizaje
            val (learnMethod, level) = viewModel.getLearnMethodForMove(move.id ?: -1)
            val learnText = when (learnMethod) {
                "level-up" -> if (level != null && level > 0) "Nivel $level" else "Nivel"
                "machine" -> "MT/MO"
                "tutor" -> "Tutor"
                "egg" -> "Huevo"
                else -> learnMethod?.replaceFirstChar { it.uppercase() } ?: "-"
            }
            binding.moveLearnMethod.text = learnText
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PokemonMoveDomain>() {
        override fun areItemsTheSame(oldItem: PokemonMoveDomain, newItem: PokemonMoveDomain): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: PokemonMoveDomain, newItem: PokemonMoveDomain): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Devuelve el color de fondo correspondiente al tipo de movimiento.
     */
    private fun getTypeColorResId(type: String?): Int {
        return when (type?.lowercase()) {
            "normal" -> R.color.type_normal
            "fire" -> R.color.type_fire
            "water" -> R.color.type_water
            "electric" -> R.color.type_electric
            "grass" -> R.color.type_grass
            "ice" -> R.color.type_ice
            "fighting" -> R.color.type_fighting
            "poison" -> R.color.type_poison
            "ground" -> R.color.type_ground
            "flying" -> R.color.type_flying
            "psychic" -> R.color.type_psychic
            "bug" -> R.color.type_bug
            "rock" -> R.color.type_rock
            "ghost" -> R.color.type_ghost
            "dragon" -> R.color.type_dragon
            "dark" -> R.color.type_dark
            "steel" -> R.color.type_steel
            "fairy" -> R.color.type_fairy
            else -> R.color.type_normal
        }
    }
}
