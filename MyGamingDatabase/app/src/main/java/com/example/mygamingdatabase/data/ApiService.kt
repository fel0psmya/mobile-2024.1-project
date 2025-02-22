package com.example.mygamingdatabase.data

import com.example.mygamingdatabase.data.models.GameResponse
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("games/")
    suspend fun getGames(): List<GameResponse>

    @GET("games/{id}/")
    suspend fun getGameById(@Path("id") id: Int): GameResponse
}
