package com.example.mygamingdatabase.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mygamingdatabase.R

@ExperimentalMaterial3Api
@Composable
fun TopBar(
    onThemeToggle: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.topbar_icon), // Platform icon
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) { // Open menu by clicking on the icon
                Icon(Icons.Default.Menu, contentDescription = "Open Menu")
            }
        }
    )
}