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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokedex.services.models.Region
import com.example.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private val regions = listOf(
        Region("National", "https://pokeapi.co/api/v2/pokedex/1"), //ACA SE MUESTRAN TODOS LOS POKEMONES SIN FALTA
        Region("Kanto", "https://pokeapi.co/api/v2/pokedex/2"),
        Region("Johto", "https://pokeapi.co/api/v2/pokedex/3"),
        Region("Hoenn", "https://pokeapi.co/api/v2/pokedex/4"),
        Region("Sinnoh", "https://pokeapi.co/api/v2/pokedex/5"), // Falta Pokemon 491,492 y 493
        Region("Unova", "https://pokeapi.co/api/v2/pokedex/8"),
        Region("Conquest-Gallery", "https://pokeapi.co/api/v2/pokedex/11"),
        Region("Kalos-Central", "https://pokeapi.co/api/v2/pokedex/12"),
        Region("Kalos-Coastal", "https://pokeapi.co/api/v2/pokedex/13"),
        Region("Kalos-Mountain", "https://pokeapi.co/api/v2/pokedex/14"),// Falta Pokemon 719,721 y 721
        Region("Alola", "https://pokeapi.co/api/v2/pokedex/21"), // Falta Pokemon 808 y 809
        Region("Galar", "https://pokeapi.co/api/v2/pokedex/27"),  // Falta Pokemon 891 a 898
        Region("Hisui", "https://pokeapi.co/api/v2/pokedex/30"),
        Region("Paldea", "https://pokeapi.co/api/v2/pokedex/31"),
        Region("BlueBerry", "https://pokeapi.co/api/v2/pokedex/32"),
        Region("Kitakami", "https://pokeapi.co/api/v2/pokedex/33"),)
        // Al final, faltan Pokemones del 1018 al 1025


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PokedexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokedexScreen(
                        regions = regions,
                        modifier = Modifier.padding(innerPadding),
                        onClickRegion = { goToRegion(it) }
                    )
                }

                PokedexScreen(regions = regions, onClickRegion = { goToRegion(it) })
        }
    }
    }

    private fun goToRegion(regionId: Int) {
        val intent = Intent(this, PokemonesRegion::class.java)
        intent.putExtra("REGION_ID", regionId)
        startActivity(intent)
    }
}

@Composable
fun PokedexScreen(
    regions: List<Region>,
    modifier: Modifier = Modifier,
    onClickRegion: (Int) -> Unit
) {
    PokedexTheme {
        Scaffold(
            topBar = {
                Text(text = stringResource(id = R.string.app_name))
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                LazyColumn {
                    items(
                        items = regions,
                        key = { it.url.split("/").last().toInt() }
                    ) { region ->
                        Column {
                            Row {
                                Text(text = region.name.replaceFirstChar { it.uppercase() })
                            }
                            Button(onClick = { onClickRegion(region.url.split("/").last().toInt()) }) {
                                Text(text = stringResource(id = R.string.go_to_region))
                            }
                        }
                    }
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexScreen(
        regions = emptyList(),
        onClickRegion = {}
    )
}
