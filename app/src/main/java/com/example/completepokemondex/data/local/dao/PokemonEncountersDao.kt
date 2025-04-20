package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.PokemonEncountersEntity

@Dao
interface PokemonEncountersDao {

    /**
     * Inserta la información de encuentros de un Pokémon en la base de datos.
     * Si el ID del Pokémon ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonEncounters(pokemonEncounters: PokemonEncountersEntity)

    /**
     * Inserta una lista de información de encuentros de Pokémon en la base de datos.
     * Si el ID del Pokémon ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemonEncounters: List<PokemonEncountersEntity>)

    /**
     * Obtiene la información de encuentros de un Pokémon por su ID.
     */
    @Query("SELECT * FROM pokemon_encounters_table WHERE pokemon_id = :pokemonId")
    suspend fun getPokemonEncountersById(pokemonId: Int): PokemonEncountersEntity?

    /**
     * Elimina la información de encuentros de un Pokémon por su ID.
     */
    @Query("DELETE FROM pokemon_encounters_table WHERE pokemon_id = :pokemonId")
    suspend fun deletePokemonEncountersById(pokemonId: Int)

    /**
     * Obtiene toda la información de encuentros de Pokémon almacenada.
     */
    @Query("SELECT * FROM pokemon_encounters_table")
    suspend fun getAllPokemonEncounters(): List<PokemonEncountersEntity>
}
