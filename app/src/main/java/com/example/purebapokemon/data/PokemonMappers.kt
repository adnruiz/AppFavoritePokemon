package com.example.purebapokemon.data

import com.example.purebapokemon.data.local.PokemonEntity
import com.example.purebapokemon.data.remote.PokemonDetailResponse
import com.example.purebapokemon.domain.model.Pokemon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Convertir de respuesta API a modelo de dominio
fun PokemonDetailResponse.toPokemon(): Pokemon {
    return Pokemon(
        id = id,
        name = name.capitalize(),
        imageUrl = sprites.other.`official-artwork`.front_default,
        types = types.map { it.type.name.capitalize() },
        isFavorite = false
    )
}

// Convertir de modelo de dominio a entidad Room
fun Pokemon.toEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = Gson().toJson(types),
        isFavorite = isFavorite
    )
}

// Convertir de entidad Room a modelo de dominio
fun PokemonEntity.toPokemon(): Pokemon {
    val typesList = Gson().fromJson<List<String>>(
        types,
        object : TypeToken<List<String>>() {}.type
    )
    return Pokemon(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = typesList,
        isFavorite = isFavorite
    )
}