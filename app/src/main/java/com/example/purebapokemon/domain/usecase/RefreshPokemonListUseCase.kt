package com.example.purebapokemon.domain.usecase


import javax.inject.Inject
import com.example.purebapokemon.domain.repository.PokemonRepository

class RefreshPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
){
    suspend operator fun invoke(limit: Int = 20, offset: Int = 0){
        repository.refreshPokemonList(limit, offset)
    }
}