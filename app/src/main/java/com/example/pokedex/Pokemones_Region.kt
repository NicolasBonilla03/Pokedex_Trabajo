package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedex.services.driverAdapters.PokemonDriverAdapter
import com.example.pokedex.services.models.PokemonEntry
import com.example.pokedex.ui.theme.PokedexTheme

class Pokemones_Region : ComponentActivity() {
    private val driverAdapter = PokemonDriverAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val regionName = intent.getStringExtra("REGION_NAME") ?: "kanto" // Recuperar la región

        setContent {
            PokedexTheme {
                val pokemonList = remember { mutableStateOf<List<PokemonEntry>>(emptyList()) }

                // Usar la región seleccionada
                driverAdapter.PokemonsByRegion(
                    region = regionName,
                    loadData = {
                        pokemonList.value = it
                    },
                    errorData = {
                        // Manejar errores
                        println("Error al cargar los Pokémon de la región.")
                    }
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonList(
                        pokemonEntries = pokemonList.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun PokemonList(pokemonEntries: List<PokemonEntry>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(
            items = pokemonEntries,
            key = { it.entry_number }
        ) {
            Column {
                Row {
                    Text(text = "ID: ${it.entry_number} ")
                    Text(text = "Nombre: ${it.pokemon_species.name}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PokedexTheme {
        PokemonList(
            pokemonEntries = emptyList(),
            modifier = Modifier.padding(16.dp)
        )
    }
}