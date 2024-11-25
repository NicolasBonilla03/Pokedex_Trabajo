package com.example.pokedex.services.models


data class EvolutionChain(
    val chain: EvolutionChainDetail
)

data class EvolutionChainDetail(
    val species: Species,
    val evolves_to: List<EvolutionChainDetail>?
)

data class Species(
    val name: String,
    val url: String
)





