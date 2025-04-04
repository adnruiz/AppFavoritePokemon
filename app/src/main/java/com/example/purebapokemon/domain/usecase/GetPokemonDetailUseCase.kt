package com.example.purebapokemon.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.purebapokemon.domain.model.Pokemon
import com.example.purebapokemon.domain.repository.PokemonRepository

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
){
    suspend operator fun invoke(id: Int): Pokemon? {
        return repository.getPokemonById(id)
    }
}