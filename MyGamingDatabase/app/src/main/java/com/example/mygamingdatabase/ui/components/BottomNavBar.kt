package com.example.mygamingdatabase.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        // List of items with routes, labels and icons
        val items = listOf(
            Triple("home", "Página Inicial", Icons.Default.Home),
            Triple("list", "Minha Lista", Icons.Default.VideoLibrary)
        )
        items.forEach { (route, label, icon) ->
            val isSelected = currentDestination == route
            val backgroundColor by animateColorAsState(
                targetValue =  if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, // Alterando entre cor de destaque e transparente
                animationSpec = tween(durationMillis = 400) // Duração da animação
            )
            val iconSize by animateDpAsState(
                targetValue = if (isSelected) 30.dp else 24.dp, // O tamanho do ícone será 30.dp se o jogo for favorito, caso contrário 24.dp
                animationSpec = tween(durationMillis = 500) // Animação suave
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = backgroundColor,
                        modifier = Modifier.size(iconSize) // Aplica o tamanho animado
                    )
                }, // Specific icon for each item
                label = {
                    Text(
                        label,
                        color = backgroundColor
                    )
                },
                selected = false,
                onClick = {
                    navController.navigate(route)
                }
            )
        }
    }
}