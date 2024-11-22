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

                driverAdapter.PokemonInfo(
                    nameOrId = pokemonId ?: "",
                    loadData = {
                        pokemonInfo.value = it
                    },
                    errorData = {
                        println("Error al cargar los detalles del Pokémon.")
                    }
                )
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InfoScreen(
                        pokemonInfo = pokemonInfo.value,
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
                Text(text = "Altura: ${pokemonInfo.height}")
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
            Row {
                Text(text = "Estadísticas: ${pokemonInfo.stats.joinToString { it.stat.name }}")
            }
            Row {
                Text(text = "Imagen frontal")
                AsyncImage(
                    model = pokemonInfo.sprites.front_default,
                    contentDescription = "Imagen frontal del Pokémon",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Row {
                Text(text = "Imagen frontal shiny")
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
            name = "Pikachu",
            modifier = Modifier.padding(16.dp)
        )
}
}
