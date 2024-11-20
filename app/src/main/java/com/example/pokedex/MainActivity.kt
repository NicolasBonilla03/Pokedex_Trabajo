package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokedex.services.controllers.RegionService
import com.example.pokedex.services.driverAdapters.PokemonDriverAdapter
import com.example.pokedex.services.models.Region
import com.example.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private val regions = listOf(
        Region("Kanto", "https://pokeapi.co/api/v2/pokedex/2/", 2),
        Region("Johto", "https://pokeapi.co/api/v2/pokedex/3/", 3),
        Region("Hoenn", "https://pokeapi.co/api/v2/pokedex/4/", 4),
        Region("Sinnoh", "https://pokeapi.co/api/v2/pokedex/5/", 5),
        Region("Unova", "https://pokeapi.co/api/v2/pokedex/8/", 8),
        Region("Kalos-Central", "https://pokeapi.co/api/v2/pokedex/12/", 12),
        Region("Kalos-Coastal", "https://pokeapi.co/api/v2/pokedex/13/", 13),
        Region("Kalos-Mountain", "https://pokeapi.co/api/v2/pokedex/14/", 14),
        Region("Alola", "https://pokeapi.co/api/v2/pokedex/16/", 16),
        Region("Galar", "https://pokeapi.co/api/v2/pokedex/galar/", 27),  // Galar region
        Region("Hisui", "https://pokeapi.co/api/v2/pokedex/hisui/", 30),  // Hisui region
        Region("Paldea", "https://pokeapi.co/api/v2/pokedex/paldea/", 31))

    private val regionService by lazy { RegionService() }

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
        val intent = Intent(this, Pokemones_Region::class.java)
        intent.putExtra("REGION_ID", regionId) // Pasar el ID de la región
        startActivity(intent)
    }
}

@Composable
fun PokedexScreen(
    regions: List<Region>,
    modifier: Modifier = Modifier,
    onClickRegion: (Int) -> Unit // Cambiar a Int para manejar el ID
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
                        key = { it.id } // Usar el ID como clave única
                    ) { region ->
                        Column {
                            Row {
                                Text(text = region.name.capitalizeFirstLetter())
                            }
                            Button(onClick = { onClickRegion(region.id) }) { // Pasar el ID de la región
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
