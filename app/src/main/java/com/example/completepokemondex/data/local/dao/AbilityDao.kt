package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.AbilityEntity

@Dao
interface AbilityDao {

    /**
     * Inserta una habilidad en la base de datos. Si el ID ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAbility(ability: AbilityEntity)

    /**
     * Inserta una lista de habilidades en la base de datos. Si el ID ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(abilities: List<AbilityEntity>)

    /**
     * Obtiene una habilidad por su ID.
     */
    @Query("SELECT * FROM ability_table WHERE id = :id")
    suspend fun getAbilityById(id: Int): AbilityEntity?

    /**
     * Obtiene todas las habilidades almacenadas.
     */
    @Query("SELECT * FROM ability_table")
    suspend fun getAllAbilities(): List<AbilityEntity>
}
