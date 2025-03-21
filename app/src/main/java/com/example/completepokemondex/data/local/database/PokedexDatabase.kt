package com.example.completepokemondex.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.completepokemondex.data.local.converters.SpritesTypeConverters
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.local.entities.PokemonSpritesEntity

@Database(
    entities = [PokemonEntity::class, PokemonSpritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SpritesTypeConverters::class)
abstract class PokedexDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokedexDatabase? = null

        fun getDatabase(context: Context): PokedexDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokedexDatabase::class.java,
                    "pokedex_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}