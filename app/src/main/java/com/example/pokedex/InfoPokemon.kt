package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokedex.services.driverAdapters.PokemonDriverAdapter
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.services.models.PokemonSpeciesInfo
import com.example.pokedex.ui.theme.PokedexTheme

class InfoPokemon : ComponentActivity() {
    private val driverAdapter = PokemonDriverAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val pokemonId = intent.getStringExtra("POKEMON_ID")

        setContent {
            PokedexTheme {
                val pokemonInfo = remember { mutableStateOf<PokemonInfo?>(null) }
                val speciesInfo = remember { mutableStateOf<PokemonSpeciesInfo?>(null) }

                // Cargar información del Pokémon
                driverAdapter.PokemonInfo(
                    nameOrId = pokemonId ?: "",
                    loadData = { pokemonInfo.value = it },
                    errorData = { println("Error al cargar los detalles del Pokémon.") }
                )

                // Cargar información adicional ("Acerca de")
                driverAdapter.PokemonSpeciesInfo(
                    nameOrId = pokemonId ?: "",
                    loadData = { speciesInfo.value = it },
                    errorData = { println("Error al cargar la información de la especie.") }
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InfoScreen(
                        pokemonInfo = pokemonInfo.value,
                        speciesInfo = speciesInfo.value,
                        name = pokemonId ?: "",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun InfoScreen(
    pokemonInfo: PokemonInfo?,
    speciesInfo: PokemonSpeciesInfo?,
    name: String,
    modifier: Modifier = Modifier) {
    if (pokemonInfo == null) {
        Text(text = "Cargando detalles del Pokémon...")
    } else {
        val pokemonName = pokemonInfo.name
        Text(text = "Detalles del Pokémon: $pokemonName")
        Column {
            Row {
                Text(text = "Nombre: $pokemonName")
            }
            Row {
                Text(text = "ID: ${pokemonInfo.id}")
            }
            Row {
                Text(text = "Altura: ${pokemonInfo.height}0 cm")
            }
            Row {
                Text(text = "Peso: ${pokemonInfo.weight}")
            }
            Row {
                Text(text = "Tipo: ${pokemonInfo.types.joinToString { it.type.name }}")
            }
            Row {
                Text(text = "Habilidades: ${pokemonInfo.abilities.joinToString { it.ability.name }}")
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Estadísticas: ", modifier = Modifier.padding(bottom = 8.dp))
                pokemonInfo.stats.forEach { stat ->
                    Text(
                        text = "${stat.stat.name.capitalize()}: ${stat.base_stat}",
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            Column() {
                if (speciesInfo != null) {
                    val flavorText = speciesInfo.flavor_text_entries
                        .firstOrNull { it.language.name == "es" }?.flavor_text?.replace("\n", " ")
                        ?: "No description available."
                    Text(text = "Acerca de:", modifier = Modifier.padding(top = 16.dp))
                    Text(text = flavorText, modifier = Modifier.padding(top = 8.dp))
                    Text(text = "")
                } else {
                    Text(text = "Cargando información adicional...")
                }
            }

            Row {
                Text(text = "Imagen Pokemon")
                AsyncImage(
                    model = pokemonInfo.sprites.front_default,
                    contentDescription = "Imagen del Pokémon",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Row {
                Text(text = "Imagen Pokemon shiny")
                AsyncImage(
                    model = pokemonInfo.sprites.front_shiny,
                    contentDescription = "Imagen shiny del Pokémon",
                    modifier = Modifier.padding(16.dp)
                )
        }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    PokedexTheme {
        InfoScreen(
            pokemonInfo = null,
            speciesInfo = null,
            name = "Pikachu",
            modifier = Modifier.padding(16.dp)
        )
}
}
