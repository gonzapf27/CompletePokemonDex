package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.PokemonMoveEntity

@Dao
interface PokemonMoveDao {

    /**
     * Inserta un movimiento en la base de datos. Si el ID ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMove(move: PokemonMoveEntity)

    /**
     * Inserta una lista de movimientos en la base de datos. Si el ID ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(moves: List<PokemonMoveEntity>)

    /**
     * Obtiene un movimiento por su ID.
     */
    @Query("SELECT * FROM pokemon_move_table WHERE move_id = :id")
    suspend fun getMoveById(id: Int): PokemonMoveEntity?

    /**
     * Obtiene todos los movimientos almacenados.
     */
    @Query("SELECT * FROM pokemon_move_table")
    suspend fun getAllMoves(): List<PokemonMoveEntity>
}
