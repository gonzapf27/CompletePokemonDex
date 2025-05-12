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
import java.util.Locale

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
            // Nombre del movimiento según el locale del dispositivo
            val locale = Locale.getDefault().language
            val name = if (locale == "es") {
                move.names?.firstOrNull { it?.language?.name == "es" }?.name
                    ?: move.names?.firstOrNull { it?.language?.name == "en" }?.name
                    ?: move.name?.replace("-", " ")?.replaceFirstChar { it.uppercase() }
            } else {
                move.names?.firstOrNull { it?.language?.name == "en" }?.name
                    ?: move.name?.replace("-", " ")?.replaceFirstChar { it.uppercase() }
            }
            binding.moveName.text = name ?: "-"

            // Tipo del movimiento (internacionalizado)
            val typeUtil = com.example.completepokemondex.util.PokemonTypeUtil.getTypeByName(move.type?.name ?: "")
            binding.moveTypeChip.text = if (typeUtil.stringRes != 0) binding.root.context.getString(typeUtil.stringRes) else move.type?.name?.replaceFirstChar { it.uppercase() } ?: "-"
            val typeColorRes = com.example.completepokemondex.util.PokemonTypeUtil.getTypeColorResId(move.type?.name)
            binding.moveTypeChip.chipBackgroundColor = ContextCompat.getColorStateList(binding.root.context, typeColorRes)

            // Poder
            binding.movePower.text = move.power?.toString() ?: "-"
            // Precisión
            binding.moveAccuracy.text = if (move.accuracy != null) "${move.accuracy}%" else "-"
            // PP
            binding.movePp.text = "PP: ${move.pp ?: "-"}"
            // Descripción (flavor_text_entries según locale)
            val flavorText = if (locale == "es") {
                move.flavor_text_entries
                    ?.firstOrNull { it?.language?.name == "es" }?.flavor_text
                    ?: move.flavor_text_entries?.firstOrNull { it?.language?.name == "en" }?.flavor_text
            } else {
                move.flavor_text_entries
                    ?.firstOrNull { it?.language?.name == "en" }?.flavor_text
            }
            binding.moveDescription.text = flavorText?.replace("\\n", " ")?.replace("\\f", " ") ?: ""
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
}
