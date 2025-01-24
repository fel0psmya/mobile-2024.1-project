package com.example.mygamingdatabase.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, // Specific icon for each item
                label = {
                    Text(
                        label,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
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