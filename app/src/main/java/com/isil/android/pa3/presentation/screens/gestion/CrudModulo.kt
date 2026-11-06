package com.isil.android.pa3.presentation.screens.gestion

import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.isil.android.pa3.domain.model.Entidad
import com.isil.android.pa3.presentation.components.CrudListScreen
import com.isil.android.pa3.presentation.components.IOSIconBadge
import com.isil.android.pa3.presentation.viewmodel.CrudEvento
import com.isil.android.pa3.presentation.viewmodel.CrudViewModel

class FormHost<T>(
    val inicial: T?,
    val saving: Boolean,
    val onDismiss: () -> Unit,
    val onSave: (T) -> Unit
)

@Composable
fun <T : Entidad> CrudModulo(
    modulo: Modulo,
    viewModel: CrudViewModel<T>,
    onBack: () -> Unit,
    itemName: (T) -> String,
    itemContent: @Composable RowScope.(T) -> Unit,
    form: @Composable (FormHost<T>) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var creando by rememberSaveable { mutableStateOf(false) }
    val cerrarFormulario by rememberUpdatedState { creando = false }

    LaunchedEffect(viewModel) {
        viewModel.eventos.collect { evento ->
            when (evento) {
                is CrudEvento.Mensaje -> Toast.makeText(context, evento.texto, Toast.LENGTH_SHORT).show()
                CrudEvento.Guardado -> cerrarFormulario()
            }
        }
    }

    CrudListScreen(
        title = modulo.titulo,
        items = state.items,
        isLoading = state.isLoading,
        error = state.error,
        itemKey = { it.codigo },
        itemName = itemName,
        onBack = onBack,
        onRefresh = viewModel::cargar,
        onAdd = { creando = true },
        onEdit = { viewModel.seleccionar(it.codigo) },
        onDelete = { viewModel.eliminar(it.codigo) }
    ) { item ->
        IOSIconBadge(modulo.icon, modulo.color, size = 40.dp)
        Spacer(Modifier.width(12.dp))
        itemContent(item)
    }

    val editando = state.seleccionado
    if (creando || editando != null) {
        form(
            FormHost(
                inicial = editando,
                saving = state.isSaving,
                onDismiss = {
                    creando = false
                    viewModel.limpiarSeleccion()
                },
                onSave = viewModel::guardar
            )
        )
    }
}
