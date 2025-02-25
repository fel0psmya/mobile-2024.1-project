package com.example.mygamingdatabase.ui.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mygamingdatabase.ui.components.LoadingIndicator
import com.example.mygamingdatabase.data.GameRepository
import com.example.mygamingdatabase.data.models.Game
import com.example.mygamingdatabase.data.models.GameStatus
import com.example.mygamingdatabase.data.models.statusDescriptions
import com.example.mygamingdatabase.isInternetAvailable
import com.example.mygamingdatabase.ui.components.MaintenanceDropdownMenu
import com.example.mygamingdatabase.ui.components.MaintenanceItemDialog
import com.example.mygamingdatabase.ui.components.NetworkWarning
import com.example.mygamingdatabase.viewmodel.GameViewModel
import com.example.mygamingdatabase.viewmodel.GameViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun ListsScreen(
    navController: NavHostController,
    context: Context = LocalContext.current,
    repository: GameRepository = GameRepository()
){
    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(context, repository)
    )

    var isConnected by remember { mutableStateOf(true) }

    val games by viewModel.games
    var userId by remember { mutableStateOf("Carregando...") }
    var favoriteGames by remember { mutableStateOf<List<Game>>(emptyList()) }
    var gameList by remember { mutableStateOf<List<Game>>(emptyList()) }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Favoritos, 1: Minha Lista
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isConnected = isInternetAvailable(context)
        if(isConnected) {
            isLoading = true
            viewModel.fetchGames()
            Log.d("ListsScreen", "Games loaded: $games")
            if (viewModel.isUserLogged()) {
                viewModel.getUserId { id ->
                    if (id != null) {
                        Log.d("ListsScreen", "User ID: $id")
                        userId = id

                        viewModel.carregarJogosFavoritos(id) { favoriteIds ->
                            Log.d("ListsScreen", "Favorite Game IDs: $favoriteIds")
                            viewModel.fetchGamesByIds(favoriteIds) { fetchedGames ->
                                favoriteGames = fetchedGames
                                Log.d("ListsScreen", "Favorite Games: $favoriteGames")
                            }
                        }

                        viewModel.carregarJogosNaLista(userId) { list ->
                            val gameIds = list.map { it.id } // Extrai apenas os IDs da lista de jogos

                            viewModel.fetchGamesByIds(gameIds) { fetchedGames ->
                                gameList = fetchedGames
                                Log.d("ListsScreen", "Game list: $gameList")
                            }
                        }
                    }
                }
            }
            delay(4500)
            isLoading = false
        }
    }

    var dropdownExpanded by remember { mutableStateOf(false) }

    if (isLoading) {
        LoadingIndicator()
    } else if (!isConnected) {
        NetworkWarning()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // TabRow
            TabRow(selectedTabIndex = selectedTab, modifier = Modifier.fillMaxWidth()) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Favoritos") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Minha Lista") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: Refazer a função de filtrar jogos
            /*if (selectedTab == 1) {
                // Botão que, ao ser clicado, abre um dropdown menu para selecionar filtrar a lista por status
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            dropdownExpanded = true
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Filtrar",
                        modifier = Modifier
                            .padding(8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = selectedStatus ?: "Filtrar por status",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todos") },
                            onClick = {
                                selectedStatus = null
                                dropdownExpanded = false
                            }
                        )
                        statusDescriptions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.value) },
                                onClick = {
                                    selectedStatus = status.value
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            val selectedGameStatus = statusDescriptions.entries.find { it.value == selectedStatus }?.key
            val filteredGameList = remember(gameList, selectedStatus) {
                gameList.filter { game ->
                    Log.d("ListsScreen", "${game.status} | $selectedGameStatus")
                    selectedStatus == null || game.status == selectedGameStatus
                }
            }*/

            // LazyVerticalGrid para exibir 2 jogos por linha
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val gamesToDisplay = if (selectedTab == 0) favoriteGames else gameList

                // Exibe os jogos se houver
                if (gamesToDisplay.isNotEmpty()) {
                    items(gamesToDisplay) { game ->
                        GameCard(
                            navController = navController,
                            game = game,
                            isFavorite = selectedTab == 0,
                            onRemoveFavorite = { gameId ->
                                favoriteGames = favoriteGames.filter { it.id != gameId }
                                Toast.makeText(context, "${game.name} foi removido dos seus favoritos", Toast.LENGTH_SHORT).show()
                                viewModel.removerJogoFavorito(userId, gameId)
                            },
                            isAddedToList = selectedTab == 1,
                            onRemoveGameFromList = {
                                gameList = gameList.filter { it.id != game.id }
                                Toast.makeText(context, "${game.name} foi removido da sua lista", Toast.LENGTH_SHORT).show()
                                viewModel.removerJogoDaLista(userId, game.id)
                            }
                        )
                    }
                }
            }

            if (selectedTab == 0 && favoriteGames.isEmpty() || selectedTab == 1 && gameList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTab == 0) "Você ainda não tem jogos favoritos." else "Sua lista está vazia.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center // Garante alinhamento central do texto
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun GameCard(
    navController: NavHostController,
    game: Game,
    isFavorite: Boolean,
    onRemoveFavorite: (Int) -> Unit,
    isAddedToList: Boolean,
    onRemoveGameFromList: (Int) -> Unit,
) {
    var selectedGameId by remember { mutableStateOf<Int?>(null) }
    var dropdownExpandedByGameId by remember { mutableStateOf<Int?>(null) }
    var showRemoveFavoriteDialog by remember { mutableStateOf(false) }
    var gameToRemoveFromFavorites by remember { mutableStateOf<Int?>(null) }



    // Mostrar o diálogo de manutenção do item
    if (selectedGameId == game.id) {
        MaintenanceItemDialog(
            game = game,
            onDismiss = { selectedGameId = null },
            onSave = { updatedGame ->
                game.status = updatedGame.status
                game.userScore = updatedGame.userScore

                // TODO
                // viewModel.salvarJogoNaLista()
                selectedGameId = null
            }
        )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .aspectRatio(3f / 4f)
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
                Column (modifier = Modifier.fillMaxHeight().align(Alignment.CenterVertically)) {
                    game.name?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    /*Text(
                        text = if (gameList.any { it.id == game.id }) "Nota: ${gameList[game.id].userScore}" else "",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )*/
                }
            }

            // IconButton no canto superior direito dependendo da tab
            if (isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(38.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clickable {
                            gameToRemoveFromFavorites = game.id
                            showRemoveFavoriteDialog = true
                        }
                )
            } else {
                // Botão "Adicionar à Lista"
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Adicionado à Lista",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(38.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clickable {
                            dropdownExpandedByGameId = game.id // Alterna o valor do dropdown
                        }
                )
            }

            if (isAddedToList && dropdownExpandedByGameId == game.id) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    MaintenanceDropdownMenu(
                        game = game,
                        expanded = dropdownExpandedByGameId == game.id,
                        onDismissRequest = { dropdownExpandedByGameId = null },
                        onEdit = {
                            dropdownExpandedByGameId = null
                            selectedGameId = game.id
                        },
                        onRemove = {
                            dropdownExpandedByGameId = null
                            onRemoveGameFromList(game.id)
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
                                onRemoveFavorite(game.id)
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

    Spacer(modifier = Modifier.height(12.dp))
}