package com.example.mygamingdatabase.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DrawerContent(navController: NavHostController, onCloseDrawer: () -> Unit) {
    val context = LocalContext.current

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
                text = "Menu",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Perfil",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCloseDrawer()
                    }
                    .padding(vertical = 8.dp)
            )

            // Settings
            Text(
                text = "Configurações",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("settings")
                        onCloseDrawer()
                    }
                    .padding(vertical = 8.dp)
            )

            // Help
            Text(
                text = "Ajuda e Perguntas Frequentes",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("help")
                        onCloseDrawer()
                    }
                    .padding(vertical = 8.dp)
            )

            // Logout
            Text(
                text = "Sair",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .clickable {
                        Toast.makeText(context, "Você saiu. Até mais!", Toast.LENGTH_SHORT).show()
                        onCloseDrawer()
                        // onDrawerItemClick()
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Versão 0.0.0.1",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}
