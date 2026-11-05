package com.isil.android.pa3.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isil.android.pa3.ui.theme.IOSColors

@Composable
fun LiquidBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val dark = isSystemInDarkTheme()
    val transition = rememberInfiniteTransition(label = "liquid")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(12_000, easing = LinearEasing), RepeatMode.Reverse),
        label = "t"
    )
    val base = if (dark) listOf(Color(0xFF000000), Color(0xFF0A0A14))
    else listOf(IOSColors.Gray6, Color(0xFFE8EEF9))
    val blobAlpha = if (dark) 0.50f else 0.42f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(base))
            .drawBehind {
                val w = size.width
                val h = size.height
                fun blob(color: Color, cx: Float, cy: Float, radius: Float) {
                    val center = Offset(cx, cy)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(color.copy(alpha = blobAlpha), Color.Transparent),
                            center = center,
                            radius = radius
                        ),
                        radius = radius,
                        center = center
                    )
                }
                blob(IOSColors.Blue, w * (0.10f + 0.25f * t), h * 0.15f, w * 0.80f)
                blob(IOSColors.Purple, w * (0.95f - 0.20f * t), h * (0.38f + 0.06f * t), w * 0.70f)
                blob(IOSColors.Pink, w * (0.20f + 0.10f * t), h * (0.72f - 0.08f * t), w * 0.60f)
                blob(IOSColors.Teal, w * 0.90f, h * (0.95f - 0.05f * t), w * 0.70f)
            },
        content = content
    )
}

@Composable
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(28.dp),
    tint: Color = Color.Unspecified,
    elevation: Dp = 10.dp
): Modifier {
    val dark = isSystemInDarkTheme()
    val fill = if (dark) listOf(Color.White.copy(alpha = 0.16f), Color.White.copy(alpha = 0.06f))
    else listOf(Color.White.copy(alpha = 0.78f), Color.White.copy(alpha = 0.45f))
    val rim = if (dark) listOf(Color.White.copy(alpha = 0.40f), Color.White.copy(alpha = 0.04f), Color.White.copy(alpha = 0.20f))
    else listOf(Color.White, Color.White.copy(alpha = 0.30f), Color.White.copy(alpha = 0.85f))

    return this
        .shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.06f),
            spotColor = Color.Black.copy(alpha = if (dark) 0.45f else 0.12f)
        )
        .clip(shape)
        .background(Brush.verticalGradient(fill))
        .then(if (tint.isSpecified) Modifier.background(tint.copy(alpha = 0.14f)) else Modifier)
        .border(1.dp, Brush.linearGradient(rim), shape)
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    contentPadding: Dp = 20.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .liquidGlass(shape)
            .padding(contentPadding),
        content = content
    )
}

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var visible by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(18.dp)
    val accent = MaterialTheme.colorScheme.primary

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .liquidGlass(shape, elevation = 0.dp),
        enabled = enabled,
        singleLine = true,
        shape = shape,
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = accent) },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = if (visible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = accent
        )
    )
}

@Composable
fun GlassProminentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null
) {
    val shape = CircleShape
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "press")
    val alpha = if (enabled) 1f else 0.5f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .shadow(14.dp, shape, ambientColor = color.copy(alpha = 0.3f), spotColor = color.copy(alpha = 0.55f))
            .clip(shape)
            .background(Brush.verticalGradient(listOf(lerp(color, Color.White, 0.22f), color)))
            .border(
                1.dp,
                Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.65f), Color.White.copy(alpha = 0.05f))),
                shape
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = enabled && !loading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.5.dp, modifier = Modifier.size(22.dp))
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (icon != null) Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Text(text, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            }
        }
    }
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentColor: Color = MaterialTheme.colorScheme.primary
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f, label = "press")
    Row(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .liquidGlass(CircleShape, elevation = 4.dp)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
    ) {
        if (icon != null) Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
        Text(text, color = contentColor, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

@Composable
fun IOSIconBadge(
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.27f))
            .background(Brush.verticalGradient(listOf(lerp(color, Color.White, 0.15f), color))),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(size * 0.6f))
    }
}

@Composable
fun InitialsAvatar(name: String, modifier: Modifier = Modifier, size: Dp = 56.dp) {
    val initials = name.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }.ifBlank { "?" }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(IOSColors.Cyan, IOSColors.Blue, IOSColors.Indigo)))
            .border(1.5.dp, Color.White.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = (size.value * 0.38f).sp)
    }
}

@Composable
fun LargeTitle(title: String, modifier: Modifier = Modifier, subtitle: String? = null) {
    Column(modifier) {
        if (subtitle != null) {
            Text(
                subtitle.uppercase(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(title, style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onBackground)
    }
}

data class GlassTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun GlassTabBar(
    tabs: List<GlassTab>,
    selectedRoute: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .liquidGlass(CircleShape, elevation = 18.dp)
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEach { tab ->
            val selected = tab.route == selectedRoute
            val accent = MaterialTheme.colorScheme.primary
            val bg by animateColorAsState(
                if (selected) accent.copy(alpha = 0.14f) else Color.Transparent, label = "tabBg"
            )
            val fg by animateColorAsState(
                if (selected) accent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f), label = "tabFg"
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(bg)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onSelect(tab.route) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    if (selected) tab.selectedIcon else tab.icon,
                    contentDescription = tab.label,
                    tint = fg,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    tab.label,
                    fontSize = 11.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    color = fg
                )
            }
        }
    }
}
