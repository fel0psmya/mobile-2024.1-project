package com.example.mygamingdatabase.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mygamingdatabase.models.Game
import com.example.mygamingdatabase.models.gameList
import com.google.accompanist.web.rememberWebViewState
import com.google.accompanist.web.WebView

@Composable
fun YouTubePlayer(videoUrl: String, modifier: Modifier = Modifier) {
    val state = rememberWebViewState(url = "https://www.youtube.com/embed/${extractYouTubeVideoId(videoUrl)}")

    WebView(
        state = state,
        modifier = modifier,
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
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
    // Verificar se temos conteúdo (vídeo ou imagens)
    val hasContent = !screenshots.isNullOrEmpty()

    Log.d("Screenshots", screenshots?.joinToString(", ") ?: "Sem screenshots")

    if (hasContent) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
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
                    )
                }
            }
        }
    } else {
        // Caso não tenha vídeo ou imagens, exibe uma mensagem ou conteúdo alternativo
        Text("Nenhum conteúdo disponível", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun MainInfoSection(game: Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagem do jogo
        Image(
            painter = rememberAsyncImagePainter(model = game.imageUrl),
            contentDescription = "Imagem do jogo",
            modifier = Modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        // Informações principais
        Column {
            Text(
                text = game.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary)
            Text(
                text = game.releaseDate,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Plataformas
            Text(
                "Plataformas: ${game.platforms?.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun GameDetailsScreen (gameId : String) {
    val game = gameList.find { it.id.toString() == gameId } // Using the updated list
    game?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Permite a rolagem vertical
        ) {
            // Seção 1: Trailer
            game.trailerUrl?.let {
                YouTubePlayer(
                    videoUrl = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Tamanho do player
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Seção 2: Screenshots
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                elevation = CardDefaults.cardElevation(4.dp)) {
                SlideShowSection(screenshots = game.screenshots)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seção 3: Imagem, título, ano, plataformas
            MainInfoSection(game)

            Spacer(modifier = Modifier.height(16.dp))

            // Seção 4: Descrição
            Card(
                modifier = Modifier
                .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                elevation = CardDefaults.cardElevation(4.dp)) {

                Column (
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Descrição",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = game.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
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