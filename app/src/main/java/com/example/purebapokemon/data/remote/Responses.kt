package com.example.purebapokemon.data.remote

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
)

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val types: List<PokemonTypeSlot>,
    val sprites: PokemonSprites
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonType
)

data class PokemonType(
    val name: String,
    val url: String
)

data class PokemonSprites(
    val front_default: String,
    val other: OtherSprites
)

data class OtherSprites(
    val `official-artwork`: OfficialArtwork
)

data class OfficialArtwork(
    val front_default: String
)