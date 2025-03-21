package com.example.completepokemondex.data.local.dao

import androidx.room.*
import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.local.entities.PokemonSpritesEntity

/**
 * Objeto de Acceso a Datos (DAO) para interactuar con las entidades Pokemon y PokemonSprites en la base de datos.
 *
 * Esta interfaz proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * en las tablas Pokemon y PokemonSprites.
 */
@Dao
interface PokemonDao {
    /**
     * Inserta una entidad Pokemon en la base de datos.
     *
     * Si ya existe un Pokemon con la misma clave primaria (generalmente el ID), será reemplazado.
     * Esto está definido por la estrategia [OnConflictStrategy.REPLACE].
     *
     * @param pokemon El [PokemonEntity] que se va a insertar o actualizar en la base de datos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    /**
     * Inserta una entidad PokemonSprites en la base de datos.
     *
     * Si ya existe PokemonSprites con la misma clave primaria (ID), será reemplazado.
     * Esto está definido por la estrategia [OnConflictStrategy.REPLACE].
     *
     * @param sprites El [PokemonSpritesEntity] que se va a insertar o actualizar en la base de datos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonSprites(sprites: PokemonSpritesEntity)

    /**
     * Busca y devuelve un Pokémon específico por su ID.
     *
     * @param id El identificador único del Pokémon a buscar.
     * @return El [PokemonEntity] correspondiente al ID proporcionado, o null si no se encuentra.
     */
    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    /**
     * Busca y devuelve un Pokémon específico por su nombre.
     *
     * @param name El nombre del Pokémon a buscar.
     * @return El [PokemonEntity] correspondiente al nombre proporcionado, o null si no se encuentra.
     */
    @Query("SELECT * FROM pokemon WHERE name = :name")
    suspend fun getPokemonByName(name: String): PokemonEntity?

    /**
     * Obtiene los sprites de un Pokémon específico por el ID del Pokémon.
     *
     * @param pokemonId El identificador único del Pokémon cuyos sprites se desean obtener.
     * @return El [PokemonSpritesEntity] correspondiente al ID proporcionado, o null si no se encuentra.
     */
    @Query("SELECT * FROM pokemonsprites WHERE id = :pokemonId")
    suspend fun getPokemonSpritesById(pokemonId: Int): PokemonSpritesEntity?

    /**
     * Obtiene todos los Pokémon almacenados en la base de datos.
     *
     * @return Una lista que contiene todos los [PokemonEntity] de la base de datos.
     */
    @Query("SELECT * FROM pokemon")
    suspend fun getAllPokemon(): List<PokemonEntity>

    /**
     * Elimina un Pokémon específico de la base de datos.
     *
     * @param pokemon El [PokemonEntity] que se va a eliminar de la base de datos.
     */
    @Delete
    suspend fun deletePokemon(pokemon: PokemonEntity)

    /**
     * Elimina todos los Pokémon de la base de datos.
     *
     * Esta operación vacía completamente la tabla de Pokémon.
     */
    @Query("DELETE FROM pokemon")
    suspend fun deleteAllPokemon()
}