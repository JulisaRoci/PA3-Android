package com.isil.android.pa3.presentation.screens.gestion

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material.icons.rounded.Wc
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isil.android.pa3.domain.model.Catalogo
import com.isil.android.pa3.domain.model.Cliente
import com.isil.android.pa3.domain.model.Empleado
import com.isil.android.pa3.domain.model.Servicio
import com.isil.android.pa3.domain.model.TipoCatalogo
import com.isil.android.pa3.domain.model.Vehiculo
import com.isil.android.pa3.presentation.components.CrudFormSheet
import com.isil.android.pa3.presentation.components.DateField
import com.isil.android.pa3.presentation.components.FormField
import com.isil.android.pa3.presentation.components.ItemTexts
import com.isil.android.pa3.presentation.components.SelectField
import com.isil.android.pa3.presentation.components.formatoPrecio
import com.isil.android.pa3.presentation.components.numeroEditable
import com.isil.android.pa3.presentation.viewmodel.AppViewModelProvider
import com.isil.android.pa3.presentation.viewmodel.CrudViewModel
import com.isil.android.pa3.presentation.viewmodel.OpcionesViewModel

@Composable
fun ModuloScreen(modulo: Modulo, onBack: () -> Unit) {
    val tipo = modulo.catalogo
    when {
        tipo != null -> CatalogoScreen(modulo, tipo, onBack)
        modulo == Modulo.SERVICIOS -> ServiciosScreen(modulo, onBack)
        modulo == Modulo.VEHICULOS -> VehiculosScreen(modulo, onBack)
        modulo == Modulo.CLIENTES -> ClientesScreen(modulo, onBack)
        modulo == Modulo.EMPLEADOS -> EmpleadosScreen(modulo, onBack)
    }
}

private fun String.aDecimal(): Double? = trim().replace(',', '.').toDoubleOrNull()

@Composable
private fun PrecioText(valor: Double) {
    Text(formatoPrecio(valor), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun CatalogoScreen(modulo: Modulo, tipo: TipoCatalogo, onBack: () -> Unit) {
    val vm: CrudViewModel<Catalogo> = viewModel(
        key = "crud-${tipo.name}",
        factory = AppViewModelProvider.crud { it.catalogos.getValue(tipo) }
    )
    CrudModulo(
        modulo = modulo,
        viewModel = vm,
        onBack = onBack,
        itemName = { it.nombre },
        itemContent = { ItemTexts(it.nombre, "Código ${it.codigo}", Modifier.weight(1f)) }
    ) { host ->
        var nombre by rememberSaveable(host.inicial) { mutableStateOf(host.inicial?.nombre.orEmpty()) }
        CrudFormSheet(
            title = if (host.inicial == null) "Nuevo ${tipo.singular}" else "Editar ${tipo.singular}",
            saving = host.saving,
            error = null,
            onDismiss = host.onDismiss,
            onSave = { host.onSave(Catalogo(codigo = host.inicial?.codigo ?: 0L, nombre = nombre.trim())) }
        ) {
            FormField("Nombre", nombre, { nombre = it }, Icons.AutoMirrored.Rounded.Label, imeAction = ImeAction.Done)
        }
    }
}

@Composable
private fun ServiciosScreen(modulo: Modulo, onBack: () -> Unit) {
    val vm: CrudViewModel<Servicio> = viewModel(key = "crud-servicios", factory = AppViewModelProvider.crud { it.servicios })
    CrudModulo(
        modulo = modulo,
        viewModel = vm,
        onBack = onBack,
        itemName = { it.nombre },
        itemContent = {
            ItemTexts(it.nombre, it.descripcion, Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            PrecioText(it.precio)
        }
    ) { host ->
        val inicial = host.inicial
        var nombre by rememberSaveable(inicial) { mutableStateOf(inicial?.nombre.orEmpty()) }
        var descripcion by rememberSaveable(inicial) { mutableStateOf(inicial?.descripcion.orEmpty()) }
        var precio by rememberSaveable(inicial) { mutableStateOf(inicial?.precio?.let(::numeroEditable).orEmpty()) }
        var error by remember { mutableStateOf<String?>(null) }
        CrudFormSheet(
            title = if (inicial == null) "Nuevo servicio" else "Editar servicio",
            saving = host.saving,
            error = error,
            onDismiss = host.onDismiss,
            onSave = {
                val precioNum = precio.aDecimal()
                error = if (precioNum == null) "Ingresa un precio válido" else null
                if (precioNum != null) {
                    host.onSave(Servicio(inicial?.codigo ?: 0L, nombre.trim(), descripcion.trim(), precioNum))
                }
            }
        ) {
            FormField("Nombre", nombre, { nombre = it }, Icons.AutoMirrored.Rounded.Label)
            FormField("Descripción", descripcion, { descripcion = it }, Icons.Rounded.Description)
            FormField("Precio (S/)", precio, { precio = it }, Icons.Rounded.Payments, KeyboardType.Decimal, ImeAction.Done)
        }
    }
}

@Composable
private fun VehiculosScreen(modulo: Modulo, onBack: () -> Unit) {
    val vm: CrudViewModel<Vehiculo> = viewModel(key = "crud-vehiculos", factory = AppViewModelProvider.crud { it.vehiculos })
    val opcionesVm: OpcionesViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val opciones by opcionesVm.uiState.collectAsStateWithLifecycle()
    CrudModulo(
        modulo = modulo,
        viewModel = vm,
        onBack = onBack,
        itemName = { "${it.placa} ${it.modelo}" },
        itemContent = {
            ItemTexts(
                title = it.placa,
                subtitle = listOfNotNull(it.marca?.nombre, it.modelo, it.anio.toString(), it.color?.nombre).joinToString(" · "),
                detail = it.cliente?.nombre?.let { nombre -> "Dueño: $nombre" },
                modifier = Modifier.weight(1f)
            )
        }
    ) { host ->
        LaunchedEffect(Unit) { opcionesVm.cargar(listOf(TipoCatalogo.MARCA, TipoCatalogo.COLOR), incluirClientes = true) }
        val inicial = host.inicial
        var placa by rememberSaveable(inicial) { mutableStateOf(inicial?.placa.orEmpty()) }
        var modelo by rememberSaveable(inicial) { mutableStateOf(inicial?.modelo.orEmpty()) }
        var anio by rememberSaveable(inicial) { mutableStateOf(inicial?.anio?.takeIf { it > 0 }?.toString().orEmpty()) }
        var marca by remember(inicial) { mutableStateOf(inicial?.marca) }
        var color by remember(inicial) { mutableStateOf(inicial?.color) }
        var cliente by remember(inicial) { mutableStateOf(inicial?.cliente) }
        var error by remember { mutableStateOf<String?>(null) }
        CrudFormSheet(
            title = if (inicial == null) "Nuevo vehículo" else "Editar vehículo",
            saving = host.saving,
            error = error,
            onDismiss = host.onDismiss,
            onSave = {
                val anioNum = anio.trim().toIntOrNull()
                error = if (anioNum == null) "Ingresa un año válido" else null
                if (anioNum != null) {
                    host.onSave(
                        Vehiculo(inicial?.codigo ?: 0L, placa.trim().uppercase(), modelo.trim(), anioNum, marca, color, cliente)
                    )
                }
            }
        ) {
            FormField("Placa", placa, { placa = it }, Icons.Rounded.DirectionsCar)
            SelectField("Marca", marca, opciones.catalogos[TipoCatalogo.MARCA].orEmpty(), { marca = it }, Icons.Rounded.Sell)
            FormField("Modelo", modelo, { modelo = it }, Icons.AutoMirrored.Rounded.Label)
            FormField("Año", anio, { anio = it }, Icons.Rounded.CalendarMonth, KeyboardType.Number, ImeAction.Done)
            SelectField("Color", color, opciones.catalogos[TipoCatalogo.COLOR].orEmpty(), { color = it }, Icons.Rounded.Palette)
            SelectField("Cliente (dueño)", cliente, opciones.clientes, { cliente = it }, Icons.Rounded.Person)
        }
    }
}

@Composable
private fun ClientesScreen(modulo: Modulo, onBack: () -> Unit) {
    val vm: CrudViewModel<Cliente> = viewModel(key = "crud-clientes", factory = AppViewModelProvider.crud { it.clientes })
    val opcionesVm: OpcionesViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val opciones by opcionesVm.uiState.collectAsStateWithLifecycle()
    CrudModulo(
        modulo = modulo,
        viewModel = vm,
        onBack = onBack,
        itemName = { it.nombreCompleto },
        itemContent = {
            ItemTexts(
                title = it.nombreCompleto,
                subtitle = listOfNotNull(it.tipoDocumento?.nombre, it.numeroDocumento).joinToString(" "),
                detail = listOf(it.celular, it.correo).filter { s -> s.isNotBlank() }.joinToString(" · "),
                modifier = Modifier.weight(1f)
            )
        }
    ) { host ->
        LaunchedEffect(Unit) {
            opcionesVm.cargar(listOf(TipoCatalogo.TIPO_DOCUMENTO, TipoCatalogo.SEXO, TipoCatalogo.DISTRITO))
        }
        val inicial = host.inicial
        var nombre by rememberSaveable(inicial) { mutableStateOf(inicial?.nombre.orEmpty()) }
        var apellidoPaterno by rememberSaveable(inicial) { mutableStateOf(inicial?.apellidoPaterno.orEmpty()) }
        var apellidoMaterno by rememberSaveable(inicial) { mutableStateOf(inicial?.apellidoMaterno.orEmpty()) }
        var tipoDocumento by remember(inicial) { mutableStateOf(inicial?.tipoDocumento) }
        var numeroDocumento by rememberSaveable(inicial) { mutableStateOf(inicial?.numeroDocumento.orEmpty()) }
        var fecha by rememberSaveable(inicial) { mutableStateOf(inicial?.fechaNacimiento.orEmpty()) }
        var sexo by remember(inicial) { mutableStateOf(inicial?.sexo) }
        var nacionalidad by rememberSaveable(inicial) { mutableStateOf(inicial?.nacionalidad ?: "Peruana") }
        var distrito by remember(inicial) { mutableStateOf(inicial?.distrito) }
        var direccion by rememberSaveable(inicial) { mutableStateOf(inicial?.direccion.orEmpty()) }
        var telefono by rememberSaveable(inicial) { mutableStateOf(inicial?.telefono.orEmpty()) }
        var celular by rememberSaveable(inicial) { mutableStateOf(inicial?.celular.orEmpty()) }
        var correo by rememberSaveable(inicial) { mutableStateOf(inicial?.correo.orEmpty()) }
        CrudFormSheet(
            title = if (inicial == null) "Nuevo cliente" else "Editar cliente",
            saving = host.saving,
            error = null,
            onDismiss = host.onDismiss,
            onSave = {
                host.onSave(
                    Cliente(
                        codigo = inicial?.codigo ?: 0L,
                        nombre = nombre.trim(),
                        apellidoPaterno = apellidoPaterno.trim(),
                        apellidoMaterno = apellidoMaterno.trim(),
                        tipoDocumento = tipoDocumento,
                        numeroDocumento = numeroDocumento.trim(),
                        fechaNacimiento = fecha,
                        sexo = sexo,
                        nacionalidad = nacionalidad.trim(),
                        distrito = distrito,
                        direccion = direccion.trim(),
                        telefono = telefono.trim(),
                        celular = celular.trim(),
                        correo = correo.trim()
                    )
                )
            }
        ) {
            FormField("Nombre", nombre, { nombre = it }, Icons.Rounded.Person)
            FormField("Apellido paterno", apellidoPaterno, { apellidoPaterno = it }, Icons.Rounded.Person)
            FormField("Apellido materno", apellidoMaterno, { apellidoMaterno = it }, Icons.Rounded.Person)
            SelectField("Tipo de documento", tipoDocumento, opciones.catalogos[TipoCatalogo.TIPO_DOCUMENTO].orEmpty(), { tipoDocumento = it }, Icons.Rounded.CreditCard)
            FormField("Número de documento", numeroDocumento, { numeroDocumento = it }, Icons.Rounded.Numbers, KeyboardType.Number)
            DateField("Fecha de nacimiento", fecha) { fecha = it }
            SelectField("Sexo", sexo, opciones.catalogos[TipoCatalogo.SEXO].orEmpty(), { sexo = it }, Icons.Rounded.Wc)
            FormField("Nacionalidad", nacionalidad, { nacionalidad = it }, Icons.Rounded.Flag)
            SelectField("Distrito", distrito, opciones.catalogos[TipoCatalogo.DISTRITO].orEmpty(), { distrito = it }, Icons.Rounded.Place)
            FormField("Dirección", direccion, { direccion = it }, Icons.Rounded.Home)
            FormField("Teléfono", telefono, { telefono = it }, Icons.Rounded.Phone, KeyboardType.Phone)
            FormField("Celular", celular, { celular = it }, Icons.Rounded.PhoneAndroid, KeyboardType.Phone)
            FormField("Correo", correo, { correo = it }, Icons.Rounded.Mail, KeyboardType.Email, ImeAction.Done)
        }
    }
}

@Composable
private fun EmpleadosScreen(modulo: Modulo, onBack: () -> Unit) {
    val vm: CrudViewModel<Empleado> = viewModel(key = "crud-empleados", factory = AppViewModelProvider.crud { it.empleados })
    val opcionesVm: OpcionesViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val opciones by opcionesVm.uiState.collectAsStateWithLifecycle()
    CrudModulo(
        modulo = modulo,
        viewModel = vm,
        onBack = onBack,
        itemName = { "${it.nombreCompleto} ${it.usuario}" },
        itemContent = {
            ItemTexts(
                title = it.nombreCompleto,
                subtitle = listOfNotNull("@${it.usuario}", it.rol?.nombre).joinToString(" · "),
                detail = formatoPrecio(it.sueldo),
                modifier = Modifier.weight(1f)
            )
        }
    ) { host ->
        LaunchedEffect(Unit) {
            opcionesVm.cargar(
                listOf(TipoCatalogo.TIPO_DOCUMENTO, TipoCatalogo.SEXO, TipoCatalogo.ESTADO_CIVIL, TipoCatalogo.DISTRITO, TipoCatalogo.ROL)
            )
        }
        val inicial = host.inicial
        var nombre by rememberSaveable(inicial) { mutableStateOf(inicial?.nombre.orEmpty()) }
        var apellidoPaterno by rememberSaveable(inicial) { mutableStateOf(inicial?.apellidoPaterno.orEmpty()) }
        var apellidoMaterno by rememberSaveable(inicial) { mutableStateOf(inicial?.apellidoMaterno.orEmpty()) }
        var tipoDocumento by remember(inicial) { mutableStateOf(inicial?.tipoDocumento) }
        var numeroDocumento by rememberSaveable(inicial) { mutableStateOf(inicial?.numeroDocumento.orEmpty()) }
        var fecha by rememberSaveable(inicial) { mutableStateOf(inicial?.fechaNacimiento.orEmpty()) }
        var sexo by remember(inicial) { mutableStateOf(inicial?.sexo) }
        var estadoCivil by remember(inicial) { mutableStateOf(inicial?.estadoCivil) }
        var nacionalidad by rememberSaveable(inicial) { mutableStateOf(inicial?.nacionalidad ?: "Peruana") }
        var distrito by remember(inicial) { mutableStateOf(inicial?.distrito) }
        var direccion by rememberSaveable(inicial) { mutableStateOf(inicial?.direccion.orEmpty()) }
        var telefono by rememberSaveable(inicial) { mutableStateOf(inicial?.telefono.orEmpty()) }
        var celular by rememberSaveable(inicial) { mutableStateOf(inicial?.celular.orEmpty()) }
        var correo by rememberSaveable(inicial) { mutableStateOf(inicial?.correo.orEmpty()) }
        var usuario by rememberSaveable(inicial) { mutableStateOf(inicial?.usuario.orEmpty()) }
        var clave by rememberSaveable(inicial) { mutableStateOf("") }
        var sueldo by rememberSaveable(inicial) { mutableStateOf(inicial?.sueldo?.let(::numeroEditable).orEmpty()) }
        var rol by remember(inicial) { mutableStateOf(inicial?.rol) }
        var error by remember { mutableStateOf<String?>(null) }
        CrudFormSheet(
            title = if (inicial == null) "Nuevo empleado" else "Editar empleado",
            saving = host.saving,
            error = error,
            onDismiss = host.onDismiss,
            onSave = {
                val sueldoNum = sueldo.aDecimal()
                error = if (sueldoNum == null) "Ingresa un sueldo válido" else null
                if (sueldoNum != null) {
                    host.onSave(
                        Empleado(
                            codigo = inicial?.codigo ?: 0L,
                            nombre = nombre.trim(),
                            apellidoPaterno = apellidoPaterno.trim(),
                            apellidoMaterno = apellidoMaterno.trim(),
                            tipoDocumento = tipoDocumento,
                            numeroDocumento = numeroDocumento.trim(),
                            fechaNacimiento = fecha,
                            sexo = sexo,
                            estadoCivil = estadoCivil,
                            nacionalidad = nacionalidad.trim(),
                            distrito = distrito,
                            direccion = direccion.trim(),
                            telefono = telefono.trim(),
                            celular = celular.trim(),
                            correo = correo.trim(),
                            usuario = usuario.trim(),
                            clave = clave.ifBlank { inicial?.clave.orEmpty() },
                            sueldo = sueldoNum,
                            rol = rol
                        )
                    )
                }
            }
        ) {
            FormField("Nombre", nombre, { nombre = it }, Icons.Rounded.Person)
            FormField("Apellido paterno", apellidoPaterno, { apellidoPaterno = it }, Icons.Rounded.Person)
            FormField("Apellido materno", apellidoMaterno, { apellidoMaterno = it }, Icons.Rounded.Person)
            SelectField("Tipo de documento", tipoDocumento, opciones.catalogos[TipoCatalogo.TIPO_DOCUMENTO].orEmpty(), { tipoDocumento = it }, Icons.Rounded.CreditCard)
            FormField("Número de documento", numeroDocumento, { numeroDocumento = it }, Icons.Rounded.Numbers, KeyboardType.Number)
            DateField("Fecha de nacimiento", fecha) { fecha = it }
            SelectField("Sexo", sexo, opciones.catalogos[TipoCatalogo.SEXO].orEmpty(), { sexo = it }, Icons.Rounded.Wc)
            SelectField("Estado civil", estadoCivil, opciones.catalogos[TipoCatalogo.ESTADO_CIVIL].orEmpty(), { estadoCivil = it }, Icons.Rounded.Favorite)
            FormField("Nacionalidad", nacionalidad, { nacionalidad = it }, Icons.Rounded.Flag)
            SelectField("Distrito", distrito, opciones.catalogos[TipoCatalogo.DISTRITO].orEmpty(), { distrito = it }, Icons.Rounded.Place)
            FormField("Dirección", direccion, { direccion = it }, Icons.Rounded.Home)
            FormField("Teléfono", telefono, { telefono = it }, Icons.Rounded.Phone, KeyboardType.Phone)
            FormField("Celular", celular, { celular = it }, Icons.Rounded.PhoneAndroid, KeyboardType.Phone)
            FormField("Correo", correo, { correo = it }, Icons.Rounded.Mail, KeyboardType.Email)
            FormField("Usuario", usuario, { usuario = it }, Icons.Rounded.AlternateEmail)
            FormField(
                label = "Contraseña",
                value = clave,
                onValueChange = { clave = it },
                icon = Icons.Rounded.Lock,
                isPassword = true,
                placeholder = if (inicial == null) "Contraseña" else "Dejar vacío para mantenerla"
            )
            FormField("Sueldo (S/)", sueldo, { sueldo = it }, Icons.Rounded.Payments, KeyboardType.Decimal, ImeAction.Done)
            SelectField("Rol", rol, opciones.catalogos[TipoCatalogo.ROL].orEmpty(), { rol = it }, Icons.Rounded.AdminPanelSettings)
        }
    }
}
