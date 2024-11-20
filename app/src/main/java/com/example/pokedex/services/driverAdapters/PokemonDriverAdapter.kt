package com.example.pokedex.services.driverAdapters
import com.example.pokedex.services.controllers.RegionService
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
}