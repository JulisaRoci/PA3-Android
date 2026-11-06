package com.isil.android.pa3.presentation.screens.gestion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isil.android.pa3.presentation.components.GlassCard
import com.isil.android.pa3.presentation.components.IOSIconBadge
import com.isil.android.pa3.presentation.components.LargeTitle
import com.isil.android.pa3.presentation.components.liquidGlass
import com.isil.android.pa3.ui.theme.IOSColors

@Composable
fun GestionScreen(onOpen: (Modulo) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        LargeTitle(title = "Gestión", subtitle = "Rápidos y Furiosos")
        Spacer(Modifier.height(20.dp))

        SectionTitle("Taller")
        Modulo.principales.chunked(2).forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                fila.forEach { modulo ->
                    ModuloTile(modulo, Modifier.weight(1f)) { onOpen(modulo) }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        SectionTitle("Catálogos")
        GlassCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), contentPadding = 0.dp) {
            Modulo.catalogos.forEachIndexed { index, modulo ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 60.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 54.dp)
                        .clickable { onOpen(modulo) }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    IOSIconBadge(modulo.icon, modulo.color)
                    Text(
                        modulo.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = IOSColors.Gray2,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(120.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    )
}

@Composable
private fun ModuloTile(modulo: Modulo, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .heightIn(min = 140.dp)
            .liquidGlass(RoundedCornerShape(26.dp), elevation = 8.dp)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        IOSIconBadge(modulo.icon, modulo.color, size = 40.dp)
        Spacer(Modifier.height(12.dp))
        Text(modulo.titulo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(2.dp))
        Text(
            modulo.descripcion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
