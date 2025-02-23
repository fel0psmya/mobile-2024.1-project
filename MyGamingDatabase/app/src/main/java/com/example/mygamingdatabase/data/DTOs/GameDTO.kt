package com.example.mygamingdatabase.data.DTOs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.mygamingdatabase.data.models.GameStatus

data class GameDTO(
    val id: Int,
    val isFavorite: Boolean, // If the game is favorite
    val isAddedToList: Boolean,
    val isReminded: Boolean,
    val reminderTime: String?, // HH:mm format
    val reminderDays: String?, // CSV format: "Monday,Wednesday,Friday"
    var userScore: Int?, // User score (1-10)
    var status: GameStatus, // Status of the game
) {
    constructor() : this(
        id = 0,
        isFavorite = false,
        isAddedToList = false,
        isReminded = false,
        reminderTime = null,
        reminderDays = null,
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY) // Default constructor for Firebase
}