package com.example.pokedex

import android.content.Intent
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
import androidx.compose.material3.Button
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

class PokemonesRegion : ComponentActivity() {
    private val driverAdapter = PokemonDriverAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val regionId = intent.getIntExtra("REGION_ID", 0) // Recuperar la región

        setContent {
            PokedexTheme {
                val pokemonList = remember { mutableStateOf<List<PokemonEntry>>(emptyList()) }

                // Usar la región seleccionada
                driverAdapter.PokemonsByRegion(
                    region = regionId.toString().lowercase(),
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
                        modifier = Modifier.padding(innerPadding),
                        onClickPokemon = {
                             val intent = Intent(this, InfoPokemon::class.java)
                             intent.putExtra("POKEMON_ID", it)
                             startActivity(intent)
                        }
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
fun PokemonList(
    pokemonEntries: List<PokemonEntry>,
    modifier: Modifier = Modifier,
    onClickPokemon: (String) -> Unit = {}
) {
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

                    Button(onClick = {
                        onClickPokemon(pokemon.entry_number.toString())
                    }) {
                        Text(text = "Ver detalles")
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