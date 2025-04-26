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
import android.widget.LinearLayout
import com.example.completepokemondex.ui.pokemonMoves.MovesSectionUi

/**
 * Adaptador para mostrar la lista de movimientos de un Pokémon en un RecyclerView
 * Utiliza inyección de dependencias
 */
class PokemonMoveListAdapter @Inject constructor() :
    ListAdapter<PokemonMoveListAdapter.ListItem, RecyclerView.ViewHolder>(MoveDiffCallback) {

    private var onMoveClicked: ((PokemonMoveDomain) -> Unit)? = null

    fun setOnMoveClickListener(listener: (PokemonMoveDomain) -> Unit) {
        onMoveClicked = listener
    }

    companion object {
        private const val VIEW_TYPE_SECTION_HEADER = 0
        private const val VIEW_TYPE_MOVE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.SectionHeader -> VIEW_TYPE_SECTION_HEADER
            is ListItem.MoveItem -> VIEW_TYPE_MOVE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SECTION_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_move_section_header, parent, false)
                SectionHeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_pokemon_move, parent, false)
                MoveViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.SectionHeader -> (holder as SectionHeaderViewHolder).bind(item)
            is ListItem.MoveItem -> (holder as MoveViewHolder).bind(item, onMoveClicked)
        }
    }

    class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sectionTitle: TextView = itemView.findViewById(R.id.section_title)
        fun bind(item: ListItem.SectionHeader) {
            sectionTitle.text = item.title
        }
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
            moveItem: ListItem.MoveItem,
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

    object MoveDiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return when {
                oldItem is ListItem.SectionHeader && newItem is ListItem.SectionHeader ->
                    oldItem.title == newItem.title
                oldItem is ListItem.MoveItem && newItem is ListItem.MoveItem ->
                    oldItem.move.id == newItem.move.id && oldItem.learnMethod == newItem.learnMethod
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class ListItem {
        data class SectionHeader(val title: String) : ListItem()
        data class MoveItem(val move: PokemonMoveDomain, val learnMethod: String) : ListItem()
    }
}
