package com.example.pokedex.services.driverAdapters
import com.example.pokedex.services.controllers.RegionService
import com.example.pokedex.services.models.PokemonEntry
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.services.models.PokemonSpecies
import com.example.pokedex.services.models.PokemonSpeciesInfo
class PokemonDriverAdapter {
    private val service: RegionService = RegionService()


    fun PokemonsByRegion(
        region: String,
        loadData: (list: List<PokemonEntry>) -> Unit,
        errorData: () -> Unit
    ) {
        this.service.getPokemonsByRegion(
            region = region,
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )
    }
    fun PokemonInfo(
        nameOrId: String,
        loadData: (list: PokemonInfo) -> Unit,
        errorData: () -> Unit

    ){
        this.service.getPokemonInfo(
            nameOrId = nameOrId,
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )

    }

    fun PokemonSpeciesInfo(
        nameOrId: String,
        loadData: (speciesInfo: PokemonSpeciesInfo) -> Unit,
        errorData: () -> Unit
    ) {
        this.service.getPokemonSpecies(
            nameOrId = nameOrId,
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )
    }

    fun PokemonsByType(
        type: String,
        loadData: (list: List<PokemonEntry>) -> Unit,
        errorData: () -> Unit
    ) {
        this.service.getPokemonsByType(
            type = type,
            success = {
                val pokemonEntries = it.pokemon.map { pokemon ->
                    val entryNumber = extractPokemonNumber(pokemon.pokemon.url)
                    PokemonEntry(entryNumber, PokemonSpecies(pokemon.pokemon.name, pokemon.pokemon.url))
                }
                loadData(pokemonEntries)
            },
            error = {
                errorData()
            }
        )
    }

    private fun extractPokemonNumber(url: String): Int {
        return url.trimEnd('/').split('/').last().toInt()
    }






}