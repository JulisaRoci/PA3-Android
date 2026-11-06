package com.isil.android.pa3.presentation.screens.main

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.isil.android.pa3.presentation.components.GlassTab
import com.isil.android.pa3.presentation.components.GlassTabBar
import com.isil.android.pa3.presentation.components.LiquidBackground
import com.isil.android.pa3.presentation.navigation.Routes
import com.isil.android.pa3.presentation.screens.ajustes.AjustesScreen
import com.isil.android.pa3.presentation.screens.entidad.EntidadScreen
import com.isil.android.pa3.presentation.screens.home.HomeScreen

private val tabs = listOf(
    GlassTab(Routes.HOME, "Inicio", Icons.Outlined.Home, Icons.Rounded.Home),
    GlassTab(Routes.ENTIDAD, "Datos", Icons.Outlined.Inventory2, Icons.Rounded.Inventory2),
    GlassTab(Routes.AJUSTES, "Ajustes", Icons.Outlined.Settings, Icons.Rounded.Settings)
)

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.HOME

    LiquidBackground {
        NavHost(
            navController = tabNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.ENTIDAD) { EntidadScreen() }
            composable(Routes.AJUSTES) { AjustesScreen(onLogout = onLogout) }
        }

        GlassTabBar(
            tabs = tabs,
            selectedRoute = currentRoute,
            onSelect = { route ->
                tabNavController.navigate(route) {
                    popUpTo(tabNavController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .fillMaxWidth()
        )
    }
}
