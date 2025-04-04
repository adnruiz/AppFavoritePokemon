package com.example.purebapokemon.domain.repository

import com.example.purebapokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getAllPokemon(): Flow<List<Pokemon>>
    fun getFavoritePokemon(): Flow<List<Pokemon>>
    suspend fun getPokemonById(id: Int): Pokemon?
    suspend fun refreshPokemonList(limit: Int, offset: Int)
    suspend fun getPokemonDetail(id: Int): Pokemon?
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
}