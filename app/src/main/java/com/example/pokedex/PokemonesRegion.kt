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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.pokedex.services.models.Region
import com.example.pokedex.ui.theme.PokedexTheme

class PokemonesRegion : ComponentActivity() {
    private val driverAdapter = PokemonDriverAdapter()

    // Lista de regiones predefinidas con sus IDs
    private val regions = listOf(
        Region("National", "https://pokeapi.co/api/v2/pokedex/1"),
        Region("Kanto", "https://pokeapi.co/api/v2/pokedex/2"),
        Region("Johto", "https://pokeapi.co/api/v2/pokedex/3"),
        Region("Hoenn", "https://pokeapi.co/api/v2/pokedex/4"),
        Region("Sinnoh", "https://pokeapi.co/api/v2/pokedex/5"),
        Region("Unova", "https://pokeapi.co/api/v2/pokedex/8"),
        Region("Conquest-Gallery", "https://pokeapi.co/api/v2/pokedex/11"),
        Region("Kalos-Central", "https://pokeapi.co/api/v2/pokedex/12"),
        Region("Kalos-Coastal", "https://pokeapi.co/api/v2/pokedex/13"),
        Region("Kalos-Mountain", "https://pokeapi.co/api/v2/pokedex/14"),
        Region("Alola", "https://pokeapi.co/api/v2/pokedex/21"),
        Region("Galar", "https://pokeapi.co/api/v2/pokedex/27"),
        Region("Hisui", "https://pokeapi.co/api/v2/pokedex/30"),
        Region("Paldea", "https://pokeapi.co/api/v2/pokedex/31"),
        Region("Blueberry", "https://pokeapi.co/api/v2/pokedex/32"),
        Region("Kitakami", "https://pokeapi.co/api/v2/pokedex/33"),

    )
//FILTRO PARA QUE SE MUESTREN SOLAMENTE LOS POKEMONES DE ESA REGION
    private val regionRanges = mapOf(
        "Kanto" to 1..151,
        "Johto" to 152..251,
        "Hoenn" to 252..386,
        "Sinnoh" to 387..493,
        "Unova" to 494..649,
        "Conquest-Gallery" to 493..493,
        "Kalos-Central" to 650..721,
        "Kalos-Coastal" to 650..721,
        "Kalos-Mountain" to 650..721,
        "Alola" to 722..809,
        "Galar" to 810..898,
        "Hisui" to 899..905,
        "Paldea" to 906..1008,
        "Kitakami" to 1009..1010,
        "Blueberry" to 1011..1017,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val regionId = intent.getIntExtra("REGION_ID", 0)
        val regionName =
            regions.find { it.url.split("/").last().toInt() == regionId }?.name ?: "Desconocida"

        setContent {
            PokedexTheme {
                val regionPokemonList = remember { mutableStateOf<List<PokemonEntry>>(emptyList()) }
                val filteredPokemonList = remember { mutableStateOf<List<PokemonEntry>>(emptyList()) }
                val types = remember { mutableStateOf(listOf(
                    "Todos","normal", "fighting", "flying", "poison", "ground", "rock", "bug", "ghost", "steel", "fire", "water", "grass", "electric",
                    "psychic", "ice", "dragon", "dark", "fairy","stellar","unknown")) }
                val selectedType = remember { mutableStateOf(types.value.first()) }
                val searchQuery = remember { mutableStateOf("") }

                driverAdapter.PokemonsByRegion(
                    region = regionId.toString().lowercase(),
                    loadData = {
                        val range = regionRanges[regionName]
                        if (range != null) {
                            val filteredPokemon = it.filter { pokemon ->
                                val entryNumber = extractPokemonNumber(pokemon.pokemon_species.url)
                                entryNumber in range
                            }
                            regionPokemonList.value = filteredPokemon
                            filteredPokemonList.value = filteredPokemon
                        } else {
                            regionPokemonList.value = it
                            filteredPokemonList.value = it
                        }
                    },
                    errorData = {
                        println("Error al cargar Pokémon de la región.")
                    }
                )

                val filterByType: (String) -> Unit = { type ->
                    selectedType.value = type
                    if (type == "Todos") {
                        filteredPokemonList.value = regionPokemonList.value
                    } else {
                        driverAdapter.PokemonsByType(
                            type = type,
                            loadData = { typePokemonList ->
                                val filtered = regionPokemonList.value.filter { regionPokemon ->
                                    val pokemonNumber = extractPokemonNumber(regionPokemon.pokemon_species.url)
                                    typePokemonList.any { it.entry_number == pokemonNumber }
                                }
                                filteredPokemonList.value = filtered
                            },
                            errorData = {
                                println("Error al cargar Pokémon por tipo.")
                            }
                        )
                    }
                }
                val filterBySearch: (String) -> Unit = { query ->
                    searchQuery.value = query
                    filteredPokemonList.value = regionPokemonList.value.filter { pokemon ->
                        pokemon.pokemon_species.name.contains(query, ignoreCase = true)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Text(
                            text = "Pokemones de la región: $regionName",
                            modifier = Modifier.padding(16.dp)
                        )
                    }) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        TypeSelector(
                            types = types.value,
                            onTypeSelected = { type ->
                                filterByType(type)
                            }
                        )
                        SearchBar(
                            query = searchQuery.value,
                            onQueryChange = { query ->
                                filterBySearch(query)
                            }
                        )

                        PokemonList(
                            pokemonEntries = filteredPokemonList.value,
                            modifier = Modifier.padding(innerPadding),
                            onClickPokemon = {
                                val intent = Intent(this@PokemonesRegion, InfoPokemon::class.java)
                                intent.putExtra("POKEMON_ID", it)
                                startActivity(intent)
                            }
                        )
                    }
                }
            }
        }

    }
    @Composable
    fun SearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        androidx.compose.material3.TextField(
            value = query,
            onValueChange = { newText ->
                onQueryChange(newText)
            },
            label = { Text("Buscar Pokémon") },
            modifier = modifier.padding(16.dp)
        )
    }

    @Composable
    fun TypeSelector(
        types: List<String>,
        onTypeSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val expanded = remember { mutableStateOf(false) }
        val selectedType = remember { mutableStateOf(types.firstOrNull() ?: "Seleccionar tipo") }

        Column(modifier = modifier) {
            Button(onClick = { expanded.value = true }) {
                Text(text = selectedType.value.replaceFirstChar { it.uppercase() })
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(text = type.replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            expanded.value = false
                            selectedType.value = type
                            onTypeSelected(type)
                        }
                    )
                }
            }
        }
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
                            val pokemonNumber = extractPokemonNumber(pokemon.pokemon_species.url)
                            Text(text = "ID: $pokemonNumber ")

                        }
                        Row {
                            Text(text = "Nombre: ${pokemon.pokemon_species.name.replaceFirstChar { it.uppercase() }}")
                        }

                        Button(onClick = {
                            val pokemonNumber = extractPokemonNumber(pokemon.pokemon_species.url)
                            onClickPokemon(pokemonNumber.toString())
                        }) {

                            Text(text = "Ver detalles")
                        }
                    }
                }
            }
        }
    }

    private fun extractPokemonNumber(url: String): Int {
        return url.trimEnd('/').split('/').last().toInt()
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        PokedexTheme {
            TypeSelector(types = listOf("fire", "water", "grass"), onTypeSelected = {})
            PokemonList(
                pokemonEntries = emptyList(),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}