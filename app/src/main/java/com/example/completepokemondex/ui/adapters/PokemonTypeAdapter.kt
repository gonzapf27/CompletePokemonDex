package com.example.completepokemondex.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.completepokemondex.databinding.ItemPokemonTypeBinding
import com.example.completepokemondex.util.PokemonTypeUtil.PokemonType
import javax.inject.Inject

/**
 * Adaptador para mostrar y manejar chips de tipo de Pokémon.
 * Permite seleccionar un tipo y notifica el cambio mediante un listener.
 */
class PokemonTypeAdapter @Inject constructor() : 
    RecyclerView.Adapter<PokemonTypeAdapter.TypeViewHolder>() {
    
    private var types: List<PokemonType> = emptyList()
    private var onTypeSelected: ((String) -> Unit)? = null
    
    // Posición del tipo seleccionado, por defecto "all" (posición 0)
    private var selectedPosition = 0
    
    /**
     * Establece la lista de tipos de Pokémon a mostrar.
     */
    fun setTypesList(typesList: List<PokemonType>) {
        types = typesList
        notifyDataSetChanged()
    }
    
    /**
     * Establece el listener para cuando se selecciona un tipo.
     */
    fun setOnTypeSelectedListener(listener: (String) -> Unit) {
        onTypeSelected = listener
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = ItemPokemonTypeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TypeViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        if (types.isNotEmpty()) {
            holder.bind(types[position], position == selectedPosition)
        }
    }
    
    override fun getItemCount() = types.size
    
    /**
     * Actualiza el tipo seleccionado y notifica los cambios.
     */
    fun selectType(position: Int) {
        if (position != selectedPosition && position in types.indices) {
            val oldPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
            onTypeSelected?.invoke(types[position].name)
        }
    }
    
    /**
     * ViewHolder para cada chip de tipo de Pokémon.
     */
    inner class TypeViewHolder(private val binding: ItemPokemonTypeBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.typeChip.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectType(position)
                }
            }
        }
        
        /**
         * Vincula los datos del tipo al chip y aplica estilos según si está seleccionado.
         */
        fun bind(type: PokemonType, isSelected: Boolean) {
            binding.typeChip.apply {
                text = if (type.stringRes != 0) {
                    context.getString(type.stringRes)
                } else {
                    type.name.replaceFirstChar { it.uppercase() }
                }

                val realColor = ContextCompat.getColor(context, type.colorRes)
                chipBackgroundColor = ColorStateList.valueOf(realColor)
                isChecked = isSelected

                // Cambia el color del texto y el borde si está seleccionado
                if (isSelected) {
                    setTextColor(android.graphics.Color.WHITE)
                    chipStrokeWidth = 6f
                    chipStrokeColor = ColorStateList.valueOf(android.graphics.Color.WHITE)
                    // Animación de escala
                    animate().scaleX(1.15f).scaleY(1.15f).setDuration(150).start()
                    // Icono de check
                    chipIcon = context.getDrawable(android.R.drawable.checkbox_on_background)
                    isChipIconVisible = true
                } else {
                    setTextColor(android.graphics.Color.BLACK)
                    chipStrokeWidth = 0f
                    chipStrokeColor = ColorStateList.valueOf(realColor)
                    // Vuelve a escala normal
                    animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                    // Sin icono
                    chipIcon = null
                    isChipIconVisible = false
                }
            }
        }
    }
}
