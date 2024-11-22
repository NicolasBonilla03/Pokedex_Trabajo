package com.example.pokedex.services.models

data class TypeResponse(
    val pokemon : List<PokemonWrapper>,
)

data class Type(
    val name: String, // Nombre del tipo (e.g., grass, fire)
    val url: String   // URL del detalle del tipo
)

data class PokemonWrapper(
    val pokemon: PokemonSpecies
)
