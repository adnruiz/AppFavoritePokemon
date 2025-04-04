package com.example.purebapokemon.data.repository

import android.database.sqlite.SQLiteException
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.purebapokemon.data.local.PokemonDao
import com.example.purebapokemon.data.remote.PokemonApiService
import com.example.purebapokemon.data.toEntity
import com.example.purebapokemon.data.toPokemon
import com.example.purebapokemon.domain.model.Pokemon
import com.example.purebapokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class PokemonRepositoryImp @Inject constructor(
    private val pokemonApiService: PokemonApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {
    override fun getAllPokemon(): Flow<List<Pokemon>> {
        return pokemonDao.getAllPokemon().map { entities ->
            entities.map { it.toPokemon() }
        }
    }

    override fun getFavoritePokemon(): Flow<List<Pokemon>> {
        return pokemonDao.getFavoritePokemon().map { entities ->
            entities.map { it.toPokemon() }
        }
    }

    override suspend fun getPokemonById(id: Int): Pokemon? {
        return pokemonDao.getPokemonById(id)?.toPokemon()
    }

    override suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        pokemonDao.updateFavoriteStatus(id, isFavorite)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun refreshPokemonList(limit: Int, offset: Int) {
        try {
            val response = pokemonApiService.getPokemonList(limit, offset)
            val pokemonList = response.results

            pokemonList.forEach { pokemonResult ->
                try {
                    val url = pokemonResult.url
                    val id = url.split("/").dropLast(1).last().toInt()

                    val existingPokemon = pokemonDao.getPokemonById(id)
                    val isFavorite = existingPokemon?.isFavorite ?: false

                    val detailResponse = pokemonApiService.getPokemonDetail(id)
                    val pokemon = detailResponse.toPokemon().copy(isFavorite = isFavorite)

                    pokemonDao.insertPokemon(pokemon.toEntity())
                } catch (e: HttpException) {
                    //API HTTP errors (e.g., 404, 500, etc.)
                    Log.e("PokemonRepository", "HTTP error fetching pokemon details", e)
                } catch (e: IOException) {
                    //Network
                    Log.e("PokemonRepository", "Network error fetching pokemon details", e)
                    throw e
                } catch (e: SQLiteException) {
                    //database errors
                    Log.e("PokemonRepository", "Database error saving pokemon", e)
                } catch (e: NumberFormatException) {
                    //URL parsing errors
                    Log.e("PokemonRepository", "Error parsing pokemon ID from URL", e)
                }
            }
        } catch (e: HttpException) {
            //API HTTP errors
            Log.e("PokemonRepository", "HTTP error fetching pokemon list", e)
            throw PokemonApiException("Failed to fetch pokemon list", e)
        } catch (e: IOException) {
            //conectividad del network
            Log.e("PokemonRepository", "Network error fetching pokemon list", e)
            throw PokemonApiException("Network unavailable", e)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getPokemonDetail(id: Int): Pokemon? {
        return try {
            val localPokemon = pokemonDao.getPokemonById(id)
            if (localPokemon != null) {
                localPokemon.toPokemon()
            } else {
                val response = pokemonApiService.getPokemonDetail(id)
                val pokemon = response.toPokemon()
                pokemonDao.insertPokemon(pokemon.toEntity())
                pokemon
            }
        } catch (e: HttpException) {
            //API HTTP errors
            Log.e("PokemonRepository", "HTTP error fetching pokemon list", e)
            throw PokemonApiException("Failed to fetch pokemon list", e)
        } catch (e: IOException) {
            //conectividad del network
            Log.e("PokemonRepository", "Network error fetching pokemon list", e)
            throw PokemonApiException("Network unavailable", e)
        }
    }
}

class PokemonApiException(message: String, cause: Throwable) : Exception(message, cause)