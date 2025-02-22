package com.example.mygamingdatabase.data.models

data class GameResponse(
    val id: Int,
    val name: String?,
    val imageUrl: String?,
    val trailerUrl: String?,
    val description: String?,
    val platforms: List<String>?,
    val releaseDate: String?,
    val screenshots: List<String>?,
    val artworkUrl: String?
)