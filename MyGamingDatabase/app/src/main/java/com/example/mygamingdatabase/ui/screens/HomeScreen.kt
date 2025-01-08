package com.example.mygamingdatabase.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    var expandedImageUrl by remember { mutableStateOf<String?>(null) } // To store expanded image's URL

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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(filteredGames) { game ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navController.navigate("gameDetails/${game.id}")
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(game.imageUrl),
                            contentDescription = game.name,
                            modifier = Modifier
                                .size(150.dp)
                                .fillMaxHeight()
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    expandedImageUrl = game.imageUrl  // Define expanded image URL
                                }
                        )

                        // Game details
                        Column (modifier = Modifier.padding(end = 22.dp, top = 28.dp, bottom = 28.dp)) {
                            // Game name, release date and favorite icon
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column (modifier = Modifier.weight(1f)) {
                                    // Game name
                                    Text(
                                        text = game.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
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

                            //Spacer(modifier = Modifier.height(8.dp))

                            // Platforms
                            game.platforms?.let {
                                Text(
                                    text = it.joinToString(", "), // Exibe as plataformas separadas por vírgula
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 8.dp),
                                )
                            }

                            // Game description
                            Text(
                                text = game.description,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    expandedImageUrl?.let { imageUrl ->
        ExpandedImageDialog(imageUrl = imageUrl, onClose = { expandedImageUrl = null })
    }
}

@Composable
fun ExpandedImageDialog(imageUrl: String, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f))
        ) {

            // Expanded image
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Expanded Image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            )

            // Close button
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}