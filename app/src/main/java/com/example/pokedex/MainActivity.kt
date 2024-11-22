package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.pokedex.services.models.Region
import com.example.pokedex.ui.theme.PokedexColors
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
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = PokedexColors.DarkGray.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
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
    Scaffold(
        modifier = Modifier
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = { PokedexHeader() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = PokedexColors.PrimaryRed) // Fondo rojo
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .background(PokedexColors.DarkGray, shape = RoundedCornerShape(16.dp)) // Fondo gris oscuro
                    .padding(16.dp)
            ) {
                items(
                    items = regions,
                    key = { it.url.split("/").last().toInt() }
                ) { region ->
                    RegionItem(region, onClickRegion)
                }
            }
        }
    }
}

@Composable
fun PokedexHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PokedexColors.DarkGray) // Fondo gris oscuro
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pokédex",
            color = PokedexColors.Gold, // Amarillo
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun RegionItem(region: Region, onClickRegion: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(PokedexColors.LightGray, shape = RoundedCornerShape(12.dp)) // Fondo gris claro
            .padding(16.dp),
    ) {
        Text(
            text = region.name.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontSize = 40.sp,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = { onClickRegion(region.url.split("/").last().toInt()) },
            colors = ButtonDefaults.buttonColors(containerColor = PokedexColors.Blue) // Azul
        ) {
            Text(
                text = "Ir a región",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexScreen(
        regions = listOf(
            Region("Kanto", "https://pokeapi.co/api/v2/pokedex/2"),
            Region("Johto", "https://pokeapi.co/api/v2/pokedex/3")
        ),
        onClickRegion = {}
    )
}

