package com.example.pokedex.services.endpoints

import com.example.pokedex.services.models.EvolutionChain
import com.example.pokedex.services.models.PokedexResponse
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.services.models.PokemonSpeciesInfo
import com.example.pokedex.services.models.RegionResponse
import com.example.pokedex.services.models.TypeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Obtener todas las regiones
    @GET("region")
    suspend fun getAllRegions(): Response<RegionResponse>

    // Obtener los pokémons por región
    @GET("pokedex/{region}/")
    suspend fun getPokemonsByRegion(@Path("region") region: String): Response<PokedexResponse>

    // Obtener la información de un Pokémon
    @GET("pokemon/{nameOrId}/")
    suspend fun getPokemonInfo(@Path("nameOrId") nameOrId: String): Response<PokemonInfo>

    // Obtener la cadena de evolución de un Pokémon
    @GET("evolution-chain/{id}/")
    suspend fun getEvolutionChain(@Path("id") id: Int): Response<EvolutionChain>

    // ApiService
    @GET("pokemon-species/{nameOrId}/")
    suspend fun getPokemonSpecies(@Path("nameOrId") nameOrId: String): Response<PokemonSpeciesInfo>

    // Obtener la lista de tipos de Pokémon
    @GET("type/{type}/")
    suspend fun getPokemonsByType(@Path("type") type: String): Response<TypeResponse>



}
