package com.example.mygamingdatabase

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygamingdatabase.models.GameViewModel
import com.example.mygamingdatabase.models.GameViewModelFactory
import com.example.mygamingdatabase.ui.theme.MyGamingDatabaseTheme
import com.example.mygamingdatabase.ui.components.BottomNavBar
import com.example.mygamingdatabase.ui.components.DrawerContent
import com.example.mygamingdatabase.ui.components.TopBar
import com.example.mygamingdatabase.ui.screens.SettingsScreen
import com.example.mygamingdatabase.ui.screens.HelpScreen
import com.example.mygamingdatabase.ui.screens.ListsScreen
import com.example.mygamingdatabase.ui.screens.HomeScreen
import com.example.mygamingdatabase.ui.screens.GameDetailsScreen

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        // UI
        setContent {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            MyGamingDatabaseTheme (darkTheme = isDarkTheme){
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = true,
                    drawerContent = {
                        DrawerContent(navController) { scope.launch { drawerState.close() } }
                    },
                    content = {
                        Scaffold(
                            topBar = {
                                TopBar(
                                    onOpenDrawer = { scope.launch { drawerState.open() } },
                                    onNavigateToHome = {
                                        navController.navigate("home") {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        }
                                    }
                                )
                            },
                            bottomBar =  { BottomNavBar(navController) }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = "home",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable(
                                    "home",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    HomeScreen(
                                        navController,
                                        context = LocalContext.current,
                                        onGameSelected = { game ->
                                            navController.navigate("details/${game.name}")
                                        }
                                    )
                                }
                                composable(
                                    "list",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    ListsScreen(navController)
                                }
                                composable(
                                    "gameDetails/{gameId}",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) { backStackEntry ->
                                    val gameId = backStackEntry.arguments?.getString("gameId")
                                    if (gameId != null) {
                                        GameDetailsScreen(gameId = gameId)
                                    }
                                }
                                composable(
                                    "help",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    HelpScreen()
                                }
                                composable(
                                    "settings",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    SettingsScreen(
                                        isDarkTheme = isDarkTheme,
                                        onThemeChange = {
                                            viewModel.toggleTheme(this@MainActivity)
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}