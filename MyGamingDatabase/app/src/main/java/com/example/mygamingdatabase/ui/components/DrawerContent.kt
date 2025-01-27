package com.example.mygamingdatabase.ui.components

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun DrawerContent(navController: NavHostController, onCloseDrawer: () -> Unit) {
    val context = LocalContext.current
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    Toast.makeText(context, "Você saiu. Até mais!", Toast.LENGTH_SHORT).show()
                    // navController.navigate("login") // Navega para a tela de login
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Sair") },
            text = { Text("Tem certeza de que deseja sair?") }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(380.dp)
            .padding(end = 20.dp)
            .background(MaterialTheme.colorScheme.surface),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "MY GAMING DATABASE",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(12.dp))

            DrawerMenuItem(
                label = "Perfil",
                icon = Icons.Default.Person,
                route = "profile",
                currentRoute = currentDestination,
                onClick = {
                    onCloseDrawer()
                }
            )

            HorizontalDivider(modifier = Modifier.width(330.dp).align(Alignment.CenterHorizontally).padding(vertical = 12.dp))

            DrawerMenuItem(
                label = "Página Inicial",
                icon = Icons.Default.Home,
                route = "home",
                currentRoute = currentDestination,
                onClick = {
                    navController.navigate("home")
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                label = "Minha Lista",
                icon = Icons.Default.VideoLibrary,
                route = "list",
                currentRoute = currentDestination,
                onClick = {
                    navController.navigate("list")
                    onCloseDrawer()
                }
            )

            HorizontalDivider(modifier = Modifier.width(330.dp).align(Alignment.CenterHorizontally).padding(vertical = 12.dp))

            DrawerMenuItem(
                label = "Ajuda e Perguntas Frequentes",
                icon = Icons.Default.Help,
                route = "help",
                currentRoute = currentDestination,
                onClick = {
                    navController.navigate("help")
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                label = "Configurações",
                icon = Icons.Default.Settings,
                route = "settings",
                currentRoute = currentDestination,
                onClick = {
                    navController.navigate("settings")
                    onCloseDrawer()
                }
            )

            HorizontalDivider(modifier = Modifier.width(330.dp).align(Alignment.CenterHorizontally).padding(vertical = 12.dp))

            DrawerMenuItem(
                label = "Sair",
                icon = Icons.Default.Logout,
                route = "logout",
                currentRoute = currentDestination,
                onClick = {
                    showExitDialog = true
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Versão 0.2.0",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    label: String,
    icon: ImageVector,
    route: String,
    currentRoute: String?,
    onClick: () -> Unit
) {
    val isSelected = currentRoute == route // Verifica se a rota atual é a do item
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val iconColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}