package com.example.purebapokemon.domain.usecase

import javax.inject.Inject
import com.example.purebapokemon.domain.repository.PokemonRepository

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int, isFavorite: Boolean){
        repository.updateFavoriteStatus(id, isFavorite)
    }
}