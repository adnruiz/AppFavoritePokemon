package com.example.purebapokemon.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PokemonEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao;
}