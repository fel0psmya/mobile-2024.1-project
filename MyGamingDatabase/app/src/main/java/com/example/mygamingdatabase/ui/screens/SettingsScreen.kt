package com.example.mygamingdatabase.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mygamingdatabase.models.gameList

@Composable
fun SettingsScreen(
    darkThemeState: MutableState<Boolean>
){
    var showClearFavoritesDialog by remember { mutableStateOf(false) }
    var showClearUserGameListDialog by remember { mutableStateOf(false) }

    // Clear favorites function
    val clearFavorites = {
        gameList.forEach { game ->
            game.isFavorite.value = false
        }
    }

    // Clear user game list
    val clearGameList = {
        gameList.forEach { game ->
            game.isAddedToList.value = false
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
                checked = darkThemeState.value,
                onCheckedChange = {
                    darkThemeState.value = it
                }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

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
                        showClearUserGameListDialog = true
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
                        showClearFavoritesDialog = true
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