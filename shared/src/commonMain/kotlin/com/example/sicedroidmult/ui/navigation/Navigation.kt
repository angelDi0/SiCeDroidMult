package com.example.sicedroidmult.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sicedroidmult.ui.screens.HomeScreen
import com.example.sicedroidmult.ui.screens.LoginScreen
import com.example.sicedroidmult.ui.screens.MenuScreen
import com.example.sicedroidmult.viewmodel.SNViewModel

@Composable
fun SNApp() {
    val navController = rememberNavController()
    val snViewModel: SNViewModel = viewModel { SNViewModel() }

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = snViewModel,
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    viewModel = snViewModel,
                    onNavigateToMenu = {
                        navController.navigate("menu")
                    }
                )
            }

            composable("menu") {
                MenuScreen(
                    viewModel = snViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}