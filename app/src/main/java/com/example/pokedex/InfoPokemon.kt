package com.example.pokedex

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import com.example.pokedex.services.driverAdapters.PokemonDriverAdapter
import com.example.pokedex.services.models.EvolutionChain
import com.example.pokedex.services.models.EvolutionChainDetail
import com.example.pokedex.services.models.PokemonInfo
import com.example.pokedex.services.models.PokemonSpeciesInfo
import com.example.pokedex.ui.theme.PokedexColors
import com.example.pokedex.ui.theme.PokedexTheme

// Nueva paleta de colores locales
val localColorPrimary = Color(0xFF00695C) // Verde local
val localColorSecondary = Color(0xFF00363A) // Azul oscuro
val localColorBackground = Color(0xFF81C784) // Verde claro

@Suppress("DEPRECATION")
class InfoPokemon : ComponentActivity() {
    private val driverAdapter = PokemonDriverAdapter()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val pokemonId = intent.getStringExtra("POKEMON_ID")

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = PokedexColors.DarkGray.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = false
            PokedexTheme {
                val pokemonInfo = remember { mutableStateOf<PokemonInfo?>(null) }
                val speciesInfo = remember { mutableStateOf<PokemonSpeciesInfo?>(null) }
                val evolutionChain = remember { mutableStateOf<EvolutionChain?>(null) }

                driverAdapter.PokemonInfo(
                    nameOrId = pokemonId ?: "",
                    loadData = { pokemonInfo.value = it },
                    errorData = { println("Error al cargar los detalles del Pokémon.") }
                )

                driverAdapter.PokemonSpeciesInfo(
                    nameOrId = pokemonId ?: "",
                    loadData = { speciesInfo.value = it },
                    errorData = { println("Error al cargar la información de la especie.") }
                )
                if (speciesInfo.value != null && evolutionChain.value == null) {
                    val evolutionId = speciesInfo.value?.evolution_chain?.url?.split("/")?.getOrNull(6)?.toInt()
                    if (evolutionId != null) {
                        driverAdapter.getEvolutionChain(
                            id = evolutionId,
                            loadData = { evolutionChain.value = it },
                            errorData = { println("Error al cargar la cadena de evolución.") }
                        )
                    }
                }

                Scaffold(
                    topBar = {
                        TopBarWithBackButton(
                            title = "Detalles del Pokémon",
                            onBackClick = { finish() } // Cierra la actividad
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) {
                    InfoScreen(
                        pokemonInfo = pokemonInfo.value,
                        speciesInfo = speciesInfo.value,
                        evolChain = evolutionChain.value,

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
    evolChain: EvolutionChain?,
) {
    if (pokemonInfo == null) {
        Text(
            text = "Cargando detalles del Pokémon...",
            style = MaterialTheme.typography.bodyLarge,
            color = PokedexColors.Gold, // Amarillo para el texto
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(PokedexColors.PrimaryRed) // Fondo rojo
                .padding(
                    top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 46.dp, // Desplaza hacia abajo
                    bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                )
        ) {
            // Tarjeta: Nombre e Imágenes
            item {
                CardWithPadding(
                    backgroundColor = PokedexColors.DarkGray, // Gris oscuro
                    contentColor = Color.White
                ) {
                    Text(
                        text = pokemonInfo.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AsyncImage(
                            model = pokemonInfo.sprites.front_default,
                            contentDescription = "Imagen del Pokémon",
                            modifier = Modifier.size(120.dp)
                        )
                        AsyncImage(
                            model = pokemonInfo.sprites.front_shiny,
                            contentDescription = "Imagen shiny del Pokémon",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }
            }

            // Tarjeta: Información Básica
            item {
                CardWithPadding(
                    backgroundColor = PokedexColors.Blue, // Azul
                    contentColor = Color.White
                ) {
                    Text(
                        text = "Información Básica",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    InfoRow(label = "ID", value = pokemonInfo.id.toString())
                    InfoRow(label = "Altura", value = "${pokemonInfo.height * 10} cm")
                    InfoRow(label = "Peso", value = "${pokemonInfo.weight / 10.0} kg")
                    InfoRow(
                        label = "Tipo",
                        value = pokemonInfo.types.joinToString { it.type.name.replaceFirstChar { it.uppercase() } }
                    )
                    InfoRow(
                        label = "Habilidades",
                        value = pokemonInfo.abilities.joinToString { it.ability.name.replaceFirstChar { it.uppercase() } }
                    )
                }
            }

            // Tarjeta: Estadísticas
            item {
                CardWithPadding(
                    backgroundColor = PokedexColors.LightGray, // Gris claro
                    contentColor = Color.Black
                ) {
                    Text(
                        text = "Estadísticas",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    pokemonInfo.stats.forEach { stat ->
                        Text(
                            text = "${stat.stat.name.replaceFirstChar { it.uppercase() }}: ${stat.base_stat}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }

            // Tarjeta: Descripción
            item {
                CardWithPadding(
                    backgroundColor = PokedexColors.DarkGray, // Gris oscuro
                    contentColor = Color.White
                ) {
                    Text(
                        text = "Acerca de:",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (speciesInfo != null) {
                        val flavorText = speciesInfo.flavor_text_entries
                            .firstOrNull { it.language.name == "es" }
                            ?.flavor_text
                            ?.replace("\n", " ")
                            ?: "No description available."
                        Text(
                            text = flavorText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = "Cargando información adicional...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            if (evolChain != null) {
                item {
                    CardWithPadding(
                        backgroundColor = PokedexColors.LightGray, // Gris claro
                        contentColor = Color.Black
                    ){
                    EvolutionChainDisplay(evolChain)
                }
                }
            }
        }
    }
}

@Composable
fun CardWithPadding(
    backgroundColor: Color,
    contentColor: Color,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TopBarWithBackButton(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(localColorPrimary) // Fondo verde local
            .padding(16.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        // Botón de volver
        androidx.compose.material3.Button(
            onClick = onBackClick,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = localColorSecondary, // Fondo azul oscuro
                contentColor = Color.White           // Texto blanco
            ),
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text("← Volver", style = MaterialTheme.typography.bodyMedium)
        }

        // Título
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun EvolutionChainDisplay(evolChain: EvolutionChain) {
    Column {
        Text(
            text = "Evoluciones: ",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        RenderEvolutionChain(chain = evolChain.chain)
    }
}
@Composable
fun RenderEvolutionChain(chain: EvolutionChainDetail) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = chain.species.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        AsyncImage(
            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getPokemonIdFromUrl(chain.species.url)}.png",
            contentDescription = "Imagen de ${chain.species.name}",
            modifier = Modifier.size(60.dp)
        )
        androidx.compose.material3.Button(
            onClick = {
                val intent = Intent(context, InfoPokemon::class.java).apply {
                    putExtra("POKEMON_ID", getPokemonIdFromUrl(chain.species.url).toString())
                }
                context.startActivity(intent)
            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("Ver")
        }
    }

    chain.evolves_to?.forEach { nextChain ->
        RenderEvolutionChain(chain = nextChain)
    }
}


fun getPokemonIdFromUrl(url: String): Int {
    return url.split("/").getOrNull(6)?.toInt() ?: 0
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    PokedexTheme {
        InfoScreen(
            pokemonInfo = null,
            speciesInfo = null,
            evolChain = null,

        )
}
}
