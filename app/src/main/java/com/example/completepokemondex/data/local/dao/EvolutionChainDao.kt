package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.EvolutionChainEntity

@Dao
interface EvolutionChainDao {

    /**
     * Inserta una cadena de evolución en la base de datos. Si el ID ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvolutionChain(chain: EvolutionChainEntity)

    /**
     * Obtiene una cadena de evolución por su ID.
     */
    @Query("SELECT * FROM evolution_chain_table WHERE id = :id")
    suspend fun getEvolutionChainById(id: Int): EvolutionChainEntity?
}
