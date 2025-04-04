package com.example.purebapokemon.ui.pokemondetail

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.purebapokemon.domain.model.Pokemon
import com.example.purebapokemon.domain.usecase.GetPokemonDetailUseCase
import com.example.purebapokemon.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> = _pokemon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    @SuppressLint("NullSafeMutableLiveData")
    fun loadPokemon(pokemonId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = getPokemonDetailUseCase(pokemonId)
                if (result != null) {
                    _pokemon.value = result
                } else {
                    _error.value = "No se encontró el Pokémon"
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Error desconocido"
            }
        }
    }

    fun toggleFavorite() {
        val currentPokemon = _pokemon.value ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(currentPokemon.id, !currentPokemon.isFavorite)
            // Actualizamos la UI con el nuevo estado
            _pokemon.value = currentPokemon.copy(isFavorite = !currentPokemon.isFavorite)
        }
    }
}