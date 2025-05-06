import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.ItemMoveSectionHeaderBinding
import com.example.completepokemondex.databinding.ItemPokemonMoveBinding
import com.example.completepokemondex.ui.pokemonMoves.PokemonMovesViewModel

class PokemonMoveSectionedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: List<PokemonMovesViewModel.MoveSectionItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PokemonMovesViewModel.MoveSectionItem.Header -> 0
            is PokemonMovesViewModel.MoveSectionItem.MoveItem -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = ItemMoveSectionHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemPokemonMoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MoveViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is PokemonMovesViewModel.MoveSectionItem.Header -> (holder as HeaderViewHolder).bind(item)
            is PokemonMovesViewModel.MoveSectionItem.MoveItem -> (holder as MoveViewHolder).bind(item)
        }
    }

    inner class HeaderViewHolder(private val binding: ItemMoveSectionHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: PokemonMovesViewModel.MoveSectionItem.Header) {
            binding.sectionTitle.text = header.title
        }
    }

    inner class MoveViewHolder(private val binding: ItemPokemonMoveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PokemonMovesViewModel.MoveSectionItem.MoveItem) {
            val move = item.move
            val nameEs = move.names?.firstOrNull { it?.language?.name == "es" }?.name
            binding.moveName.text = nameEs ?: move.name?.replace("-", " ")?.replaceFirstChar { it.uppercase() } ?: "-"
            val typeName = move.type?.name?.replaceFirstChar { it.uppercase() } ?: "-"
            binding.moveTypeChip.text = typeName
            val typeColorRes = getTypeColorResId(move.type?.name)
            binding.moveTypeChip.chipBackgroundColor = ContextCompat.getColorStateList(binding.root.context, typeColorRes)
            binding.movePower.text = move.power?.toString() ?: "-"
            binding.moveAccuracy.text = if (move.accuracy != null) "${move.accuracy}%" else "-"
            binding.movePp.text = "PP: ${move.pp ?: "-"}"
            val descEs = move.flavor_text_entries?.firstOrNull { it?.language?.name == "es" }?.flavor_text
            binding.moveDescription.text = descEs?.replace("\\n", " ")?.replace("\\f", " ") ?: ""
            val learnText = when (item.learnMethod) {
                "level-up" -> if (item.level != null && item.level > 0) "Nivel ${item.level}" else "Nivel"
                "machine" -> "MT/MO"
                "tutor" -> "Tutor"
                "egg" -> "Huevo"
                else -> item.learnMethod?.replaceFirstChar { it.uppercase() } ?: "-"
            }
            binding.moveLearnMethod.text = learnText
        }
    }

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