package com.example.mygamingdatabase.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygamingdatabase.DataStoreUtils
import com.example.mygamingdatabase.data.GameRepository
import com.example.mygamingdatabase.data.RetrofitInstance
import com.example.mygamingdatabase.data.mapToGame
import com.example.mygamingdatabase.data.models.Game
import com.example.mygamingdatabase.data.models.GameStatus
import com.example.mygamingdatabase.data.models.toDTO
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GameViewModel (
    context: Context,
    private val repository: GameRepository
) : ViewModel() {
    // Firebase Authentication Section
    var loginResult: ((Boolean) -> Unit)? = null
    var registerResult: ((Boolean) -> Unit)? = null

    fun isUserLogged(): Boolean {
        return repository.isUserLogged()
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.loginUser(email, password)
            onResult(success) // Retorna true ou false para a tela de login
        }
    }

    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.resetPassword(email)
            onResult(success)
        }
    }

    fun getUserName(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val name = repository.getUserName()
            onResult(name)
        }
    }

    fun getUserId(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val userId = repository.getUserId()
            onResult(userId)
        }
    }

    fun loginWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.loginWithGoogle(idToken)
            onResult(success)
        }
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        return repository.getGoogleSignInClient(context)
    }

    fun logout() {
        repository.logout()
        games.value = emptyList()
    }

    fun register(email: String, password: String, name: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                repository.registerUser(email, password, name)
                onResult(true)
            } catch (e: Exception) {
                Log.e("GameViewModel", "Register error: ${e.message}")
                onResult(false)
            }
        }
    }

    // Firebase Realtime Database Section
    private val database = FirebaseDatabase.getInstance().reference.child("users")

    private val _userGames = MutableStateFlow<List<Game>>(emptyList())
    val userGames: StateFlow<List<Game>> = _userGames

    init {
        monitorarAlteracoes()
    }

    private fun monitorarAlteracoes() {
        database.addChildEventListener(
            object : ChildEventListener {
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    carregarJogos()
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    carregarJogos()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    carregarJogos()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GameViewModel - Firebase Error", "Database error: ${error.message}")
                }
            }
        )
    }

    private fun carregarJogos() {
        database.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val gamesList = mutableListOf<Game>()
                    for (gameSnapshot in snapshot.children) {
                        val game = gameSnapshot.getValue(Game::class.java)
                        if (game != null) {
                            gamesList.add(game)
                        }
                    }
                    _userGames.value = gamesList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GameViewModel - FirebaseError", "Database error: ${error.message}")
                }
            }
        )
    }

    fun salvarJogoFavorito(userId: String, game: Game) {
        val gameDTO = game.toDTO()
        val favoriteGamesRef = database.child(userId).child("favorites").child(game.id.toString())
        favoriteGamesRef.setValue(gameDTO)
            .addOnSuccessListener {
                Log.d("GameViewModel", "Jogo ${game.id} adicionado aos favoritos do usuário $userId")
            }
            .addOnFailureListener { e ->
                Log.e("GameViewModel", "Erro ao adicionar jogo ${game.id} aos favoritos: ${e.message}")
            }
    }

    fun removerJogoFavorito(userId: String, gameId: Int) {
        database.child(userId).child("favorites").child(gameId.toString()).removeValue()
            .addOnSuccessListener {
                Log.d("GameViewModel", "Jogo $gameId removido dos favoritos do usuário $userId")
            }
            .addOnFailureListener { e ->
                Log.e("GameViewModel", "Erro ao remover jogo $gameId dos favoritos: ${e.message}")
            }
    }

    fun removerTodosOsFavoritos (userId: String, onComplete: () -> Unit) {
        database.child(userId).child("favorites").removeValue()
            .addOnSuccessListener {
                Log.d("GameViewModel", "Todos os favoritos removidos do usuário $userId")
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("GameViewModel", "Erro ao remover todos os favoritos: ${e.message}")
                onComplete()
            }
    }

    fun isGameFavorite(userId: String, gameId: Int, onComplete: (Boolean) -> Unit) {
        val favoriteGamesRef = database.child(userId).child("favorites").child(gameId.toString())
        favoriteGamesRef.get().addOnSuccessListener { snapshot ->
            onComplete(snapshot.exists())
        }.addOnFailureListener { e ->
            Log.e("GameViewModel", "Erro ao verificar jogo favorito: ${e.message}")
            onComplete(false)
        }
    }

    fun buscarJogosFavoritos(userId: String, onResult: (List<Int>) -> Unit) {
        viewModelScope.launch {
            val favoriteGamesRef = database.child(userId).child("favorites")
            favoriteGamesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favoriteGameIds = mutableListOf<Int>()
                    for (gameSnapshot in snapshot.children) {
                        val gameId = gameSnapshot.key?.toIntOrNull()
                        if (gameId != null) {
                            favoriteGameIds.add(gameId)
                        }
                    }
                    onResult(favoriteGameIds)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GameViewModel - FirebaseError", "Database error: ${error.message}")
                    onResult(emptyList())
                }
            })
        }
    }

    fun carregarJogosFavoritos(userId: String,  onComplete: (List<Int>) -> Unit) {
        buscarJogosFavoritos(userId) { favoriteGameIds ->
            val updatedGames = games.value.map { game ->
                game.copy(isFavorite = mutableStateOf(favoriteGameIds.contains(game.id)))
            }
            games.value = updatedGames
            onComplete(favoriteGameIds)
        }
    }

    fun salvarJogoNaLista(userId: String, game: Game) {
        val gameData = mapOf(
            "rating" to game.userScore,
            "status" to game.status.name
        )
        database.child(userId).child("games").child(game.id.toString()).setValue(gameData)
    }

    fun removerJogoDaLista(userId: String, gameId: Int) {
        database.child(userId).child("games").child(gameId.toString()).removeValue()
    }

    fun removerTodosOsJogosDaLista (userId: String, onComplete: () -> Unit) {
        database.child(userId).child("games").removeValue()
            .addOnSuccessListener {
                Log.d("GameViewModel", "Limpeza da lista do usuário $userId concluída")
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("GameViewModel", "Erro ao limpar lista: ${e.message}")
                onComplete()
            }
    }

    fun alterarJogoDaLista(userId: String, game: Game) {
        val gameData = mapOf(
            "rating" to game.userScore,
            "status" to game.status.name
        )
        database.child(userId).child("games").child(game.id.toString()).updateChildren(gameData)
    }

    fun isGameAddedToList(userId: String, gameId: Int, onComplete: (Boolean) -> Unit) {
        val gameListRef = database.child(userId).child("games").child(gameId.toString())
        gameListRef.get().addOnSuccessListener { snapshot ->
            onComplete(snapshot.exists())
        }.addOnFailureListener { e ->
            Log.e("GameViewModel", "Erro ao verificar jogo na lista: ${e.message}")
            onComplete(false)
        }
    }

    fun buscarJogosNaLista(userId: String, onResult: (List<Game>) -> Unit) {
        viewModelScope.launch {
            val userGamesRef = database.child(userId).child("games")
            userGamesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userGamesList = mutableListOf<Game>()
                    for (gameSnapshot in snapshot.children) {
                        Log.d("GameViewModel", "Snapshot key: ${gameSnapshot.key}, value: ${gameSnapshot.value}")
                        val gameId = gameSnapshot.key?.toIntOrNull()

                        if (gameId != null) {
                            val rating = gameSnapshot.child("rating").getValue(Int::class.java) ?: 0
                            val statusString = gameSnapshot.child("status").getValue(String::class.java)
                            val status = statusString?.let { GameStatus.valueOf(it) } ?: GameStatus.PLANNING_TO_PLAY

                            val game = Game(gameId, rating, status)

                            userGamesList.add(game)
                        } else {
                            Log.e("GameViewModel", "Jogo não encontrado para o ID $gameId")
                        }
                    }
                    onResult(userGamesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("GameViewModel - FirebaseError", "Database error: ${error.message}")
                    onResult(emptyList())
                }
            })
        }
    }

    fun carregarJogosNaLista(userId: String,  onComplete: (List<Game>) -> Unit) {
        buscarJogosNaLista(userId) { gameList ->
            Log.d("GameViewModel", "Jogos carregados: $gameList")
            val updatedGames = games.value.map { game ->
                val gameInList = gameList.find { it.id == game.id }
                game.copy(
                    isAddedToList = mutableStateOf(gameInList != null),
                    userScore = gameInList?.userScore ?: game.userScore,
                    status = gameInList?.status ?: game.status
                )
            }
            games.value = updatedGames
            onComplete(gameList)
        }
    }

    fun salvarLembrete(userId: String, gameId: Int, reminderTime: String, reminderDays: String) {
        val reminderData = mapOf(
            "reminderTime" to reminderTime,
            "reminderDays" to reminderDays
        )
        database.child(userId).child("reminders").child(gameId.toString()).setValue(reminderData)
    }

    // Remover lembrete

    // Alterar lembrete

    // API Section
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

    fun fetchGamesByIds(ids: List<Int>, onResult: (List<Game>) -> Unit) {
        viewModelScope.launch {
            try {
                val gameList = mutableListOf<Game>()
                for(id in ids) {
                    val response = RetrofitInstance.api.getGameById(id)
                    val game = mapToGame(response)
                    gameList.add(game)
                }
                onResult(gameList)
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