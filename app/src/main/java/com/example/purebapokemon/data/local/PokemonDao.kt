package com.example.purebapokemon.data.local
import java.util.List;
import kotlinx.coroutines.flow.Flow;
import androidx.room.*;

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_table")
    //fun getAll(): List<User>
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE isFavorite = 1")
    fun getFavoritePokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE id = :pokemonId")
    suspend fun getPokemonById(pokemonId: Int): PokemonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemonList: List<PokemonEntity>)

    @Update
    suspend fun updatePokemon(pokemon: PokemonEntity)

    @Query("UPDATE pokemon_table SET isFavorite = :isFavorite WHERE id = :pokemonId")
    suspend fun updateFavoriteStatus(pokemonId: Int, isFavorite: Boolean)
}
