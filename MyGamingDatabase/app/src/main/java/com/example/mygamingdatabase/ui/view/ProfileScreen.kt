package com.example.mygamingdatabase.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.app.ui.components.LoadingIndicator
import com.example.mygamingdatabase.R
import com.example.mygamingdatabase.data.GameRepository
import com.example.mygamingdatabase.data.models.Game
import com.example.mygamingdatabase.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    viewModel: GameViewModel,
    navController: NavController
) {
    val profileImage by remember { mutableStateOf<Painter?>(null) }
    var userName by remember { mutableStateOf("Carregando...") }
    var userId by remember { mutableStateOf("") }

    val games by viewModel.games
    var favoriteGames by remember { mutableStateOf<List<Game>>(emptyList()) }
    var gameList by remember { mutableStateOf<List<Game>>(emptyList()) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        viewModel.fetchGames()
        if (viewModel.isUserLogged()) {
            viewModel.getUserName { name ->
                userName = name ?: "Usuário"
            }
            viewModel.getUserId { id ->
                if (id != null) {
                    userId = id
                    viewModel.carregarJogosFavoritos(id) { favoriteIds ->
                        Log.d("ListsScreen", "Favorite Game IDs: $favoriteIds")
                        viewModel.fetchGamesByIds(favoriteIds) { fetchedGames ->
                            favoriteGames = fetchedGames
                            Log.d("ListsScreen", "Favorite Games: $favoriteGames")
                        }
                    }
                }
            }
        }
        delay(1000)
        isLoading = false
    }

    if (isLoading) {
        LoadingIndicator()
    } else {
        if (!viewModel.isUserLogged()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Você não está logado, entre aqui:",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate("login")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Entrar")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                // Imagem de perfil (decidir se será implementado)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            // TODO: Implement image picker from gallery
                        }
                ) {
                    if (profileImage != null) {
                        Image(
                            painter = profileImage!!,
                            contentDescription = "Profile Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_profile_placeholder),
                            contentDescription = "Profile Placeholder",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Decidir se usará username

                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Jogos Favoritos",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                HorizontalDivider()
                // Implementar lazy row
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favoriteGames) { game ->
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .padding(top = 4.dp, bottom = 8.dp)
                                .aspectRatio(3f / 4f)
                                .align(Alignment.Start)
                                .clickable {
                                    navController.navigate("gameDetails/${game.id}")
                                },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = rememberAsyncImagePainter(game.imageUrl),
                                    contentDescription = game.name,
                                    modifier = Modifier
                                        .matchParentSize(),
                                    contentScale = ContentScale.Crop
                                )

                                // Row na parte inferior com fundo preto transparente
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp)
                                        .align(Alignment.BottomStart)
                                        .background(Color.Black.copy(alpha = 0.6f)) // Fundo preto semi-transparente
                                        .padding(8.dp)
                                ) {
                                    Column(modifier = Modifier.fillMaxHeight()) {
                                        game.name?.let {
                                            Text(
                                                text = it,
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Lista de Jogos",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                HorizontalDivider()
                // Implementar lazy row
            }
        }
    }
}