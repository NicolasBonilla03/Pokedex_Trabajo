package com.example.pokedex.services.endpoints

import com.example.pokedex.services.models.EvolutionChain
import com.example.pokedex.services.models.PokedexResponse
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.services.models.Region
import com.example.pokedex.services.models.RegionResponse
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
}
