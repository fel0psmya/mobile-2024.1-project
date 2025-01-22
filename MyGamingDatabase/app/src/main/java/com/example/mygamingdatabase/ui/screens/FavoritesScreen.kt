package com.example.mygamingdatabase.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.mygamingdatabase.models.gameList

@Composable
fun FavoritesScreen(navController: NavHostController){
    // Observe the changes in the favorite state dynamically
    val favoriteGames = gameList.filter { it.isFavorite.value }

    var expandedImageUrl by remember { mutableStateOf<String?>(null) } // To store expanded image's URL

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        if (favoriteGames.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Você ainda não tem jogos favoritos.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        } else {
            // Displays a list of favorites
            items(favoriteGames) { game ->
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))



    expandedImageUrl?.let { imageUrl ->
        ExpandedImageDialog(imageUrl = imageUrl, onClose = { expandedImageUrl = null })
    }
}