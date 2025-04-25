package com.example.completepokemondex.di

import android.content.Context
import com.example.completepokemondex.data.local.dao.AbilityDao
import com.example.completepokemondex.data.local.dao.EvolutionChainDao
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.dao.PokemonDetailsDao
import com.example.completepokemondex.data.local.dao.PokemonEncountersDao
import com.example.completepokemondex.data.local.dao.PokemonMoveDao
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokedexDatabase(@ApplicationContext context: Context): PokedexDatabase {
        return PokedexDatabase.getDatabase(context)
    }

    @Provides
    fun providePokemonDao(database: PokedexDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    fun providePokemonDetailsDao(database: PokedexDatabase): PokemonDetailsDao {
        return database.pokemonDetailsDao()
    }

    @Provides
    fun providePokemonSpeciesDao(database: PokedexDatabase): PokemonSpeciesDao {
        return database.pokemonSpeciesDao()
    }

    @Provides
    fun provideAbilityDao(database: PokedexDatabase): AbilityDao {
        return database.abilityDao()
    }

    @Provides
    fun provideEvolutionChainDao(database: PokedexDatabase): EvolutionChainDao {
        return database.evolutionChainDao()
    }

    @Provides
    fun providePokemonRemoteDataSource(): PokemonRemoteDataSource {
        return PokemonRemoteDataSource()
    }

    @Provides
    fun providePokemonEncountersDao(database: PokedexDatabase): PokemonEncountersDao {
        return database.pokemonEncountersDao()
    }

    @Provides
    fun providePokemonMoveDao(database: PokedexDatabase): PokemonMoveDao {
        return database.pokemonMoveDao()
    }

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
        pokemonMoveDao: PokemonMoveDao

    ): PokemonRepository {
        return PokemonRepository(
            pokemonDao,
            pokemonDetailsDao,
            pokemonSpeciesDao,
            abilityDao,
            remoteDataSource,
            evolutionChainDao,
            pokemonEncountersDao,
            pokemonMoveDao
        )
    }
}