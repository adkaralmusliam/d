package com.xsandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xsandbox.ui.screens.AppListScreen
import com.xsandbox.ui.screens.HomeScreen
import com.xsandbox.ui.theme.XSandboxTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize logging
        Timber.plant(Timber.DebugTree())
        Timber.i("MainActivity created")

        setContent {
            XSandboxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    XSandboxNavigation()
                }
            }
        }
    }
}

@Composable
fun XSandboxNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToAppList = {
                    navController.navigate("app_list")
                }
            )
        }
        composable("app_list") {
            AppListScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onAppSelected = { packageName ->
                    // Navigate to sandbox creation or profile selection
                    navController.navigate("sandbox_creation/$packageName")
                }
            )
        }
        composable("sandbox_creation/{packageName}") { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: return@composable
            // SandboxCreationScreen will be implemented next
            // For now, show a placeholder
        }
    }
}
