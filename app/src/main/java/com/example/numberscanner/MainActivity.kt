package com.example.numberscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.numberscanner.data.database.AppDatabase
import com.example.numberscanner.data.repository.ScanRepository
import com.example.numberscanner.ui.screens.DatabaseRecordsScreen
import com.example.numberscanner.ui.screens.ScannerScreen
import com.example.numberscanner.ui.viewmodel.RecordsViewModel
import com.example.numberscanner.ui.viewmodel.RecordsViewModelFactory
import com.example.numberscanner.ui.viewmodel.ScannerViewModel
import com.example.numberscanner.ui.viewmodel.ScannerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = ScanRepository(database.scanRecordDao())

        setContent {
            MaterialTheme {
                MainScreen(repository)
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Scanner : Screen("scanner", "SCANNER", Icons.Default.Search)
    object Database : Screen("database", "DATABASE", Icons.AutoMirrored.Filled.List)
}

@Composable
fun MainScreen(repository: ScanRepository) {
    val navController = rememberNavController()
    val items = listOf(Screen.Scanner, Screen.Database)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label, fontSize = 10.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color(0xFF303F9F),
                            indicatorColor = Color(0xFF303F9F),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Scanner.route, Modifier.padding(innerPadding)) {
            composable(Screen.Scanner.route) {
                val scannerViewModel: ScannerViewModel = viewModel(
                    factory = ScannerViewModelFactory(repository)
                )
                ScannerScreen(scannerViewModel)
            }
            composable(Screen.Database.route) {
                val recordsViewModel: RecordsViewModel = viewModel(
                    factory = RecordsViewModelFactory(repository)
                )
                DatabaseRecordsScreen(recordsViewModel)
            }
        }
    }
}
