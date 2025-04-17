package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity

@Dao
interface PokemonDetailsDao {

    /**
     * Obtiene el PokemonDetails de la base de datos.
     * @param id ID del PokemonDetails a obtener.
     * @return PokemonDetailsEntity con el ID especificado.
     */
    @Query("SELECT * FROM pokemon_details_table WHERE pokemon_id = :id")
    suspend fun getPokemonById(id: Int): PokemonDetailsEntity?

    /**
     * Inserta un PokemonDetails en la base de datos. Si el ID ya existe, lo reemplaza.
     *
     * @param pokemonDetails PokemonDetailsEntity a insertar o actualizar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemonDetails: PokemonDetailsEntity)

    /**
     * Obtiene todos los detalles de Pokémon de la base de datos.
     * Los sprites están embebidos en los resultados.
     */
    @Query("SELECT * FROM pokemon_details_table")
    suspend fun getAllPokemonDetails(): List<PokemonDetailsEntity>

    /**
     * Obtiene el PokemonDetails de la base de datos por nombre.
     * @param name Nombre del Pokémon a obtener.
     * @return PokemonDetailsEntity con el nombre especificado.
     */
    @Query("SELECT * FROM pokemon_details_table WHERE LOWER(pokemon_name) = LOWER(:name)")
    suspend fun getPokemonByName(name: String): PokemonDetailsEntity?
}
