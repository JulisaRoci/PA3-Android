package com.isil.android.pa3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Activa la pantalla de bienvenida
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // Contenido principal de la aplicación
        setContent {
            MaterialTheme {
                Surface {
                    Text(text = "Bienvenido a Gestión de Cursos")
                }
            }
        }
    }
}