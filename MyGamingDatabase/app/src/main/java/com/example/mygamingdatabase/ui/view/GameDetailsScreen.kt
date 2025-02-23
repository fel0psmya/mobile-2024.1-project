package com.example.mygamingdatabase.ui.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mygamingdatabase.ui.components.LoadingIndicator
import com.example.mygamingdatabase.data.GameRepository
import com.example.mygamingdatabase.data.models.Game
import com.example.mygamingdatabase.data.models.statusDescriptions
import com.example.mygamingdatabase.ui.components.AlarmReminderDialog
import com.example.mygamingdatabase.ui.components.MaintenanceDropdownMenu
import com.example.mygamingdatabase.ui.components.MaintenanceItemDialog
import com.example.mygamingdatabase.viewmodel.GameViewModel
import com.example.mygamingdatabase.viewmodel.GameViewModelFactory
import com.google.accompanist.web.rememberWebViewState
import com.google.accompanist.web.WebView
import kotlinx.coroutines.delay

@Composable
fun YouTubePlayer(videoUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val state = rememberWebViewState(url = "https://www.youtube.com/embed/${extractYouTubeVideoId(videoUrl)}")

    WebView(
        state = state,
        modifier = modifier.padding(start = 16.dp, end = 16.dp),
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
            webView.settings.setSupportMultipleWindows(true)
        }
    )
}

fun extractYouTubeVideoId(url: String): String {
    val regex = "https://(?:www\\.)?youtube\\.com/watch\\?v=([^&]+)".toRegex()
    val match = regex.find(url)
    return match?.groupValues?.get(1) ?: ""
}

@Composable
fun SlideShowSection(screenshots: List<String>?) {
    var expandedImageUrl by remember { mutableStateOf<String?>(null) } // To store expanded image's URL

    // Verificar se temos conteúdo (vídeo ou imagens)
    val hasContent = !screenshots.isNullOrEmpty()

    Log.d("Screenshots", screenshots?.joinToString(", ") ?: "Sem screenshots")

    if (hasContent) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Se houver screenshots
            if (screenshots != null && screenshots.isNotEmpty()) {
                items(screenshots) { screenshot ->
                    Image(
                        painter = rememberAsyncImagePainter(model = screenshot),
                        contentDescription = "Screenshot do jogo",
                        modifier = Modifier
                            .size(200.dp) // Ajuste o tamanho conforme necessário
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                expandedImageUrl = screenshot // Define expanded image URL
                            }
                    )
                }
            }
        }
    } else {
        // Caso não tenha vídeo ou imagens, exibe uma mensagem ou conteúdo alternativo
        Text("Nenhum conteúdo disponível", style = MaterialTheme.typography.bodyLarge)
    }

    expandedImageUrl?.let { imageUrl ->
        ExpandedImageDialog(imageUrl = imageUrl, onClose = { expandedImageUrl = null })
    }
}

@Composable
fun MainInfoSection(game: Game) {
    var expandedImageUrl by remember { mutableStateOf<String?>(null) } // To store expanded image's URL

    var selectedGameId by remember { mutableStateOf<Int?>(null) }
    var showAlarmReminder by remember { mutableStateOf(false) }
    var gameToRemind by remember { mutableStateOf<Int?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagem de fundo (artwork)
            Image(
                painter = rememberAsyncImagePainter(model = game.artworkUrl),
                contentDescription = "Fundo do jogo",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop // Faz a imagem ocupar toda a área sem distorção
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f)) // Fundo preto com 70% de opacidade
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagem do jogo
                Image(
                    painter = rememberAsyncImagePainter(model = game.imageUrl),
                    contentDescription = "Imagem do jogo",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            expandedImageUrl = game.imageUrl  // Define expanded image URL
                        }
                )

                // Informações principais
                Column(modifier = Modifier.padding(end = 24.dp)) {
                    game.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    game.releaseDate?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Plataformas
                    Text(
                        "Plataformas:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(top = 4.dp),
                        color = (MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        "${game.platforms?.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                        color = Color.White
                    )

                    // Botão de Lembrete
                    val rmndBackgroundColor by animateColorAsState(
                        targetValue =  if (game.isReminded.value) Color.White else Color.Black.copy(alpha = 0.3f), // Alterando entre cor de destaque e transparente
                        animationSpec = tween(durationMillis = 400) // Duração da animação
                    )
                    val rmndTextColor by animateColorAsState(
                        targetValue =  if (game.isReminded.value) Color.Black else Color.White,
                        animationSpec = tween(durationMillis = 400)
                    )
                    Row (
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(40.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                rmndBackgroundColor // Fundo preto com opacidade
                            )
                            .clickable {
                                if (!game.isReminded.value) {
                                    showAlarmReminder = true
                                    gameToRemind = game.id
                                } else {
                                    game.isReminded.value = !game.isReminded.value
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                        // horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (game.isReminded.value) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                            contentDescription = "Reminder",
                            tint = rmndTextColor,
                            modifier = Modifier
                                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                                .size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (game.isReminded.value) "Lembrete" else "Lembrar-me",
                            color = rmndTextColor,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                        )
                    }
                }
            }
        }
    }

    if (showAlarmReminder && gameToRemind == game.id) {
        AlarmReminderDialog(
            game,
            onDismissRequest = {
                showAlarmReminder = false
                gameToRemind = null
            },
            onConfirm = { hour, minute, days ->
                Log.d("AlarmReminderDialog", "onConfirm chamado!")

                if (!game.isReminded.value) {
                    game.reminderTime.value = "$hour:$minute" // Exemplo de formatação do horário
                    game.reminderDays.value = if (days.isNotEmpty()) {
                        days.joinToString(", ") // Concatena os dias com vírgulas
                    } else {
                        "Nenhum dia selecionado"
                    }
                    game.isReminded.value = true
                }

                showAlarmReminder = false
                gameToRemind = null
            }
        )
    }

    expandedImageUrl?.let { imageUrl ->
        ExpandedImageDialog(imageUrl = imageUrl, onClose = { expandedImageUrl = null })
    }
}

@Composable
fun ActionButtonsSection(
    game: Game,
    isFavorite: Boolean,
    userId: String?,
    viewModel: GameViewModel
) {
    var selectedGameId by remember { mutableStateOf<Int?>(null) }
    var dropdownExpandedByGameId by remember { mutableStateOf<Int?>(null) }

    var showRemoveFavoriteDialog by remember { mutableStateOf(false) }
    var gameToRemoveFromFavorites by remember { mutableStateOf<Int?>(null) }
    var localIsFavorite by remember { mutableStateOf(isFavorite) }

    val context = LocalContext.current

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

    // Botão de favoritos
    val favBackgroundColor by animateColorAsState(
        targetValue =  if (localIsFavorite) MaterialTheme.colorScheme.primary else Color.Transparent, // Alterando entre cor de destaque e transparente
        animationSpec = tween(durationMillis = 400) // Duração da animação
    )
    val favTextColor by animateColorAsState(
        targetValue =  if (localIsFavorite) Color.White else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 400)
    )
    Row (
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(48.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                favBackgroundColor
            )
            .clickable {
                if (userId != null) {
                    if (!localIsFavorite) {
                        viewModel.salvarJogoFavorito(userId, game)
                        Toast.makeText(context, "${game.name} foi adicionado aos seus favoritos!", Toast.LENGTH_SHORT).show()
                        localIsFavorite = true
                    } else {
                        gameToRemoveFromFavorites = game.id
                        showRemoveFavoriteDialog = true
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (localIsFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = favTextColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (localIsFavorite) "Favorito" else "Favoritar",
            color = favTextColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Botão "Adicionar à Lista"
    val listBackgroundColor by animateColorAsState(
        targetValue =  if (game.isAddedToList.value) MaterialTheme.colorScheme.primary else Color.Transparent, // Alterando entre cor de destaque e transparente
        animationSpec = tween(durationMillis = 400) // Duração da animação
    )
    val listTextColor by animateColorAsState(
        targetValue =  if (game.isAddedToList.value) Color.White else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 400)
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(48.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                listBackgroundColor
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
        Icon(
            imageVector = if (!game.isAddedToList.value) Icons.Filled.BookmarkBorder else Icons.Filled.Bookmark,
            contentDescription = if (!game.isAddedToList.value) "Adicionar" else "Adicionado",
            tint = listTextColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (!game.isAddedToList.value) "Adicionar à Lista" else "${game.userScore} | ${statusDescriptions[game.status]}",
            color = listTextColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
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
                        viewModel.removerJogoFavorito(userId!!, game.id)
                        Toast.makeText(context, "${game.name} foi removido dos seus favoritos", Toast.LENGTH_SHORT).show()
                        localIsFavorite = false
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

@ExperimentalMaterial3Api
@Composable
fun GameDetailsScreen (gameId : String, context: Context = LocalContext.current) {
    val repository = GameRepository()
    val viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(context, repository))
    val game = viewModel.games.value.find { it.id == gameId.toInt() }
    var isLoading by remember { mutableStateOf(true) }

    var isFavorite by remember { mutableStateOf(false) }
    var userId by remember { mutableStateOf<String?>(null) }


    // Simula o carregamento dos dados. Usar um ViewModel ou outra lógica de carregamento posteriormente.
    LaunchedEffect(gameId) {
        isLoading = true
        viewModel.fetchGameById(gameId.toInt())
        if(viewModel.isUserLogged()) {
            viewModel.getUserId { id ->
                if (id != null) {
                    userId = id
                    viewModel.isGameFavorite(id, gameId.toInt()) { favorite ->
                        isFavorite = favorite
                    }
                }
            }
        }
        delay(1000) // Simula um atraso de 1 segundo
        isLoading = false
    }

    if(isLoading) {
        LoadingIndicator()
    } else {
        game?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Permite a rolagem vertical
            ) {
                // Seção 1: Imagem, título, ano, plataformas
                MainInfoSection(game)

                Spacer(modifier = Modifier.height(24.dp))

                // Seção 2: Botão de Favorito e Botão de Adicionar à Lista
                ActionButtonsSection(game, isFavorite, userId, viewModel)

                Spacer(modifier = Modifier.height(24.dp))

                // Seção 3: Trailer
                game.trailerUrl?.let {
                    YouTubePlayer(
                        videoUrl = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Tamanho do player
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Seção 4: Screenshots
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RectangleShape
                ) {
                    SlideShowSection(screenshots = game.screenshots)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Seção 5: Descrição
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Descrição",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        game.description?.let { it1 ->
                            Text(
                                text = it1,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        } ?: run {
            // Shows an error message if the event is not found
            Text(
                text = "Evento não encontrado",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}