package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.TypeEntity

@Dao
interface TypeDao {

    /**
     * Inserta un tipo en la base de datos. Si el ID ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertType(type: TypeEntity)

    /**
     * Inserta una lista de tipos en la base de datos. Si el ID ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(types: List<TypeEntity>)

    /**
     * Obtiene un tipo por su ID.
     */
    @Query("SELECT * FROM type_table WHERE type_id = :id")
    suspend fun getTypeById(id: Int): TypeEntity?

    /**
     * Obtiene todos los tipos almacenados.
     */
    @Query("SELECT * FROM type_table")
    suspend fun getAllTypes(): List<TypeEntity>
}
