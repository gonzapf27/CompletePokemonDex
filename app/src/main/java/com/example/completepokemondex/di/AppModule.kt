package com.example.completepokemondex.di

import android.content.Context
import com.example.completepokemondex.data.local.dao.AbilityDao
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.dao.PokemonDetailsDao
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
import com.example.completepokemondex.data.local.dao.EvolutionChainDao
import com.example.completepokemondex.data.local.database.PokedexDatabase
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePokemonRepository(
        pokemonDao: PokemonDao,
        pokemonDetailsDao: PokemonDetailsDao,
        pokemonSpeciesDao: PokemonSpeciesDao,
        abilityDao: AbilityDao,
        remoteDataSource: PokemonRemoteDataSource,
        evolutionChainDao: EvolutionChainDao
    ): PokemonRepository =
        PokemonRepository(
            pokemonDao,
            pokemonDetailsDao,
            pokemonSpeciesDao,
            abilityDao,
            remoteDataSource,
            evolutionChainDao
        )

    @Provides
    @Singleton
    fun providePokemonDao(database: PokedexDatabase): PokemonDao =
        database.pokemonDao()

    @Provides
    @Singleton
    fun providePokemonDetailsDao(database: PokedexDatabase): PokemonDetailsDao =
        database.pokemonDetailsDao()

    @Provides
    @Singleton
    fun providePokemonSpeciesDao(database: PokedexDatabase): PokemonSpeciesDao =
        database.pokemonSpeciesDao()

    @Provides
    @Singleton
    fun provideAbilityDao(database: PokedexDatabase): AbilityDao =
        database.abilityDao()

    @Provides
    @Singleton
    fun provideEvolutionChainDao(database: PokedexDatabase): EvolutionChainDao =
        database.evolutionChainDao()

    @Provides
    @Singleton
    fun providePokemonRemoteDataSource(): PokemonRemoteDataSource =
        PokemonRemoteDataSource()

    @Provides
    @Singleton
    fun providePokedexDatabase(@ApplicationContext appContext: Context): PokedexDatabase =
        PokedexDatabase.getDatabase(appContext)
}