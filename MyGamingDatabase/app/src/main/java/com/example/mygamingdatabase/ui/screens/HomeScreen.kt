package com.example.mygamingdatabase.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mygamingdatabase.models.Game
import com.example.mygamingdatabase.models.gameList
import com.example.mygamingdatabase.ui.components.TopBar

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavHostController,
    context: Context,
    onGameSelected: (Game) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredGames = remember(searchQuery) {
        gameList.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val recentSearches = remember { mutableListOf<Game>() }

    Column {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Pesquisar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        )

        // LazyRow to Recent Searches
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recentSearches) { game ->
                Button(onClick = { onGameSelected(game) }) {
                    Text(game.name)
                }
            }
        }

        // LazyColumn to Filtered Games List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(filteredGames) { game ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("gameDetails/${game.id}")
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(game.imageUrl),
                            contentDescription = game.name,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .size(130.dp)
                                .fillMaxHeight()
                        )

                        // Game details
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            // Game name, release date and favorite icon
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column (modifier = Modifier.weight(1f)) {
                                    // Game name
                                    Text(
                                        text = game.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    // Release date
                                    Text(
                                        text = game.releaseDate,
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Favorite icon
                                Icon(
                                    imageVector = if (game.isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            game.isFavorite.value = !game.isFavorite.value // Update favorite state
                                        }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Platforms
                            game.platforms?.let {
                                Text(
                                    text = it.joinToString(", "), // Exibe as plataformas separadas por vírgula
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            // Game description
                            Text(
                                text = game.description,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}