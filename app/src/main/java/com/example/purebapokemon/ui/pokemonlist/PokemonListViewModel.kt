package com.example.purebapokemon.ui.pokemonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.purebapokemon.domain.model.Pokemon
import com.example.purebapokemon.domain.usecase.GetAllPokemonUseCase
import com.example.purebapokemon.domain.usecase.RefreshPokemonListUseCase
import com.example.purebapokemon.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getAllPokemonUseCase: GetAllPokemonUseCase,
    private val refreshPokemonListUseCase: RefreshPokemonListUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Primero obtenemos los datos de la base de datos
                getAllPokemonUseCase().collect { pokemonList ->
                    _pokemonList.value = pokemonList
                }

                // Si no hay datos o queremos actualizar, hacemos refresh desde la API
                if (_pokemonList.value.isNullOrEmpty()) {
                    refreshPokemonList()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshPokemonList() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                refreshPokemonListUseCase(20, 0)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(pokemon.id, !pokemon.isFavorite)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}