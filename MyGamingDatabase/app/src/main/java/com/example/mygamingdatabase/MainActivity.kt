package com.example.mygamingdatabase

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mygamingdatabase.data.GameRepository
import com.example.mygamingdatabase.data.RetrofitInstance
import com.example.mygamingdatabase.viewmodel.GameViewModel
import com.example.mygamingdatabase.viewmodel.GameViewModelFactory
import com.example.mygamingdatabase.ui.theme.MyGamingDatabaseTheme
import com.example.mygamingdatabase.ui.components.BottomNavBar
import com.example.mygamingdatabase.ui.components.DrawerContent
import com.example.mygamingdatabase.ui.components.TopBar
import com.example.mygamingdatabase.ui.view.ForgotPasswordScreen
import com.example.mygamingdatabase.ui.view.SettingsScreen
import com.example.mygamingdatabase.ui.view.HelpScreen
import com.example.mygamingdatabase.ui.view.ListsScreen
import com.example.mygamingdatabase.ui.view.HomeScreen
import com.example.mygamingdatabase.ui.view.GameDetailsScreen
import com.example.mygamingdatabase.ui.view.LoginScreen
import com.example.mygamingdatabase.ui.view.ProfileScreen
import com.example.mygamingdatabase.ui.view.RegisterScreen

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = GameRepository()
        val viewModel: GameViewModel = ViewModelProvider(
            this,
            GameViewModelFactory(
                this,
                repository
            )
        )[GameViewModel::class.java]

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

            var isLogged by remember { mutableStateOf(false) }

            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            val currentRoute by navController.currentBackStackEntryAsState()

            MyGamingDatabaseTheme (darkTheme = isDarkTheme){
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = true,
                    drawerContent = {
                        if (!listOf("login", "forgot", "register").contains(currentRoute?.destination?.route)) {
                            DrawerContent(
                                viewModel,
                                navController,
                                isLogged,
                                onLogout = {
                                    isLogged = false
                                    scope.launch { drawerState.close() }
                                }
                            ) {
                                scope.launch { drawerState.close() }
                            }
                        }
                    },
                    content = {
                        Scaffold(
                            topBar = {
                                if (!listOf("login", "forgot", "register").contains(currentRoute?.destination?.route)) {
                                    TopBar(
                                        onOpenDrawer = { scope.launch { drawerState.open() } },
                                        onNavigateToHome = {
                                            navController.navigate("home") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    )
                                }
                            },
                            bottomBar =  {
                                if (!listOf("login", "forgot", "register").contains(currentRoute?.destination?.route)) {
                                    BottomNavBar(navController)
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = "login",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable(
                                    "login",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    LoginScreen(viewModel, navController) {
                                        isLogged = true
                                    }
                                }
                                composable(
                                    "register",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    RegisterScreen(viewModel, navController)
                                }
                                composable(
                                    "forgot",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    ForgotPasswordScreen(viewModel, navController)
                                }
                                composable(
                                    "profile",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    ProfileScreen(viewModel, navController, isLogged)
                                }
                                composable(
                                    "home",
                                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                                ) {
                                    scope.launch { drawerState.close() }
                                    HomeScreen(
                                        navController,
                                        context = LocalContext.current,
                                        viewModel = viewModel,
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