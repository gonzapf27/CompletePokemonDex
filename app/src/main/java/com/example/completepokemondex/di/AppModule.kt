package com.example.completepokemondex.di

import android.content.Context
import com.example.completepokemondex.data.local.dao.AbilityDao
import com.example.completepokemondex.data.local.dao.EvolutionChainDao
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.dao.PokemonDetailsDao
import com.example.completepokemondex.data.local.dao.PokemonEncountersDao
import com.example.completepokemondex.data.local.dao.PokemonMoveDao
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
import com.example.completepokemondex.data.local.dao.TypeDao
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer dependencias de la base de datos, DAOs y repositorios.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provee la instancia de la base de datos de la Pokédex.
     */
    @Provides
    @Singleton
    fun providePokedexDatabase(@ApplicationContext context: Context): PokedexDatabase {
        return PokedexDatabase.getDatabase(context)
    }

    /**
     * Provee el DAO de Pokémon.
     */
    @Provides
    fun providePokemonDao(database: PokedexDatabase): PokemonDao {
        return database.pokemonDao()
    }

    /**
     * Provee el DAO de detalles de Pokémon.
     */
    @Provides
    fun providePokemonDetailsDao(database: PokedexDatabase): PokemonDetailsDao {
        return database.pokemonDetailsDao()
    }

    /**
     * Provee el DAO de especies de Pokémon.
     */
    @Provides
    fun providePokemonSpeciesDao(database: PokedexDatabase): PokemonSpeciesDao {
        return database.pokemonSpeciesDao()
    }

    /**
     * Provee el DAO de habilidades.
     */
    @Provides
    fun provideAbilityDao(database: PokedexDatabase): AbilityDao {
        return database.abilityDao()
    }

    /**
     * Provee el DAO de cadenas evolutivas.
     */
    @Provides
    fun provideEvolutionChainDao(database: PokedexDatabase): EvolutionChainDao {
        return database.evolutionChainDao()
    }

    /**
     * Provee el data source remoto de Pokémon.
     */
    @Provides
    fun providePokemonRemoteDataSource(): PokemonRemoteDataSource {
        return PokemonRemoteDataSource()
    }

    /**
     * Provee el DAO de encuentros de Pokémon.
     */
    @Provides
    fun providePokemonEncountersDao(database: PokedexDatabase): PokemonEncountersDao {
        return database.pokemonEncountersDao()
    }

    /**
     * Provee el DAO de movimientos de Pokémon.
     */
    @Provides
    fun providePokemonMoveDao(database: PokedexDatabase): PokemonMoveDao {
        return database.pokemonMoveDao()
    }

    /**
     * Provee el DAO de tipos de Pokémon.
     */
    @Provides
    fun providePokemonTypeDao(database: PokedexDatabase): TypeDao {
        return database.typeDao()
    }

    /**
     * Provee el repositorio principal de Pokémon.
     */
    @Provides
    @Singleton
    fun providePokemonRepository(
        pokemonDao: PokemonDao,
        pokemonDetailsDao: PokemonDetailsDao,
        pokemonSpeciesDao: PokemonSpeciesDao,
        abilityDao: AbilityDao,
        remoteDataSource: PokemonRemoteDataSource,
        evolutionChainDao: EvolutionChainDao,
        pokemonEncountersDao: PokemonEncountersDao,
        pokemonMoveDao: PokemonMoveDao,
        pokemonTypeDao: TypeDao

    ): PokemonRepository {
        return PokemonRepository(
            pokemonDao,
            pokemonDetailsDao,
            pokemonSpeciesDao,
            abilityDao,
            remoteDataSource,
            evolutionChainDao,
            pokemonEncountersDao,
            pokemonMoveDao,
            pokemonTypeDao
        )
    }
}