package com.example.mygamingdatabase.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mygamingdatabase.data.models.Game

@Composable
fun MaintenanceDropdownMenu(
    game: Game,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    var showRemoveDialog by remember { mutableStateOf(false) } // Controla se o AlertDialog de remoção está visível

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
                onDismissRequest() // Fecha o menu
                onEdit() // Ação de editar
            },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit, // Ícone de edição
                        contentDescription = "Editar"
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espaçamento entre o ícone e o texto
                    Text("Editar")
                }
            }
        )
        DropdownMenuItem(
            onClick = {
                // onDismissRequest() // Fecha o menu
                showRemoveDialog = true // Exibe o dialog de confirmação
            },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Ícone de edição
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espaçamento entre o ícone e o texto
                    Text("Remover", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    // AlertDialog de confirmação para remoção
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = {
                Text(text = "Remover jogo da lista")
            },
            text = {
                Text("Você tem certeza que deseja remover '${game.name}' da sua lista?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRemoveDialog = false // Fecha o dialog
                        onRemove() // Chama a função de remoção
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}