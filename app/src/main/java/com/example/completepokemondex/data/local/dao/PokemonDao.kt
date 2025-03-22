package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity

/**
 * DAO (Data Access Object) para interactuar con la tabla de Pokémon en la base de datos.
 * Contiene métodos para realizar operaciones CRUD sobre los datos de Pokémon.
 */
@Dao
interface PokemonDao {

    /**
     * Inserta un Pokémon en la base de datos. Si el ID ya existe, lo reemplaza.
     *
     * @param pokemon La entidad de Pokémon a insertar o actualizar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemon: PokemonDetailsEntity)

    /**
     * Obtiene un Pokémon por su ID.
     *
     * @param id El ID del Pokémon.
     * @return El Pokémon con el ID especificado.
     */
    @Query("SELECT * FROM pokemon_details_table WHERE id = :id LIMIT 1")
    suspend fun getPokemonById(id: Int): PokemonDetailsEntity?
}
