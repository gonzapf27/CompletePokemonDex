package com.example.completepokemondex.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity
import com.example.completepokemondex.data.local.entities.PokemonEntity
import com.example.completepokemondex.data.local.entities.PokemonSpeciesEntity

/**
 * Base de datos principal de la aplicación Pokedex que contiene
 * todas las tablas relacionadas con los Pokémon.
 * 
 * @property pokemonDao DAO para acceder a la información de Pokémon.
 */
@Database(
    entities = [PokemonEntity::class, PokemonDetailsEntity::class, PokemonSpeciesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokedexDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO de Pokémon.
     */
    abstract fun pokemonDao(): PokemonDao

    companion object {
        // Singleton para prevenir múltiples instancias de la base de datos
        @Volatile
        private var INSTANCE: PokedexDatabase? = null

        /**
         * Obtiene una instancia de la base de datos Pokedex.
         * Si la instancia ya existe, la retorna; de lo contrario, crea una nueva.
         *
         * @param context El contexto de la aplicación.
         * @return Una instancia de PokedexDatabase.
         */
        fun getDatabase(context: Context): PokedexDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokedexDatabase::class.java,
                    "pokedex_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
