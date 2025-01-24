package com.example.mygamingdatabase.ui.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.mygamingdatabase.models.Game
import com.example.mygamingdatabase.models.scoreDescriptions
import com.example.mygamingdatabase.models.statusDescriptions

@OptIn(UnstableApi::class)
@Composable
fun MaintenanceItemDialog (
    game: Game, // Dados do jogo a ser editado
    onDismiss: () -> Unit, // Ação de dismiss
    onSave: (Game) -> Unit // Ação para salvar as alterações
) {
    // Estados locais
    val selectedStatus = remember { mutableStateOf(game.status) }
    val selectedScore = remember { mutableStateOf(game.userScore ?: 0) }

    // Controle dos menus suspensos
    var statusMenuExpanded by remember { mutableStateOf(false) }
    var scoreMenuExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Adicionar à lista") },
        text = {
            LazyColumn {
                item {
                    // Campo para editar o status
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        Text("Status:")

                        Spacer(modifier = Modifier.width(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    statusMenuExpanded = !statusMenuExpanded
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (statusMenuExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Toggle",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(horizontal = 4.dp)
                            )

                            Text(
                                text = statusDescriptions[selectedStatus.value]
                                    ?: "Selecionar Status",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            DropdownMenu(
                                expanded = statusMenuExpanded,
                                onDismissRequest = { statusMenuExpanded = false },
                                modifier = Modifier.heightIn(max = 200.dp)
                            ) {
                                statusDescriptions.forEach { (status) ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedStatus.value = status
                                            statusMenuExpanded = false
                                        },
                                        text = { Text(text = "${statusDescriptions[status]}") }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Nota:")

                        Spacer(modifier = Modifier.width(18.dp))

                        // Campo para editar a nota
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)) // Define as bordas arredondadas
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    scoreMenuExpanded = !scoreMenuExpanded
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (scoreMenuExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Toggle",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(horizontal = 4.dp)
                            )

                            Text(
                                text = "${selectedScore.value} - ${scoreDescriptions[selectedScore.value]}",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            DropdownMenu(
                                expanded = scoreMenuExpanded,
                                onDismissRequest = { scoreMenuExpanded = false },
                                modifier = Modifier.heightIn(max = 200.dp)
                            ) {
                                scoreDescriptions.forEach { (score) ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedScore.value = score
                                            scoreMenuExpanded = false
                                        },
                                        text = { Text(text = "$score - ${scoreDescriptions[score]}") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // Ao salvar, passamos os dados alterados para o callback onSave
                onSave(game.copy(userScore = selectedScore.value, status = selectedStatus.value))
                Log.d("MaintenanceItemDialog", "Status selecionado: ${selectedStatus.value}, Nota selecionada: ${selectedScore.value}, Adicionado: ${game.isAddedToList}")
            }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}