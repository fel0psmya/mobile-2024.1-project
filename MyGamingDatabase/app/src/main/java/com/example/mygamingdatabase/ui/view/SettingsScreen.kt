package com.example.mygamingdatabase.ui.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mygamingdatabase.data.GameRepository
import com.example.mygamingdatabase.data.models.gameList
import com.example.mygamingdatabase.isInternetAvailable
import com.example.mygamingdatabase.viewmodel.GameViewModel
import com.example.mygamingdatabase.viewmodel.GameViewModelFactory

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    repository: GameRepository
){
    var showClearFavoritesDialog by remember { mutableStateOf(false) }
    var showClearUserGameListDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(true) }

    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(context, repository)
    )

    var userId by remember { mutableStateOf("Carregando...") }


    LaunchedEffect(Unit) {
        // Clear favorites function
        isConnected = isInternetAvailable(context)
        if(isConnected) {
            if (viewModel.isUserLogged()) {
                viewModel.getUserId { id ->
                    if (id != null) {
                        userId = id
                    }
                }
            }
        } else {
            Toast.makeText(context, "Sem conexão", Toast.LENGTH_LONG).show()
        }
    }

    val clearFavorites = {
        if (userId != "Carregando...") {
            viewModel.removerTodosOsFavoritos(userId) {
                Toast.makeText(context, "Favoritos removidos com sucesso", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Erro ao carregar o ID do usuário", Toast.LENGTH_LONG).show()
        }
    }

    val clearGameList = {
        if (userId != "Carregando...") {
            viewModel.removerTodosOsJogosDaLista(userId) {
                Toast.makeText(context, "Jogos removidos com sucesso da sua lista", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Erro ao carregar o ID do usuário", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Configurações",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Modo Escuro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modo Escuro",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = isDarkTheme,
                onCheckedChange = { isChecked -> onThemeChange(isChecked) }
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Limpar Lista
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Limpar Lista de Jogos",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Limpar",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        if (viewModel.isUserLogged()) {
                            showClearUserGameListDialog = true
                        } else {
                        Toast.makeText(context, "Você precisa estar logado para limpar sua lista.", Toast.LENGTH_LONG).show()
                        }
                    }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Limpar Favoritos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Limpar Favoritos",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Limpar",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        if (viewModel.isUserLogged()) {
                            showClearFavoritesDialog = true
                        } else {
                            Toast.makeText(context, "Você precisa estar logado para adicionar aos favoritos.", Toast.LENGTH_LONG).show()
                        }
                    }
            )
        }
    }

    if(showClearFavoritesDialog) {
        AlertDialog(
            onDismissRequest = { showClearFavoritesDialog = false },
            title = {
                Text(text = "Limpar lista de favoritos")
            },
            text = {
                Text("Você tem certeza que deseja limpar sua lista de favoritos? Essa ação não pode ser desfeita")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearFavoritesDialog = false
                        clearFavorites()
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showClearFavoritesDialog = false
                    }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if(showClearUserGameListDialog) {
        AlertDialog(
            onDismissRequest = { showClearUserGameListDialog = false },
            title = {
                Text(text = "Limpar sua lista de jogos")
            },
            text = {
                Text("Você tem certeza que deseja limpar sua lista de jogos? Essa ação não pode ser desfeita")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearUserGameListDialog = false
                        clearGameList()
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showClearUserGameListDialog = false
                    }) {
                    Text("Cancelar")
                }
            }
        )
    }
}