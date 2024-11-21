package com.example.pokedex.services.models

import com.google.gson.annotations.SerializedName

data class RegionResponse(
    //val count: Int,
    @SerializedName("results")
    val results: List<Region>
)

data class Region(
    val name: String,
    val url: String,

)


