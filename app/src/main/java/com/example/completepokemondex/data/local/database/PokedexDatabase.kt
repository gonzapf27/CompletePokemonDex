package com.example.completepokemondex.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.completepokemondex.data.local.converters.SpritesTypeConverters
import com.example.completepokemondex.data.local.dao.PokemonDao
import com.example.completepokemondex.data.local.entities.PokemonDetailsEntity
import com.example.completepokemondex.data.local.entities.PokemonSpritesEntity

/**
 * Base de datos Room para la aplicación Pokedex.
 *
 * Esta clase define la estructura de la base de datos SQLite utilizada para almacenar
 * información de Pokémon localmente en el dispositivo. Utiliza Room como capa de abstracción
 * y maneja múltiples entidades.
 *
 * @property entities Define las entidades que se manejarán en la base de datos.
 * @property version Número de versión actual de la base de datos.
 * @property exportSchema Determina si se exporta el esquema de la base de datos.
 */
@Database(
    entities = [PokemonDetailsEntity::class, PokemonSpritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SpritesTypeConverters::class)
abstract class PokedexDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO de Pokémon para realizar operaciones en la base de datos.
     *
     * @return Instancia del DAO para manipular datos de Pokémon.
     */
    abstract fun pokemonDao(): PokemonDao

    /**
     * Objeto compañero que contiene métodos estáticos y la instancia singleton de la base de datos.
     */
    companion object {
        /**
         * Instancia única de la base de datos siguiendo el patrón Singleton.
         * La anotación @Volatile garantiza que los cambios sean visibles inmediatamente para todos los hilos.
         */
        @Volatile
        private var INSTANCE: PokedexDatabase? = null

        /**
         * Obtiene la instancia de la base de datos, creándola si no existe.
         *
         * Este método implementa el patrón Singleton con bloqueo doble para garantizar
         * que solo se cree una instancia de la base de datos, incluso en entornos multihilo.
         *
         * @param context El contexto de la aplicación utilizado para crear la base de datos.
         * @return Instancia única de la base de datos PokedexDatabase.
         */
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