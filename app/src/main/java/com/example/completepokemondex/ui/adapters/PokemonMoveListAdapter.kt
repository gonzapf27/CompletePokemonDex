package com.example.completepokemondex.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.completepokemondex.R
import com.example.completepokemondex.data.domain.model.PokemonMoveDomain
import com.example.completepokemondex.util.PokemonTypeUtil
import com.google.android.material.chip.Chip
import javax.inject.Inject

/**
 * Adaptador para mostrar la lista de movimientos de un Pokémon en un RecyclerView
 * Utiliza inyección de dependencias
 */
class PokemonMoveListAdapter @Inject constructor() :
    ListAdapter<PokemonMoveListAdapter.PokemonMoveItem, PokemonMoveListAdapter.MoveViewHolder>(MoveDiffCallback) {

    private var onMoveClicked: ((PokemonMoveDomain) -> Unit)? = null

    fun setOnMoveClickListener(listener: (PokemonMoveDomain) -> Unit) {
        onMoveClicked = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon_move, parent, false)
        return MoveViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        val moveItem = getItem(position)
        holder.bind(moveItem, onMoveClicked)
    }

    class MoveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.move_name)
        private val typeChip: Chip = itemView.findViewById(R.id.move_type_chip)
        private val powerTextView: TextView = itemView.findViewById(R.id.move_power)
        private val accuracyTextView: TextView = itemView.findViewById(R.id.move_accuracy)
        private val ppTextView: TextView = itemView.findViewById(R.id.move_pp)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.move_description)
        private val learnMethodTextView: TextView = itemView.findViewById(R.id.move_learn_method)

        fun bind(
            moveItem: PokemonMoveItem,
            onMoveClicked: ((PokemonMoveDomain) -> Unit)?
        ) {
            val move = moveItem.move
            val context = itemView.context

            // Configurar nombre del movimiento con primera letra en mayúscula
            nameTextView.text = move.name?.replaceFirstChar { it.uppercase() } ?: "???"

            // Configurar chip con tipo del movimiento
            move.type?.name?.let { typeName ->
                val pokemonType = PokemonTypeUtil.getTypeByName(typeName)
                typeChip.text = if (pokemonType.stringRes != 0) {
                    context.getString(pokemonType.stringRes)
                } else {
                    typeName.replaceFirstChar { it.uppercase() }
                }
                
                val typeColor = ContextCompat.getColor(context, pokemonType.colorRes)
                typeChip.chipBackgroundColor = ColorStateList.valueOf(typeColor)
            } ?: run {
                typeChip.text = "???"
                typeChip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.type_normal)
                )
            }

            // Configurar poder
            powerTextView.text = move.power?.toString() ?: "--"

            // Configurar precisión
            accuracyTextView.text = move.accuracy?.let { "$it%" } ?: "--"

            // Configurar PP
            ppTextView.text = move.pp?.let { "PP: $it" } ?: "PP: ??"

            // Configurar descripción
            val description = move.effect_entries?.firstOrNull { 
                it?.language?.name == "es" || it?.language?.name == "en" 
            }?.short_effect ?: "Sin descripción disponible"
            descriptionTextView.text = description

            // Configurar método de aprendizaje
            learnMethodTextView.text = moveItem.learnMethod

            // Configurar click listener
            itemView.setOnClickListener {
                onMoveClicked?.invoke(move)
            }
        }
    }

    object MoveDiffCallback : DiffUtil.ItemCallback<PokemonMoveItem>() {
        override fun areItemsTheSame(oldItem: PokemonMoveItem, newItem: PokemonMoveItem): Boolean {
            return oldItem.move.id == newItem.move.id && 
                   oldItem.learnMethod == newItem.learnMethod
        }

        override fun areContentsTheSame(oldItem: PokemonMoveItem, newItem: PokemonMoveItem): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Clase que encapsula un movimiento de Pokémon y su método de aprendizaje
     */
    data class PokemonMoveItem(
        val move: PokemonMoveDomain,
        val learnMethod: String
    )
}
