import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.completepokemondex.R
import com.example.completepokemondex.databinding.ItemMoveSectionHeaderBinding
import com.example.completepokemondex.databinding.ItemPokemonMoveBinding
import com.example.completepokemondex.ui.pokemonMoves.PokemonMovesViewModel
import java.util.Locale

/**
 * Adaptador para mostrar movimientos de Pokémon agrupados por secciones (por ejemplo, método de aprendizaje).
 */
class PokemonMoveSectionedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Lista de ítems (cabeceras y movimientos)
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

    /**
     * ViewHolder para la cabecera de sección.
     */
    inner class HeaderViewHolder(private val binding: ItemMoveSectionHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: PokemonMovesViewModel.MoveSectionItem.Header) {
            binding.sectionTitle.text = header.title
        }
    }

    /**
     * ViewHolder para un movimiento de Pokémon.
     */
    inner class MoveViewHolder(private val binding: ItemPokemonMoveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PokemonMovesViewModel.MoveSectionItem.MoveItem) {
            val move = item.move
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
            binding.movePower.text = move.power?.toString() ?: "-"
            binding.moveAccuracy.text = if (move.accuracy != null) "${move.accuracy}%" else "-"
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
}