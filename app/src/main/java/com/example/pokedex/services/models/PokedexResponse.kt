package com.example.pokedex.services.models

data class PokedexResponse(
    val pokemon_entries: List<PokemonEntry>
)



data class PokemonSpecies(
    val name: String,
    val url: String
)
