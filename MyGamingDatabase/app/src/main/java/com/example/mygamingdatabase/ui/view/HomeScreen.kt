package com.example.mygamingdatabase.ui.view

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mygamingdatabase.ui.components.MaintenanceDropdownMenu
import com.example.mygamingdatabase.ui.components.MaintenanceItemDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.scale
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygamingdatabase.ui.components.LoadingIndicator
import com.example.mygamingdatabase.data.models.Game
import com.example.mygamingdatabase.data.models.GameStatus
import com.example.mygamingdatabase.isInternetAvailable
import com.example.mygamingdatabase.ui.components.NetworkWarning
import com.example.mygamingdatabase.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController: NavHostController,
    context: Context,
    viewModel: GameViewModel = viewModel(),
    onGameSelected: (Game) -> Unit,
) {
    var isConnected by remember { mutableStateOf(true) }

    var searchQuery by remember { mutableStateOf("") }
    var expandedImageUrl by remember { mutableStateOf<String?>(null) } // To store expanded image's URL

    var selectedGameId by remember { mutableStateOf<Int?>(null) }
    var dropdownExpandedByGameId by remember { mutableStateOf<Int?>(null) }
    var showRemoveFavoriteDialog by remember { mutableStateOf(false) }
    var gameToRemoveFromFavorites by remember { mutableStateOf<Int?>(null) }

    var userId by remember { mutableStateOf("Carregando...") }
    var favoriteGameIds by remember { mutableStateOf<List<Int>>(emptyList()) }


    val games by viewModel.games
    val filteredGames = remember(games, searchQuery) {
        games.filter { it.name?.contains(searchQuery, ignoreCase = true) ?: false }
    }
    val recentSearches = remember { mutableListOf<Game>() }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Controle do lembrete para notificações
    var showReminderDialog by remember { mutableStateOf(false) }
    var selectedGameIdForReminder by remember { mutableStateOf<Int?>(null) } // Identifica qual jogo requer o lembrete
    var doNotAskAgain by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    // Fetch games
    LaunchedEffect(Unit) {
        isConnected = isInternetAvailable(context)
        if(isConnected) {
            isLoading = true
            viewModel.fetchGames()
            if (viewModel.isUserLogged()) {
                viewModel.getUserId { id ->
                    if (id != null) {
                        Log.d("HomeScreen", "User ID: $id")
                        userId = id
                        viewModel.carregarJogosFavoritos(id) { favoriteIds ->
                            favoriteGameIds = favoriteIds
                        }
                    }
                }
            }
            delay(1000)
            isLoading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column (modifier = Modifier.padding(paddingValues)) {
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Pesquisar") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            // LazyRow to Recent Searches
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentSearches) { game ->
                    Button(onClick = { onGameSelected(game) }) {
                        game.name?.let { Text(it) }
                    }
                }
            }

            // LazyColumn to Filtered Games List
            if (isLoading) {
                LoadingIndicator()
            } else if (!isConnected) {
                NetworkWarning()
            } else {
                LazyColumn {
                    items(filteredGames) { game ->
                        Log.d(
                            "Game Api",
                            "${game.name}, ${game.id}, ${game.releaseDate}, ${game.imageUrl}"
                        )

                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(durationMillis = 300)),
                            exit = fadeOut(tween(durationMillis = 300))
                        ) {
                            Card(
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("gameDetails/${game.id}")
                                    },
                                elevation = CardDefaults.cardElevation(2.dp),
                                shape = RectangleShape
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

                                                // TODO
                                                // viewModel.salvarJogoNaLista(viewModel.getUserId(), game)

                                                coroutineScope.launch {
                                                    val result = snackbarHostState.showSnackbar(
                                                        message = "${game.name} foi adicionado à sua lista!",
                                                        duration = SnackbarDuration.Short,
                                                        withDismissAction = true
                                                    )
                                                }

                                                if (game.status == GameStatus.PLANNING_TO_PLAY || game.status == GameStatus.PLAYING) {
                                                    selectedGameIdForReminder = game.id
                                                }
                                            }
                                        }
                                    )
                                }

                                if (selectedGameIdForReminder == game.id && !showReminderDialog) {
                                    showReminderDialog = true
                                }

                                if (selectedGameIdForReminder == game.id && showReminderDialog) {
                                    AlertDialog(
                                        onDismissRequest = {
                                            showReminderDialog = false
                                            selectedGameIdForReminder = null
                                        },
                                        title = { Text("Definir Lembrete") },
                                        text = {
                                            Column {
                                                Text("Deseja definir um lembrete para jogar ${game.name}?")
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(top = 16.dp)
                                                ) {
                                                    Checkbox(
                                                        checked = doNotAskAgain,
                                                        onCheckedChange = {
                                                            doNotAskAgain = it
                                                            // Salvar a escolha do usuário (por exemplo, usando SharedPreferences ou DataStore)
                                                            println("Usuário optou por 'Não perguntar novamente': $doNotAskAgain")
                                                        }
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text("Não perguntar sobre lembretes novamente")
                                                }
                                            }
                                        },
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    showReminderDialog = false
                                                    selectedGameIdForReminder = null
                                                    navController.navigate("gameDetails/${game.id}")
                                                }
                                            ) {
                                                Text("Confirmar")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(
                                                onClick = {
                                                    showReminderDialog = false
                                                    selectedGameIdForReminder = null
                                                }
                                            ) {
                                                Text("Cancelar")
                                            }
                                        },
                                        properties = DialogProperties(
                                            dismissOnBackPress = true,
                                            dismissOnClickOutside = true
                                        )
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
                                            .size(200.dp)
                                            .align(Alignment.CenterVertically)
                                            .fillMaxHeight()
                                            .clickable {
                                                expandedImageUrl =
                                                    game.imageUrl  // Define expanded image URL
                                            }
                                    )

                                    // Game details
                                    Column(
                                        modifier = Modifier.padding(
                                            end = 22.dp,
                                            top = 24.dp,
                                            bottom = 24.dp
                                        )
                                    ) {
                                        // Game name, release date and favorite icon
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                // Game name
                                                game.name?.let {
                                                    Text(
                                                        text = it,
                                                        style = MaterialTheme.typography.titleMedium.copy(
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }

                                                // Release date
                                                game.releaseDate?.let {
                                                    Text(
                                                        text = it,
                                                        style = MaterialTheme.typography.bodySmall.copy(
                                                            color = Color.Gray
                                                        )
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Favorite icon
                                            val isPressed = remember { mutableStateOf(false) }
                                            val scale by animateFloatAsState(
                                                targetValue = if (isPressed.value) 1.2f else 1.0f, // Escala o ícone
                                                animationSpec = tween(durationMillis = 150) // Duração do efeito de "pop"
                                            )
                                            Icon(
                                                imageVector = if (favoriteGameIds.contains(game.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                contentDescription = "Favorite",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .scale(scale)
                                                    .clickable {
                                                        // TODO
                                                        if(viewModel.isUserLogged()) {
                                                            if (!favoriteGameIds.contains(game.id)) {
                                                                viewModel.salvarJogoFavorito(userId, game)
                                                                favoriteGameIds = favoriteGameIds + game.id

                                                                coroutineScope.launch {
                                                                    val result =
                                                                        snackbarHostState.showSnackbar(
                                                                            message = "${game.name} foi adicionado aos favoritos!",
                                                                            // actionLabel = "VER",
                                                                            duration = SnackbarDuration.Short,
                                                                            withDismissAction = true
                                                                        )
                                                                }

                                                                // Dispara a animação de "pop" apenas quando o jogo for adicionado aos favoritos
                                                                isPressed.value = true

                                                                // Reseta o estado de pressionado após o efeito de "pop"
                                                                coroutineScope.launch {
                                                                    delay(150) // Tempo do efeito de pop
                                                                    isPressed.value = false
                                                                }
                                                            } else {
                                                                gameToRemoveFromFavorites = game.id
                                                                showRemoveFavoriteDialog = true
                                                            }
                                                        } else {
                                                            coroutineScope.launch {
                                                                snackbarHostState.showSnackbar(
                                                                    message = "Você precisa estar logado para adicionar aos favoritos.",
                                                                    duration = SnackbarDuration.Short
                                                                )
                                                            }
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
                                        game.description?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 4,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(top = 8.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Botão "Adicionar à Lista"
                                        val backgroundColor by animateColorAsState(
                                            targetValue = if (game.isAddedToList.value) MaterialTheme.colorScheme.primary else Color.Transparent, // Alterando entre cor de destaque e transparente
                                            animationSpec = tween(durationMillis = 400) // Duração da animação
                                        )
                                        val iconColor by animateColorAsState(
                                            targetValue = if (!game.isAddedToList.value) MaterialTheme.colorScheme.primary else Color.White,
                                            animationSpec = tween(durationMillis = 400)
                                        )
                                        Row(
                                            modifier = Modifier
                                                .height(48.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .background(backgroundColor)
                                                .clickable {
                                                    if (viewModel.isUserLogged()) {
                                                        if (game.isAddedToList.value) {
                                                            dropdownExpandedByGameId = game.id // Alterna o valor do dropdown
                                                        } else {
                                                            selectedGameId = game.id
                                                        }
                                                    } else {
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                message = "Você precisa estar logado para adicionar à lista.",
                                                                duration = SnackbarDuration.Short
                                                            )
                                                        }
                                                    }
                                                },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = if (!game.isAddedToList.value) Icons.Filled.BookmarkBorder else Icons.Filled.Bookmark,
                                                contentDescription = if (!game.isAddedToList.value) "Adicionar" else "Adicionado",
                                                tint = iconColor
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (!game.isAddedToList.value) "Adicionar à Lista" else "Adicionado",
                                                color = iconColor,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            )
                                        }

                                        if (game.isAddedToList.value && dropdownExpandedByGameId == game.id) {
                                            Box(modifier = Modifier.padding(start = 8.dp)) {
                                                MaintenanceDropdownMenu(
                                                    game = game,
                                                    expanded = dropdownExpandedByGameId == game.id,
                                                    onDismissRequest = {
                                                        dropdownExpandedByGameId = null
                                                    },
                                                    onEdit = {
                                                        dropdownExpandedByGameId = null
                                                        selectedGameId = game.id
                                                    },
                                                    onRemove = {
                                                        dropdownExpandedByGameId = null
                                                        game.isAddedToList.value = false
                                                        // TODO
                                                        // viewModel.removerJogoDaLista(viewModel.getUserId(), game.id)
                                                        coroutineScope.launch {
                                                            val result =
                                                                snackbarHostState.showSnackbar(
                                                                    message = "${game.name} foi removido da sua lista",
                                                                    actionLabel = "DESFAZER",
                                                                    duration = SnackbarDuration.Short,
                                                                    withDismissAction = true
                                                                )
                                                            if (result == SnackbarResult.ActionPerformed) {
                                                                game.isAddedToList.value = true
                                                                // TODO
                                                                // viewModel.salvarJogoNaLista(viewModel.getUserId(), game)
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }

                                        if (showRemoveFavoriteDialog && gameToRemoveFromFavorites == game.id) {
                                            AlertDialog(
                                                onDismissRequest = {
                                                    showRemoveFavoriteDialog = false
                                                },
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
                                                            viewModel.removerJogoFavorito(userId, game.id)
                                                            favoriteGameIds = favoriteGameIds - game.id

                                                            coroutineScope.launch {
                                                                val result =
                                                                    snackbarHostState.showSnackbar(
                                                                        message = "${game.name} foi removido dos favoritos",
                                                                        actionLabel = "DESFAZER",
                                                                        duration = SnackbarDuration.Short,
                                                                        withDismissAction = true
                                                                    )
                                                                if (result == SnackbarResult.ActionPerformed) {
                                                                    viewModel.salvarJogoFavorito(userId, game)
                                                                    favoriteGameIds = favoriteGameIds + game.id
                                                                }
                                                            }
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
                        }
                        Spacer(modifier = Modifier.height(16.dp))
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