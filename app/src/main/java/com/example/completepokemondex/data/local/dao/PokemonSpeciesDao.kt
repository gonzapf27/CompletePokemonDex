package com.example.completepokemondex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.completepokemondex.data.local.entities.PokemonSpeciesEntity

/**
 * Interfaz DAO (Data Access Object) para realizar operaciones en la tabla `pokemon_species_table`.
 * Proporciona métodos para insertar, consultar y recuperar datos relacionados con las especies de Pokémon.
 */
@Dao
interface PokemonSpeciesDao {

    /**
     * Inserta un objeto `PokemonSpeciesEntity` en la base de datos.
     * Si ya existe un registro con el mismo ID, lo reemplaza.
     *
     * @param pokemonSpecies La entidad de especie de Pokémon a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonSpecies(pokemonSpecies: PokemonSpeciesEntity)

    /**
     * Inserta una lista de objetos `PokemonSpeciesEntity` en la base de datos.
     * Si ya existen registros con los mismos IDs, los reemplaza.
     *
     * @param pokemonSpeciesList Lista de entidades de especies de Pokémon a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemonSpeciesList: List<PokemonSpeciesEntity>)

    /**
     * Recupera un objeto `PokemonSpeciesEntity` de la base de datos utilizando su ID.
     *
     * @param id El ID de la especie de Pokémon a recuperar.
     * @return La entidad de especie de Pokémon correspondiente al ID, o `null` si no existe.
     */
    @Query("SELECT * FROM pokemon_species_table WHERE id = :id")
    suspend fun getPokemonSpeciesById(id: Int): PokemonSpeciesEntity?

    /**
     * Recupera todas las entidades `PokemonSpeciesEntity` de la base de datos.
     *
     * @return Una lista de todas las especies de Pokémon almacenadas.
     */
    @Query("SELECT * FROM pokemon_species_table")
    suspend fun getAllPokemonSpecies(): List<PokemonSpeciesEntity>

    /**
     * Recupera un objeto `PokemonSpeciesEntity` de la base de datos utilizando su nombre.
     *
     * @param name El nombre de la especie de Pokémon a recuperar.
     * @return La entidad de especie de Pokémon correspondiente al nombre, o `null` si no existe.
     */
    @Query("SELECT * FROM pokemon_species_table WHERE name = :name")
    suspend fun getPokemonSpeciesByName(name: String): PokemonSpeciesEntity?
}