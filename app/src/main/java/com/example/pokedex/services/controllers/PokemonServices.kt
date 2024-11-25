package com.example.pokedex.services.controllers
import ApiClient

import com.example.pokedex.services.endpoints.ApiService
import com.example.pokedex.services.models.EvolutionChain
import com.example.pokedex.services.models.PokemonEntry
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.services.models.PokemonSpecies
import com.example.pokedex.services.models.PokemonSpeciesInfo
import com.example.pokedex.services.models.Sprites
import com.example.pokedex.services.models.TypeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegionService: ApiClient() {
    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)

    fun getPokemonsByRegion(
        region: String,
        success: (pokemons: List<PokemonEntry>) -> Unit,
        error: () -> Unit
    ) {
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonsByRegion(region)

                val data = response.body()
                when (data) {
                    null -> success(emptyList())
                    else -> success(data.pokemon_entries)
                }
            } catch (e: Exception) {
                println(e)
                error()
            }
        }
    }

    fun getPokemonInfo(
        nameOrId: String,
        success: (pokemon: PokemonInfo) -> Unit,
        error: () -> Unit

    ){
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonInfo(nameOrId)
                val data = response.body()
                when (data) {
                    null -> success(PokemonInfo(0, "", emptyList(), Sprites("", ""), emptyList(), emptyList(), 0, 0))
                    else -> success(data)
                }
        } catch (e: Exception) {
            println(e)
            error()
        }
            }
    }
    fun getPokemonSpecies(
        nameOrId: String,
        success: (speciesInfo: PokemonSpeciesInfo) -> Unit,
        error: () -> Unit
    ) {
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonSpecies(nameOrId)
                val data = response.body()
                if (data != null) {
                    success(data)
                } else {
                    success(PokemonSpeciesInfo(emptyList(), PokemonSpecies("", "")))
                }
            } catch (e: Exception) {
                println(e)
                error()
            }
        }
    }
    fun getPokemonsByType(
        type: String,
        success: (response: TypeResponse) -> Unit,
        error: () -> Unit
    ) {
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getPokemonsByType(type)
                val data = response.body()
                when (data) {
                    null -> success(TypeResponse(emptyList()))
                    else -> success(data)
                }
            } catch (e: Exception) {
                println(e)
                error()
            }
        }
    }
    fun getEvolutionChain(
        id: Int,
        success: (evolutionChain: EvolutionChain) -> Unit,
        error: () -> Unit
    ) {
        serviceScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofit()
                    .create(ApiService::class.java)
                    .getEvolutionChain(id)

                val data = response.body()
                if (data != null) {
                    success(data)
                } else {
                    error()
                }
            } catch (e: Exception) {
                println("Error al obtener la cadena de evolución: $e")
                error()
            }
        }
    }

}