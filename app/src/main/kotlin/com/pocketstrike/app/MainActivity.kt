package com.pocketstrike.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pocketstrike.app.theme.DarkBase
import com.pocketstrike.app.theme.PocketStrikeTheme
import com.pocketstrike.app.ui.AppearanceScreen
import com.pocketstrike.app.ui.CronTasksScreen
import com.pocketstrike.app.ui.GalleryScreen
import com.pocketstrike.app.ui.HermesMemoryScreen
import com.pocketstrike.app.ui.HomeScreen
import com.pocketstrike.app.ui.McpScreen
import com.pocketstrike.app.ui.ProvidersScreen
import com.pocketstrike.app.ui.ToolsScreen
import com.pocketstrike.app.viewmodel.PocketStrikeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PocketStrikeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketStrikeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBase
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigate = { destination ->
                                    navController.navigate(destination)
                                }
                            )
                        }
                        composable("gallery") {
                            GalleryScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("providers") {
                            ProvidersScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("tools") {
                            ToolsScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("mcp") {
                            McpScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("hermes") {
                            HermesMemoryScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("cron") {
                            CronTasksScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("appearance") {
                            AppearanceScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
