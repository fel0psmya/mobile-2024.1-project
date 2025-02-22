package com.example.mygamingdatabase.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygamingdatabase.DataStoreUtils
import com.example.mygamingdatabase.data.RetrofitInstance
import com.example.mygamingdatabase.data.mapToGame
import com.example.mygamingdatabase.data.models.Game

class GameViewModel (context: Context) : ViewModel() {
    var games = mutableStateOf<List<Game>>(listOf())
        private set

    fun fetchGames () {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getGames()
                response.forEach { gameResponse ->
                    Log.d("GameViewModel", "GameResponse: $gameResponse")
                }
                games.value = response.map { mapToGame(it) }
                games.value.forEach { game ->
                    Log.d("GameViewModel", "Mapped Game: $game")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchGameById (id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getGameById(id)
                Log.d("GameViewModel", "GameResponse: $response")
                val game = mapToGame(response)
                Log.d("GameViewModel", "Mapped Game: $game")
                games.value = listOf(game)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Lê o tema do DataStore
    val themeFlow: Flow<Boolean> = DataStoreUtils.readTheme(context)
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            // Carrega as tarefas salvas do DataStore
            try {
                themeFlow.collect { savedTheme ->
                    _isDarkTheme.value = savedTheme
                }
            } catch (e: Exception) {
                // Logar erro ou tratar de alguma maneira
                e.printStackTrace()
            }
        }
    }

    fun toggleTheme(context: Context) {
        viewModelScope.launch {
            val newTheme = !_isDarkTheme.value
            _isDarkTheme.value = newTheme
            DataStoreUtils.saveTheme(context, newTheme)
        }
    }
}