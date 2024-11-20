package com.example.pokedex.services.models

data class EvolutionChain(
    val chain: ChainLink // Eslabón principal de la cadena de evolución
)

data class ChainLink(
    val species: NamedResource,      // Especie del pokémon actual
    val evolves_to: List<ChainLink> // Lista de evoluciones desde esta especie
)
