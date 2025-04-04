package com.example.purebapokemon.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.purebapokemon.domain.model.Pokemon
import com.example.purebapokemon.domain.repository.PokemonRepository

class GetAllPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
){
    operator fun invoke(): Flow<List<Pokemon>> {
        return repository.getAllPokemon()
    }
}