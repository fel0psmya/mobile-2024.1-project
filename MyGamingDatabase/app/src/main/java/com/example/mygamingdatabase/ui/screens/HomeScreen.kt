package com.example.mygamingdatabase.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.mygamingdatabase.models.Game
import com.example.mygamingdatabase.models.gameList
import com.example.mygamingdatabase.ui.components.MaintenanceDropdownMenu
import com.example.mygamingdatabase.ui.components.MaintenanceItemDialog


@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavHostController,
    context: Context,
    onGameSelected: (Game) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedImageUrl by remember { mutableStateOf<String?>(null) } // To store expanded image's URL

    var selectedGameId by remember { mutableStateOf<Int?>(null) }
    var dropdownExpandedByGameId by remember { mutableStateOf<Int?>(null) }
    var showRemoveFavoriteDialog by remember { mutableStateOf(false) }
    var gameToRemoveFromFavorites by remember { mutableStateOf<Int?>(null) }

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
                    // Mostrar o diálogo de manutenção do item
                    if (selectedGameId == game.id) {
                        MaintenanceItemDialog(
                            game = game,
                            onDismiss = { selectedGameId = null },
                            onSave = { updatedGame ->
                                game.status = updatedGame.status
                                game.userScore = updatedGame.userScore
                                if (!game.isAddedToList.value) {
                                    game.isAddedToList.value = true
                                }
                                selectedGameId = null
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(game.imageUrl),
                            contentDescription = game.name,
                            modifier = Modifier
                                .size(175.dp)
                                .align(Alignment.CenterVertically)
                                .fillMaxHeight()
                                .clickable {
                                    expandedImageUrl =
                                        game.imageUrl  // Define expanded image URL
                                }
                        )

                        // Game details
                        Column (modifier = Modifier.padding(end = 22.dp, top = 28.dp, bottom = 28.dp)) {
                            // Game name, release date and favorite icon
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
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
                                            if (!game.isFavorite.value) {
                                                game.isFavorite.value = !game.isFavorite.value
                                            } else {
                                                gameToRemoveFromFavorites = game.id
                                                showRemoveFavoriteDialog = true
                                            }
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

                            Spacer(modifier = Modifier.height(8.dp))

                            // Botão "Adicionar à Lista"
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .height(48.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        if (game.isAddedToList.value) MaterialTheme.colorScheme.primary
                                        else Color.Transparent // Fundo transparente se não adicionado
                                    )
                                    .clickable {
                                        if (game.isAddedToList.value) {
                                            dropdownExpandedByGameId = game.id // Alterna o valor do dropdown
                                        } else {
                                            selectedGameId = game.id
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (!game.isAddedToList.value) {
                                    Icon(
                                        imageVector = Icons.Filled.BookmarkBorder,
                                        contentDescription = "Adicionar",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Adicionar à Lista",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.Bookmark,
                                        contentDescription = "Adicionado",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text ="Adicionado",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                            }

                            if (game.isAddedToList.value && dropdownExpandedByGameId == game.id) {
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    MaintenanceDropdownMenu(
                                        game = game,
                                        expanded = if (dropdownExpandedByGameId == game.id) { true } else { false },
                                        onDismissRequest = { dropdownExpandedByGameId = null },
                                        onEdit = {
                                            dropdownExpandedByGameId = null
                                            selectedGameId = game.id
                                        },
                                        onRemove = {
                                            dropdownExpandedByGameId = null
                                            game.isAddedToList.value = false
                                        }
                                    )
                                }
                            }

                            if(showRemoveFavoriteDialog && gameToRemoveFromFavorites == game.id) {
                                AlertDialog(
                                    onDismissRequest = { showRemoveFavoriteDialog = false },
                                    title = {
                                        Text(text = "Remover jogo dos favoritos")
                                    },
                                    text = {
                                        Text("Você tem certeza que deseja remover '${game.name}' dos seus favoritos?")
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                showRemoveFavoriteDialog = false
                                                game.isFavorite.value = !game.isFavorite.value
                                            }
                                        ) {
                                            Text("Confirmar")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                showRemoveFavoriteDialog = false
                                                gameToRemoveFromFavorites = null
                                            }) {
                                            Text("Cancelar")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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