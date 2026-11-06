package com.isil.android.pa3.presentation.screens.ajustes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isil.android.pa3.presentation.components.GlassCard
import com.isil.android.pa3.presentation.components.IOSIconBadge
import com.isil.android.pa3.presentation.components.InitialsAvatar
import com.isil.android.pa3.presentation.components.LargeTitle
import com.isil.android.pa3.presentation.viewmodel.AjustesViewModel
import com.isil.android.pa3.presentation.viewmodel.AppViewModelProvider
import com.isil.android.pa3.ui.theme.IOSColors

@Composable
fun AjustesScreen(
    onLogout: () -> Unit,
    viewModel: AjustesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val session by viewModel.session.collectAsStateWithLifecycle()
    val loggedOut by viewModel.loggedOut.collectAsStateWithLifecycle()
    var showConfirm by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(loggedOut) {
        if (loggedOut) onLogout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        LargeTitle(title = "Ajustes")
        Spacer(Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth(), contentPadding = 16.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                InitialsAvatar(name = session?.fullName.orEmpty(), size = 60.dp)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        session?.fullName.orEmpty(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        session?.email.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        SectionHeader("General")
        SettingsGroup {
            SettingsRow(Icons.Rounded.Person, IOSColors.Blue, "Cuenta", value = "@${session?.username.orEmpty()}")
            RowDivider()
            SettingsRow(Icons.Rounded.Badge, IOSColors.Orange, "Rol", value = session?.role.orEmpty(), showChevron = false)
            RowDivider()
            SettingsRow(Icons.Rounded.Notifications, IOSColors.Red, "Notificaciones", value = "Activadas")
            RowDivider()
            SettingsRow(Icons.Rounded.Palette, IOSColors.Indigo, "Apariencia", value = "Sistema")
        }

        SectionHeader("Información")
        SettingsGroup {
            SettingsRow(Icons.Rounded.DirectionsCar, IOSColors.Green, "Aplicación", value = "Rápidos y Furiosos", showChevron = false)
            RowDivider()
            SettingsRow(Icons.Rounded.Info, IOSColors.Gray, "Versión", value = "1.0", showChevron = false)
        }

        Spacer(Modifier.height(28.dp))
        SettingsGroup {
            SettingsRow(
                icon = Icons.AutoMirrored.Rounded.Logout,
                color = IOSColors.Red,
                title = "Cerrar sesión",
                titleColor = MaterialTheme.colorScheme.error,
                showChevron = false,
                onClick = { showConfirm = true }
            )
        }
        Text(
            "Al cerrar sesión se eliminará el token guardado en este dispositivo.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(120.dp))
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
            title = { Text("¿Cerrar sesión?", fontWeight = FontWeight.SemiBold) },
            text = { Text("Se eliminará el token de acceso y volverás a la pantalla de inicio de sesión.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    viewModel.logout()
                }) {
                    Text("Cerrar sesión", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 16.dp, top = 28.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        contentPadding = 0.dp,
        content = content
    )
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    color: Color,
    title: String,
    value: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    showChevron: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        IOSIconBadge(icon, color)
        Text(title, style = MaterialTheme.typography.bodyLarge, color = titleColor, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (showChevron) {
            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = IOSColors.Gray2,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 60.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outline
    )
}
