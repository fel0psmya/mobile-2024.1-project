package com.example.mygamingdatabase.data

import androidx.compose.runtime.mutableStateOf
import com.example.mygamingdatabase.data.models.Game
import com.example.mygamingdatabase.data.models.GameResponse
import com.example.mygamingdatabase.data.models.GameStatus

fun mapToGame(gameResponse: GameResponse): Game {
    return Game(
        id = gameResponse.id,
        name = gameResponse.name ?: "Unknown",
        imageUrl = gameResponse.imageUrl ?: "No image available",
        trailerUrl = gameResponse.trailerUrl ?: "No trailer available",
        description = gameResponse.description ?: "No description available",
        platforms = gameResponse.platforms ?: listOf(),
        releaseDate = gameResponse.releaseDate ?: "Unknown",
        screenshots = gameResponse.screenshots ?: listOf(),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = gameResponse.artworkUrl ?: "No artwork available"
    )
}