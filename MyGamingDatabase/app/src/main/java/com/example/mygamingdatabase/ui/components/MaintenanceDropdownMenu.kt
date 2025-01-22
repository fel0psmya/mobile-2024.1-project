package com.example.mygamingdatabase.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mygamingdatabase.models.Game

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
            text = { Text("Editar") }
        )
        DropdownMenuItem(
            onClick = {
                // onDismissRequest() // Fecha o menu
                showRemoveDialog = true // Exibe o dialog de confirmação
            },
            text = { Text("Remover") }
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