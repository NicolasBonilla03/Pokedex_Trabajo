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

        val regionName = intent.getStringExtra("REGION_NAME")?.capitalizeFirstLetter() ?: "kanto" // Recuperar la región

        setContent {
            PokedexTheme {
                val pokemonList = remember { mutableStateOf<List<PokemonEntry>>(emptyList()) }

                // Usar la región seleccionada
                driverAdapter.PokemonsByRegion(
                    region = regionName.lowercase(),
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
fun String.capitalizeFirstLetter(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}




@Composable
fun PokemonList(pokemonEntries: List<PokemonEntry>, modifier: Modifier = Modifier) {
    if (pokemonEntries.isEmpty()) {
        Text(text = "No hay Pokémon disponibles en esta región.")
        return
    } else {
        LazyColumn(modifier = modifier) {
            items(
                items = pokemonEntries,
                key = { it.entry_number }
            ) { pokemon ->
                Column {
                    Row {
                        // Extraer el número del Pokémon desde la URL
                        val pokemonNumber = extractPokemonNumber(pokemon.pokemon_species.url)
                        Text(text = "ID: $pokemonNumber ")

                    }
                    Row {
                        Text(text = "Nombre: ${pokemon.pokemon_species.name.capitalizeFirstLetter()}")
                    }
                }
            }
        }
    }
}
fun extractPokemonNumber(url: String): Int {
    return url.trimEnd('/').split('/').last().toInt()
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