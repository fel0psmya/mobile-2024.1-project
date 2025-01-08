package com.example.mygamingdatabase.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        // List of items with routes, labels and icons
        val items = listOf(
            Triple("home", "Home", Icons.Default.Home),
            Triple("favorites", "Favoritos", Icons.Default.Favorite)
        )
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = label
                    )
                }, // Specific icon for each item
                label = { Text(label) },
                selected = false,
                onClick = { navController.navigate(route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary, // Usa a cor primária
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant, // Contraste adequado
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}