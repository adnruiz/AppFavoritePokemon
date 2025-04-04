package com.example.purebapokemon.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.purebapokemon.data.repository.PokemonRepositoryImp
import com.example.purebapokemon.domain.repository.PokemonRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPokemonRepository(
        pokemonRepositoryImp: PokemonRepositoryImp
    ): PokemonRepository
}