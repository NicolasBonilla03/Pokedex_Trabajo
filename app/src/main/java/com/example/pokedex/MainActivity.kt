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
import com.example.pokedex.services.driverAdapters.PokemonDriverAdapter
import com.example.pokedex.services.models.Region
import com.example.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    private val pokemonDriverAdapter by lazy { PokemonDriverAdapter() }
    //val pokemonViewModel by lazy { PokemonViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var regions by remember { mutableStateOf(emptyList<Region>()) }
            var loadProducts by remember { mutableStateOf(false) }

            if (!loadProducts) {

                this.pokemonDriverAdapter.allRegions(
                    loadData = { regionsList ->
                        println("okkk")
                        println(regionsList)
                        regions = regionsList
                        loadProducts = true
                    },
                    errorData = {
                        println("Error en el servicio")
                        loadProducts = true
                    }
                )


            }
            PokedexScreen(regions = regions, onClickRegion = { goToRegion(it) })
        }
    }

    private fun goToRegion(regionName: String) {
        val intent = Intent(this, Pokemones_Region::class.java)
        intent.putExtra("REGION_NAME", regionName) // Pasar la región seleccionada
        startActivity(intent)
    }

}


@Composable
fun PokedexScreen(
    regions: List<Region>,
    onClickRegion: (String) -> Unit
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
                        key = { it.name }
                    ) { region ->
                        Column {
                            Row {
                                Text(text = stringResource(id = R.string.title))
                                Text(text = region.name)
                            }
                            Button(onClick = { onClickRegion(region.name) }) { // Pasar el nombre de la región
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
