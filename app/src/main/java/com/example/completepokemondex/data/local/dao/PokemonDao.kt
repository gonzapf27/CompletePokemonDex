package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity
import com.example.completepokemondex.data.local.entities.PokemonEntity

/**
 * DAO (Data Access Object) para interactuar con la tabla de Pokémon en la base de datos. Contiene
 * métodos para realizar operaciones CRUD sobre los datos de Pokémon.
 */
@Dao
interface PokemonDao {

    /** Inserta un Pokemon en la base de datos. Si el ID ya existe, lo reemplaza */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    /**
     * Inserta múltiples Pokémon en la base de datos. Si algún ID ya existe, lo reemplaza.
     *
     * @param pokemonList Lista de Pokémon a insertar o actualizar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemonList: List<PokemonEntity>)

    /**
     * Obtiene todos los Pokémon de la base de datos.
     *
     * @return Lista de todos los Pokémon en la base de datos.
     */
    @Query("SELECT * FROM pokemon_table ORDER BY pokemon_id ASC")
    suspend fun getAllPokemon(): List<PokemonEntity>

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
}
