package com.isil.android.pa3.presentation.screens.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isil.android.pa3.presentation.components.GlassButton
import com.isil.android.pa3.presentation.components.GlassCard
import com.isil.android.pa3.presentation.components.IOSIconBadge
import com.isil.android.pa3.presentation.components.InitialsAvatar
import com.isil.android.pa3.presentation.components.LargeTitle
import com.isil.android.pa3.presentation.components.liquidGlass
import com.isil.android.pa3.presentation.viewmodel.AppViewModelProvider
import com.isil.android.pa3.presentation.viewmodel.HomeViewModel
import com.isil.android.pa3.ui.theme.IOSColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val session by viewModel.session.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val today = remember {
        SimpleDateFormat("EEEE, d 'de' MMMM", Locale.forLanguageTag("es-PE")).format(Date())
    }
    val token = session?.token.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        LargeTitle(title = "Inicio", subtitle = today)
        Spacer(Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                InitialsAvatar(name = session?.fullName.orEmpty(), size = 52.dp)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        "Hola, ${session?.fullName?.substringBefore(" ").orEmpty()} 👋",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Sesión iniciada como @${session?.username.orEmpty()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IOSIconBadge(Icons.Rounded.Key, IOSColors.Orange)
                Spacer(Modifier.width(12.dp))
                Text(
                    "Token de sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusPill(text = "Activo", color = IOSColors.Green)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "Generado por la API al iniciar sesión y guardado con TokenManager.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .liquidGlass(RoundedCornerShape(16.dp), elevation = 0.dp)
                    .padding(14.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = token.ifEmpty { "—" },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            GlassButton(
                text = "Copiar token",
                icon = Icons.Rounded.ContentCopy,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("token", token))
                    Toast.makeText(context, "Token copiado", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoTile(
                icon = Icons.Rounded.VerifiedUser,
                color = IOSColors.Green,
                title = "Estado",
                value = "Autenticado",
                modifier = Modifier.weight(1f)
            )
            InfoTile(
                icon = Icons.Rounded.Numbers,
                color = IOSColors.Indigo,
                title = "Longitud",
                value = "${token.length} caracteres",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(120.dp))
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(color))
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun InfoTile(
    icon: ImageVector,
    color: Color,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier, shape = RoundedCornerShape(24.dp), contentPadding = 16.dp) {
        IOSIconBadge(icon, color, size = 32.dp)
        Spacer(Modifier.height(10.dp))
        Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}
