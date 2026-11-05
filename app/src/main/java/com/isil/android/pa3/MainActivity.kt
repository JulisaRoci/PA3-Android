package com.isil.android.pa3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.isil.android.pa3.presentation.navigation.AppNavGraph
import com.isil.android.pa3.presentation.viewmodel.AppViewModelProvider
import com.isil.android.pa3.presentation.viewmodel.MainViewModel
import com.isil.android.pa3.ui.theme.Grupo1PA3Theme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels { AppViewModelProvider.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { mainViewModel.startDestination.value == null }

        enableEdgeToEdge()

        setContent {
            Grupo1PA3Theme {
                val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()
                startDestination?.let { AppNavGraph(startDestination = it) }
            }
        }
    }
}
