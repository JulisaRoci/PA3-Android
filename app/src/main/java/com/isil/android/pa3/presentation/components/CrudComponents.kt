package com.isil.android.pa3.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.isil.android.pa3.domain.model.Catalogo
import com.isil.android.pa3.ui.theme.IOSColors
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatoPrecio(valor: Double): String =
    String.format(Locale.US, "S/ %,.2f", valor)

fun numeroEditable(valor: Double): String =
    BigDecimal.valueOf(valor).stripTrailingZeros().toPlainString()

private fun formatoFechaApi(): SimpleDateFormat =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }

private fun normalizar(texto: String): String =
    java.text.Normalizer.normalize(texto.lowercase(), java.text.Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CrudListScreen(
    title: String,
    items: List<T>,
    isLoading: Boolean,
    error: String?,
    itemKey: (T) -> Long,
    itemName: (T) -> String,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    itemContent: @Composable RowScope.(T) -> Unit
) {
    var porEliminar by remember { mutableStateOf<T?>(null) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val filtrados = remember(items, busqueda) {
        if (busqueda.isBlank()) items else items.filter { normalizar(itemName(it)).contains(normalizar(busqueda.trim())) }
    }

    Box(Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = isLoading && items.isNotEmpty(),
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = topInset + 8.dp, bottom = 200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    BackChip(onClick = onBack)
                    Spacer(Modifier.height(8.dp))
                    LargeTitle(title = title, subtitle = "${items.size} registros")
                    Spacer(Modifier.height(12.dp))
                    GlassTextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        placeholder = "Buscar",
                        leadingIcon = Icons.Rounded.Search,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                    )
                }
                when {
                    isLoading && items.isEmpty() -> item {
                        Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    error != null && items.isEmpty() -> item {
                        MessageCard(Icons.Rounded.CloudOff, IOSColors.Red, "No se pudo cargar", error, "Reintentar", Icons.Rounded.Refresh, onRefresh)
                    }
                    filtrados.isEmpty() -> item {
                        MessageCard(
                            Icons.Rounded.Inbox, IOSColors.Gray, "Sin registros",
                            if (busqueda.isBlank()) "Aún no hay registros." else "No hay coincidencias para \"$busqueda\".",
                            "Agregar", Icons.Rounded.Add, onAdd
                        )
                    }
                    else -> items(filtrados, key = itemKey) { item ->
                        CrudItemCard(onClick = { onEdit(item) }, onDelete = { porEliminar = item }) {
                            itemContent(item)
                        }
                    }
                }
            }
        }

        GlassFab(
            onClick = onAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 24.dp, bottom = 100.dp)
        )
    }

    porEliminar?.let { item ->
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
            title = { Text("¿Eliminar registro?", fontWeight = FontWeight.SemiBold) },
            text = { Text("Se eliminará \"${itemName(item)}\".") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(item)
                    porEliminar = null
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { porEliminar = null }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@Composable
fun BackChip(onClick: () -> Unit, text: String = "Gestión") {
    Row(
        modifier = Modifier
            .liquidGlass(CircleShape, elevation = 2.dp)
            .clickable(onClick = onClick)
            .padding(start = 10.dp, end = 14.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.ArrowBackIos,
            contentDescription = "Volver",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(text, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun CrudItemCard(
    onClick: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlass(RoundedCornerShape(24.dp), elevation = 6.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
        IconButton(onClick = onDelete) {
            Icon(Icons.Rounded.DeleteOutline, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun ItemTexts(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    detail: String? = null
) {
    Column(modifier) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (subtitle.isNotBlank()) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (!detail.isNullOrBlank()) {
            Text(
                detail,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MessageCard(
    icon: ImageVector,
    color: Color,
    title: String,
    text: String,
    actionText: String,
    actionIcon: ImageVector,
    onAction: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            IOSIconBadge(icon, color, size = 56.dp)
            Spacer(Modifier.height(14.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(6.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            GlassButton(text = actionText, icon = actionIcon, onClick = onAction)
        }
    }
}

@Composable
fun GlassFab(onClick: () -> Unit, modifier: Modifier = Modifier, icon: ImageVector = Icons.Rounded.Add) {
    val color = MaterialTheme.colorScheme.primary
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.9f else 1f, label = "fab")
    Box(
        modifier = modifier
            .size(60.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(16.dp, CircleShape, ambientColor = color.copy(alpha = 0.3f), spotColor = color.copy(alpha = 0.6f))
            .clip(CircleShape)
            .background(Brush.verticalGradient(listOf(lerp(color, Color.White, 0.25f), color)))
            .border(1.dp, Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.7f), Color.White.copy(alpha = 0.05f))), CircleShape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = "Agregar", tint = Color.White, modifier = Modifier.size(30.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudFormSheet(
    title: String,
    saving: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                }
                Text(
                    title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(88.dp))
            }
            content()
            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(4.dp))
            GlassProminentButton(text = "Guardar", icon = Icons.Rounded.Check, loading = saving, onClick = onSave)
        }
    }
}

@Composable
private fun FieldLabel(label: String) {
    Text(
        label.uppercase(),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
    )
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    placeholder: String = label
) {
    val focusManager = LocalFocusManager.current
    Column {
        FieldLabel(label)
        GlassTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            leadingIcon = icon,
            isPassword = isPassword,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}

@Composable
private fun GlassPickerRow(
    text: String,
    isPlaceholder: Boolean,
    icon: ImageVector,
    trailing: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .liquidGlass(RoundedCornerShape(18.dp), elevation = 0.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(
            text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isPlaceholder) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(trailing, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SelectField(
    label: String,
    selected: Catalogo?,
    options: List<Catalogo>,
    onSelect: (Catalogo) -> Unit,
    icon: ImageVector
) {
    var abierto by remember { mutableStateOf(false) }
    Column {
        FieldLabel(label)
        GlassPickerRow(
            text = selected?.nombre?.takeIf { it.isNotBlank() } ?: "Seleccionar",
            isPlaceholder = selected == null,
            icon = icon,
            trailing = Icons.Rounded.ExpandMore,
            onClick = { abierto = true }
        )
    }
    if (abierto) {
        SelectDialog(
            title = label,
            options = options,
            selectedCodigo = selected?.codigo,
            onDismiss = { abierto = false },
            onSelect = {
                onSelect(it)
                abierto = false
            }
        )
    }
}

@Composable
private fun SelectDialog(
    title: String,
    options: List<Catalogo>,
    selectedCodigo: Long?,
    onDismiss: () -> Unit,
    onSelect: (Catalogo) -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    val filtradas = remember(options, busqueda) {
        if (busqueda.isBlank()) options else options.filter { normalizar(it.nombre).contains(normalizar(busqueda.trim())) }
    }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            if (options.size > 6) {
                GlassTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = "Buscar",
                    leadingIcon = Icons.Rounded.Search
                )
            }
            if (options.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            LazyColumn(Modifier.weight(1f, fill = false)) {
                items(filtradas, key = { it.codigo }) { opcion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onSelect(opcion) }
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            opcion.nombre,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (opcion.codigo == selectedCodigo) {
                            Icon(Icons.Rounded.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                }
            }
            TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Cerrar", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    Column {
        FieldLabel(label)
        GlassPickerRow(
            text = value.ifBlank { "AAAA-MM-DD" },
            isPlaceholder = value.isBlank(),
            icon = Icons.Rounded.CalendarMonth,
            trailing = Icons.Rounded.ExpandMore,
            onClick = { abierto = true }
        )
    }
    if (abierto) {
        val inicial = remember(value) { runCatching { formatoFechaApi().parse(value)?.time }.getOrNull() }
        val state = rememberDatePickerState(initialSelectedDateMillis = inicial)
        DatePickerDialog(
            onDismissRequest = { abierto = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onValueChange(formatoFechaApi().format(Date(it))) }
                    abierto = false
                }) { Text("Aceptar", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { abierto = false }) { Text("Cancelar", color = MaterialTheme.colorScheme.primary) }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
