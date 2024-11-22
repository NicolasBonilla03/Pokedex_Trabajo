package com.example.pokedex.services.driverAdapters
import com.example.pokedex.services.controllers.RegionService
import com.example.pokedex.services.models.PokemonEntry
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.services.models.Region
import com.example.pokedex.services.models.RegionResponse
import kotlinx.coroutines.runBlocking

class PokemonDriverAdapter {
    private val service: RegionService = RegionService()

    fun allRegions(
        loadData: (list: List<Region>) -> Unit,
        errorData: () -> Unit
    ) {
        this.service.getAllRegions(
            success = {
                loadData(it)
            },
            error = {
                errorData()
            }
        )
    }
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

}