package com.example.completepokemondex.di

import android.content.Context
import com.example.completepokemondex.data.local.dao.AbilityDao
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.dao.PokemonDetailsDao
import com.example.completepokemondex.data.local.dao.PokemonSpeciesDao
import com.example.completepokemondex.data.remote.datasource.PokemonRemoteDataSource
import com.example.completepokemondex.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
        remoteDataSource: PokemonRemoteDataSource
    ): PokemonRepository =
        PokemonRepository(
            pokemonDao,
            pokemonDetailsDao,
            pokemonSpeciesDao,
            abilityDao,
            remoteDataSource
        )
}